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
    this.stock = [];
    this.insure = [];
    this.saving = [];
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.pageheader = this.tagname+"header";
    this.graphName = this.name+"graph";
}

Player.prototype = new Datamgr();

Player.prototype.init = function(){
	this.data = store.get(this.name);
}

Player.prototype.getTotal = function(data) {
	var saving = 0;
	if (data.saving.length>0)
		saving = data.saving[0].amount;
		
	var saving2 = 0;
	for (var i=1;i<data.saving.length;i++){
		saving2 += data.saving[i].amount;
	}
	var insure = 0;
	for (var i=0;i<data.insure.length;i++){
		insure += data.insure[i].amount;
	}
	var stock = 0;
	for (var i=0;i<data.stock.length;i++){
		stock += data.stock[i].amount;
	}
	return {saving:ForDight(saving),saving2:ForDight(saving2),insure:ForDight(insure),stock:ForDight(stock)};
}

Player.prototype.flushPageview = function() {
    var tag = document.getElementById("tagsaving");
    tag.innerHTML = "存款: " +ForDight(this.saving[0].amount);	
    tag.style.display = "";
    tag = document.getElementById("tagweektop");
    tag.innerHTML = "周排名: "+this.data.weektop;	
    tag.style.display = "";
    tag = document.getElementById("tagplayerinfo");
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
    	if (key=="cash")
    		this.saving[0].amount = prop[key];
    	else
			this.data[key] = prop[key];
    }
	this.flushPageview();
	this.syncData2();
    store.set(this.name,this.data);
}

Player.prototype.clone = function(data) {
	var pl = this.data;
	data.playerid = pl.playerid;
	data.accountid = pl.accountid;
	data.sex = pl.sex;
	data.openstock = pl.openstock;
	data.createtime = pl.createtime;
	data.playername = pl.playername;
	data.pwd = pl.pwd;
	data.exp = pl.exp;
	if (pl.quest!=null)
		data.quest = pl.quest.concat();
}

Player.prototype.syncData2 = function(){
	var data = {};
	this.clone(data);
	data.quest = JSON.stringify(data.quest);
	var updateStr = "player="+JSON.stringify(data);
	//alert("任务同步到服务器:"+data.quest);
	try  {
		$.ajax({type:"post",url:"/cf/login_update.do",data:updateStr,success:this.syncCallback});
	}   catch  (e)   {
	    document.write(e.name);
	}   
}

Player.prototype.syncCallback=function(dataobj){
	try    {
			var obj = eval ("(" + dataobj + ")");
			//alert(obj);
	}   catch  (e)   {
	    document.write(e.name  +   " :  "   +  dataobj);
	}   
}

Player.prototype.prize = function(prizes) {
	var prop = {};
 	var cashUpdate = false;
     for (var i=0;i< prizes.length;i++)
     {
     	var key = "";
     	if (prizes[i].t==ITEM_TYPE.EXP){
     		key = "exp";    
     		var v = this.data[key]+prizes[i].v;
     		if (v<0)
     			v = 0;
			prop[key] = v;
     	}else if (prizes[i].t==ITEM_TYPE.CASH){
     		this.saving[0].amount += prizes[i].v;
     		cashUpdate = true;
     	}
     }
     if (cashUpdate==true){
 		try  {
			var obj = {id:this.saving[0].id,amount:this.saving[0].amount};
			var updateStr = obj2ParamStr("saving",obj);
			$.ajax({type:"post",url:"/cf/saving_update.do",data:updateStr,success:this.syncCallback});
		}   catch  (e)   {
	   	 logerr(e.name);
		}     	
     }
     this.updateData(prop);
}

Player.prototype.getDataMap = function(tname) {
    var tdata = this.getData(tname);
    var tdataMap = {};
    for (var i=0;i<tdata.length;i++){
		var data = tdata[i];
		if (tdataMap[data.itemid]==null){
			tdataMap[data.itemid] = {qty:data.qty,amount:data.amount,profit:data.profit};
		}else{
			tdataMap[data.itemid].qty += data.qty;
			tdataMap[data.itemid].amount += data.amount;
			tdataMap[data.itemid].profit += data.profit;
		}
   }
    return tdataMap;
}

