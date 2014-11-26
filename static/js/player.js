// JavaScript Document
Player = function(){
    this.name = "player";
    this.count = 0;
    this.syncDuration = 5;
    this.data = {};
}

Player.prototype = new Datamgr();

Player.prototype.init = function(){
    g_game.register(this);
    this.loadData();
}

Player.prototype.loadDataCallback = function(tx,results){
	if (results.rows.length==0){
	  // $('#myregister').modal('show');  
	   g_player.register_c(0);	   
    }else {
        var item = results.rows.item(0);
        g_player.login(item);
    }
}


Player.prototype.register_c = function(sex){
    var createtime = Date.parse(new Date());
    var item = {
        "accountid":1,"playerid":1,"playername":"pname","pwd":"pwd","exp":100,"cash":100,
	quest:"[]",
        lastsignin:createtime,sex:sex,createtime:createtime
        };
	
        var items = [];
        items.push(item);
       var ret = g_db.insertJson(game_tables[this.name], items);	

      // alert("register"+item.createtime+";sex:"+item.sex);
       var init = g_game.init_client();
       if (init==true){
            g_player.login(item,true);              
       }else {
        alert("game client init failed");
       }
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

Player.prototype.login = function(item,isRegister){
	//var dataobj = $.ajax({url:"/cf/login_login.do?player.playername="+playername+"&player.pwd="+pwd,async:false});
//	var obj = eval ("(" + dataobj.responseText + ")");
     this.data = item;
     this.quest = eval(item.quest);
    
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
    	alert('isregister');
    }else {
	    //load signin:
	    var lastDate = new Date(item.lastsignin);
	    var currDate = new Date();
	   	if (currDate.getMonth()!=lastDate.getMonth()||currDate.getDate()!=lastDate.getDate())
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
	sets.push({"name":key,"value":prop[key]});
	
	if (key=="cash") {
	    this.updateView_cash();
	}
    }
    
    var row = [];
    row.push(sets);
    g_db.update(game_tables[this.name],row,[{'name':'playerid','value':this.data.playerid}]);
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
     var items = [];
     for (var i=0;i< logs.length;i++)
     {
     	logs[i].playerid=this.data.playerid;
     	items.push(logs[i]);
     }  
      
     var ret = g_db.insertJson(game_tables["playerlog"], items);	
}

var g_player = new Player();
g_player.init();
