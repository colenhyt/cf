Playerlog = function(){
	this.name = "playerlog";
}

Playerlog.prototype.init = function(){
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,{});
	}
}

Playerlog.prototype.addlog = function(){
	var currDate = new Date();
 	var jsonCurr = Date.parse(currDate);
	var daylog = {feeling:-1,dailyquest:0,logins:[jsonCurr]};
	var tdata = store.get(this.name);
	var currdlog = tdata[currDate.toDateString()];
	if (currdlog) {
		currdlog.logins.push(jsonCurr);
		daylog = currdlog;
	}
	
	tdata[currDate.toDateString()] = daylog;
	store.set(this.name,tdata);
}

Playerlog.prototype.findTodayLog = function(){
	var needSignin = true;
	var needDailyQuest = true;
	var currDate = new Date();
	var tdata = store.get(this.name);
	var todaylog = tdata[currDate.toDateString()];
	if (todaylog){
		if (todaylog.feeling>=0)
			needSignin = false;
		if (todaylog.dailyquest==1)
			needDailyQuest = false;		
	}
	return [needDailyQuest,needSignin];
}

Playerlog.prototype.updateQuest = function(){
	var currDate = new Date();
	var tdata = store.get(this.name);
	var currdlog = tdata[currDate.toDateString()];
	if (currdlog) {
		currdlog.dailyquest = 1;
	}
	tdata[currDate.toDateString()] = currdlog;
	store.set(this.name,tdata);
}

Playerlog.prototype.updateSignin = function(feeling){
	var currDate = new Date();
	var tdata = store.get(this.name);
	var currdlog = tdata[currDate.toDateString()];
	if (currdlog) {
		currdlog.feeling = feeling;
	}
	tdata[currDate.toDateString()] = currdlog;
	store.set(this.name,tdata);
}

// JavaScript Document
Player = function(){
    this.name = "player";
    this.count = 0;
    this.syncDuration = 5;
    this.data = {};
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
       this.pageheader = this.tagname+"header";
}

Player.prototype = new Datamgr();

Player.prototype.init = function(){
    var isRe = false;
    var ret = true;
    this.buildHTML();
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		ret = this.register();
		isRe = true;
	}   
	if (ret==true) 
     this.login(isRe); 
}

Player.prototype.buildHTML = function()
{
		var page = new PageUtil(this.tagname);
            page.addHeader("<button type='button' class='close' data-dismiss='modal'><img src='static/img/close.png' class='cf_title_close top'></button>");
	var header = "<ul id='"+this.id+"Tab' class='nav nav-tabs'>"
            header += "<div id='"+this.pageheader+"'></div>"
           header += "</ul>"	
	page.addHeader(header);
	
	var content = "<div id='player_content' class='cfpage small'></div>"
	page.addContent(content);
	document.write(page.toString());
	
}

Player.prototype.show = function(){
        var   header = "<button class='cf_title_bg'>个人等级</button>"       	
	var tagHeader = document.getElementById(this.pageheader);
	tagHeader.innerHTML = header;
	
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
    
    $('#'+this.tagname).modal({position:50,show: true});       
}

Player.prototype.register = function(){
//    var pname = document.getElementById("player.playername");
//    var pheadicon = document.getElementById("player.headicon");
//    var ppwd = document.getElementById("player.pwd");
    var createtime = Date.parse(new Date());
     var player = {
        "accountid":1,"playerid":-1,"playername":"playerxxx","exp":0,"cash":0,
	quest:[],
        lastsignin:createtime,sex:0,createtime:createtime
        };
           
	var dataobj = $.ajax({url:"/cf/login_register.do?player.accountid="+player.accountid+"&player.playername="+player.playername,async:false});
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = eval ("(" + dataobj.responseText + ")");
			if (obj!=null&&obj.pwd!=null){
				player.playerid = obj.playerid;
				player.pwd = obj.pwd;
			}
		}
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   // return false;
	}	

	store.set(this.name,player);
	return true;
}

Player.prototype.login = function(isRegister){
     this.data = store.get(this.name);
     var player = this.data;
     
	var dataobj = $.ajax({url:"/cf/login_login.do?player.playerid="+player.playerid+"&player.pwd="+player.pwd,async:false});
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = eval ("(" + dataobj.responseText + ")");
			if (obj!=null){
				
			}
		}
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   // return false;
	}	
    
    //head img:
    var head_img = head_imgs[0];
    if (head_imgs.length>player.sex)
        head_img = head_imgs[player.sex];
    head_img.name = this.name;
    g_game.addImg(head_img);
    
    g_playerlog.addlog();
    
 	var dlog = g_playerlog.findTodayLog();	   
    if (dlog[0]==true)
	{
	        var accept = g_quest.onAcceptDaily();
	        if (accept)
	        {
	        	g_playerlog.updateQuest();
	        }
    }
    
    //player props
    this.flushPageview();
    	   
	if (dlog[1]==true)
	{
		g_signin.start(0);
	}
   return true;
}

Player.prototype.flushPageview = function() {
    var tag = document.getElementById("tagcash");
    tag.innerHTML = this.data.cash;	
    tag.style.display = "";
    tag = document.getElementById("tagcard");
    tag.innerHTML = this.data.exp;	
    tag.style.display = "";
    tag = document.getElementById("tagplayer");
	var lv = g_title.getLevel();
    tag.innerHTML = g_title.getData(lv).name;	
    tag.style.display = "";
    tag = document.getElementById("taglevel");
    tag.innerHTML = lv;	
    tag.style.display = "";
}

Player.prototype.updateData = function(prop) {
    var sets =[];
    for (key in prop){
		this.data[key] = prop[key];
    }
	this.flushPageview();
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
     		if (v<0)
     			v = 0;
			prop[key] = v;
     	}
     }  
     this.updateData(prop);
}

Player.prototype.getStocks = function() {
    var stocks = this.data.stock;
    if (stocks==null)
    	stocks = [];
    var tstocks = {};
    for (var i=0;i<stocks.length;i++){
	var items = stocks[i].items;
	var amount = 0;
	var totalCount = 0;
	for (var j=0;j<items.length;j++){
	    if (items[j].status==0) {
		amount += items[j].price * items[j].count;
		totalCount += items[j].count;
	    }
	}
 	tstocks[stocks[i].id] = {amount:amount,count:totalCount};
   }
    return tstocks;
}

Player.prototype.getPlayerStock = function(stock) {
    var profit = 0;
    var avgPrice = 0;
    var pstock = this.getStocks()[stock.id];
     if (pstock!=null) {
	profit = pstock["amount"] - pstock["count"]*stock.price;
	avgPrice = pstock["amount"]/pstock["count"];
	
    }    
    return {profit:profit,avgPrice:avgPrice};
}

Player.prototype.syncData = function(){
	//alert('pp.syncData');
}   

//store.remove("player");
//store.remove("playerlog");
var g_playerlog = new Playerlog()
g_playerlog.init();
var g_player = new Player();