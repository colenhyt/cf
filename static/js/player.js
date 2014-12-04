// JavaScript Document
Player = function(){
    this.name = "player";
    this.count = 0;
    this.syncDuration = 5;
    this.data = {};
	this.tagname = "my"+this.name
}

Player.prototype = new Datamgr();

Player.prototype.init = function(){
    var isRe = false;
    this.buildHTML();
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		this.register_c(0);
		isRe = true;
	}    
	
     this.login(isRe); 
}

Player.prototype.buildHTML = function()
{
	var page = new PageUtil(this.tagname,0,'modal-content2');
	var content = page.buildHeader1('个人等级','');
	page.addHeader(content);

	content = "<div id='player_content'></div>"
	page.addContent(content);

	document.write(page.toString());
}

Player.prototype.show = function(){
	var texp = store.get("exp");
	var lv = g_title.getLevel();
	var content = "<div class='cf-signin-prize'>"
	content += "<table>"
	content += " <tr>"
	content += "  <td width='100'>"
    content += "<img src='"+head_imgs[this.data.sex].src+"'/>"
	content += "</td>"
	content += "   <td>"
    content += "等级:"+lv+"<br>"	
    content += "经验:"+g_title.getData(lv).exp+"/"+this.data.exp	
	content += "</td>"
	content += " </tr>"
	content += "</table>"
    content += " </div>"
    content +=            " <div class='cf-signin-feeling'> "
    content += "当前等级称号:"+g_title.getData(lv).name+"<br>"
    content += "下个等级称号:"+g_title.getData(lv+1).name+"<br>"
    content +=            " </div>"
    
    var get = document.getElementById("player_content");
    get.innerHTML = content;
    
    $('#'+this.tagname).modal('show');       
}

Player.prototype.register_c = function(sex){
    var createtime = Date.parse(new Date());
    var item = {
        "accountid":1,"playerid":1,"playername":"pname","pwd":"pwd","exp":100,"cash":100,
	quest:"[]",
        lastsignin:createtime,sex:sex,createtime:createtime
        };
	
	store.set(this.name,item);
}

Player.prototype.register = function(){
    var pname = document.getElementById("player.playername");
    var pheadicon = document.getElementById("player.headicon");
    var ppwd = document.getElementById("player.pwd");
	var dataobj = $.ajax({url:"/cf/login_register.do?player.accountid=1&player.playername="+pname.value+"&player.pwd="+ppwd.value,async:false});
	var obj = eval ("(" + dataobj.responseText + ")");
	if (obj.playername!=null){
        var row = [];
        row.push([
            {'name' : 'accountid','value' : obj.accountid},
            {'name' : 'playerid','value' : obj.playerid},
            {'name' : 'playername','value' : obj.playername},
            {'name' : 'pwd','value' : obj.pwd},
            {'name' : 'version','value' : obj.version},
        ]);
 	
       var ret = g_db.insert(game_tables[this.name], row);	              
	}
}

Player.prototype.login = function(isRegister){
	//var dataobj = $.ajax({url:"/cf/login_login.do?player.playername="+playername+"&player.pwd="+pwd,async:false});
//	var obj = eval ("(" + dataobj.responseText + ")");
     this.data = store.get(this.name);
     var item = this.data;
    
    //head img:
    var head_img = head_imgs[0];
    if (head_imgs.length>item.sex)
        head_img = head_imgs[item.sex];
    head_img.name = this.name;
    g_game.m_scene.m_map.addImg(head_img);
    
    //cash div
    this.updateView_cash();
    
   
    if (isRegister==true)
    {
    	g_signin.start(0);
    }else {
	    //load signin:
	    var lastDate = new Date(item.lastsignin);
	    var currDate = new Date();
	   	//if (currDate.getMonth()!=lastDate.getMonth()||currDate.getDate()!=lastDate.getDate())
	    {//todo
	        g_signin.start(0);
	    }
    }
   
}

Player.prototype.updateView_cash = function() {
    var tagcash = document.getElementById("tagcash");
    tagcash.innerHTML = "<b>"+this.data.cash+"</b>";	
}

Player.prototype.updateData = function(prop) {
    var sets =[];
    for (key in prop){
		this.data[key] = prop[key];
		if (key=="cash") {
		    this.updateView_cash();
		}
    }
    store.set(this.name,this.data);
}

Player.prototype.prize = function(prizes) {
	var prop = {};
     for (var i=0;i< prizes.length;i++)
     {
     	var key = "";
     	if (prizes[i].t==ITEM_TYPE.CASH)
     		key = "cash";
     	else if (prizes[i].t==ITEM_TYPE.EXP)
     		key = "exp";    
     	if (key.length>0){	
     		var v = this.data[key]+prizes[i].v;
			prop[key] = v;
     	}
     }  
     this.updateData(prop);
}
     
Player.prototype.syncData = function(){
	//alert('pp.syncData');
}   

Player.prototype.logPlayer = function(logs) {
	var tdata = store.get("playerlog");
	if (tdata==null)
	{
		store.set("playerlog",[]);
		tdata = store.get("playerlog");
	}
     for (var i=0;i< logs.length;i++)
     {
     	tdata.push(logs[i]);
     }  
 	store.set("playerlog",tdata);
}

var g_player = new Player();