Player.prototype.getData = function(tname){
	var tdata;
	if (tname=="insure"){
		this.insure = this.insure?this.insure:[];
		tdata = this.insure;
	}else if (tname=="stock"){
		this.stock = this.stock?this.stock:[];
		tdata = this.stock;
	}else if (tname=="saving"){
		this.saving = this.saving?this.saving:[];
		tdata = this.saving;
	}
	return tdata;
}

Player.prototype.getItem = function(tname,id){
	var tdata = this.getData(tname);
	var item;
	if (tname=="insure"){
		item = g_insure.findItem(id);
	}else if (tname=="stock"){
		item = g_stock.findItem(id);
	}else if (tname=="saving"){
		item = g_saving.findItem(id);
	}
	return item;
}

Player.prototype.getItemData = function(tname,item) {
    var profit = 0;
    var avgPrice = 0;
    var qty = 0;
    var amount = 0;
    var pitem = this.getDataMap(tname)[item.id];
     if (pitem!=null) {
     	var quote = g_stock.findLastQuote(item.id);
     	var ps = 0;
     	if (tname==g_stock.name){
     		if (quote!=null) 
     			ps = quote.price;
			profit = pitem["amount"] - pitem["qty"]*ps;
     	}else if (tname==g_saving.name){
     		profit = pitem.profit;
     	}else {
     		ps = item.price;
			profit = pitem["amount"] - pitem["qty"]*ps;
     	}
     	if (!profit)
     		profit = 0;
		avgPrice = pitem["amount"]/pitem["qty"];
		amount = pitem.amount;
		qty = pitem.qty;
    }    
    return {profit:profit,avgPrice:avgPrice,qty:qty,amount:amount};
}

Player.prototype.buyItem = function(tname,id,qty,amount2){
	if (id<=0||qty==0) return false;
	
	var tdata = this.getData(tname);
	var item = this.getItem(tname,id)
		
	if (item==null) return false;
	
	
	var cash = this.saving[0].amount;
	var ps = 0;
	if (tname==g_stock.name){
		var quote = g_stock.findLastQuote(id); 
		ps =  quote.price;
	}else
		ps = item.price;
	var amount = ps * qty;
	if (cash<amount){
		 g_msg.open('你的钱不够');
		return;
	}
	if (amount2!=null)
		amount = amount2;
		
	amount = ForDight(amount);
		
	var tgoods = {itemid:item.id,playerid:this.data.playerid,name:item.name,
			qty:qty,price:item.price,amount:amount,
			createtime:Date.parse(new Date())};
			
	var dataParam = obj2ParamStr(tname,tgoods);
	var ret = myajax(tname+"_add",dataParam);
	if (ret==null||ret.code!=0)
	{
		g_msg.open("操作失败:"+ERR_MSG[ret.code]);
		return {ret:false};
	}
	
	tdata.push(tgoods);
				
	cash -= amount;
	var pupdate = {"cash":cash};
	this.updateData(pupdate);
	g_quest.onBuyItem(tname,item,qty);
	
		
	return {ret:true,item:tgoods};
		
}

Player.prototype.find = function(playerid){
     var serverPlayer = {};
	var dataobj = $.ajax({url:"/cf/login_get.do?player.playerid="+playerid,async:false});
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = eval ("(" + dataobj.responseText + ")");
			if (obj!=null){
				serverPlayer.data = obj;
				if (obj.saving)
					serverPlayer.saving = eval ("(" + obj.saving + ")");
				if (obj.quest)
					serverPlayer.data.quest = eval ("(" + obj.quest + ")");
				if (obj.stock)
					serverPlayer.stock = eval ("(" + obj.stock + ")");
				if (obj.insure)
					serverPlayer.insure = eval ("(" + obj.insure + ")");
			}
		}
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   // return false;
	}
	return serverPlayer;
}

//store.remove("player");
//store.remove("playerlog");
var g_playerlog = new Playerlog()
g_playerlog.init();
var g_player = new Player();
g_player.init();