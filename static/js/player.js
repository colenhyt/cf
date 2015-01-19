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
    this.stockids = [];
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
	var saving = data.saving[1].amount;
		
	var saving2 = 0;
	for (key in data.saving){
		if (key!=1&&data.saving[key])
			saving2 += data.saving[key].amount;
	}
	var insure = 0;
	for (key in data.insure){
		insure += data.insure[key].amount;
	}
	var stock = 0;
	for (key in data.stock){
		var items = data.stock[key];
		for (var i=0;i<items.length;i++){
		 stock += items[i].qty*items[i].price;
		}
	}
	total = parseInt(saving+saving2+insure+stock);
	return {saving:parseInt(saving),saving2:parseInt(saving2),insure:parseInt(insure),stock:parseInt(stock),total:total};
}

Player.prototype.flushPageview = function() {
    var tag = document.getElementById("tagsaving");
    tag.innerHTML = "存款: " +parseInt(this.saving[1].amount);	
    var strTop = "";
    if (this.data.weektop>0){
    	strTop = this.data.weektop;
    }
    tag = document.getElementById("tagweektop");
    tag.innerHTML = "周排名: "+strTop;	
    tag = document.getElementById("tagplayerinfo");
	var lv = g_title.getLevel();
    tag.innerHTML = g_title.getData(lv).name;	
    tag = document.getElementById("taglevel");
    tag.innerHTML = lv;	
    
}

Player.prototype.updateData = function(prop) {
    var sets =[];
	var oldLevel = g_title.getLevel();
   for (key in prop){
    	if (key=="cash")
    		this.saving[1].amount = prop[key];
    	else
			this.data[key] = prop[key];
    }
     var newLevel = g_title.getLevel();
     if (newLevel>oldLevel)
     	g_uplevel.open();
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
			var obj = cfeval(dataobj);
			//alert(obj);
	}   catch  (e)   {
	    document.write(e.name  +   " :  "   +  dataobj);
	}   
}

//破产处理
Player.prototype.broke = function() {
  var amount = 0 - this.saving[1].amount;
  if (amount<=0) return;
  
  var savingids = [];
  //定期:
  for (key in this.saving)
  {
   if (key!=1){
   	amount -= this.saving[key];
   	if (amount<=0)
   	 break;
   }
  }
  
  //股票:
  var stockids = [];
  if (amount>0)
  {
   for (key in this.stock)
   {
   
   }
  }
  
  //破产
  if (amount>0)
  {
   
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
     		this.saving[1].amount += prizes[i].v;
     		cashUpdate = true;
     	}
     }
     if (this.saving[1].amount<0)	//
     {
      this.broke();
     }
     
     if (cashUpdate==true){
 		try  {
			var obj = {id:this.saving[1].id,amount:this.saving[1].amount,playerid:this.data.playerid};
			var updateStr = obj2ParamStr("saving",obj);
			$.ajax({type:"post",url:"/cf/saving_updatelive.do",data:updateStr,success:this.syncCallback});
		}   catch  (e)   {
	   	 logerr(e.name);
		}     	
     }
     this.updateData(prop);
}

Player.prototype.setStockIds = function(){
	this.stockids = [];
	for (stockid in this.stock){
		var qty = 0;
		for (var i=0;i<this.stock[stockid].length;i++){
			qty += this.stock[stockid][i].qty;
		}	
		if (qty>0)
			this.stockids.push(stockid);
	}
	if (this.stockids.length>0){
		g_stock.hasTip = true;
	}
}

Player.prototype.getData = function(tname){
	var tdata;
	if (tname=="insure"){
		this.insure = this.insure?this.insure:{};
		tdata = this.insure;
	}else if (tname=="stock"){
		this.stock = this.stock?this.stock:{};
		tdata = this.stock;
	}else if (tname=="saving"){
		this.saving = this.saving?this.saving:{};
		tdata = this.saving;
	}
	return tdata;
}

Player.prototype.getItem = function(tname,id){
	var items = store.get(tname);
	return items[id];
}

//快到期保险产品
Player.prototype.getOfftimeInsure = function() {
	var now = new Date();
	var itemids = [];
	var mills = Date.parse(now);
	for (itemid in this.insure){
		var ctime = this.insure[itemid].createtime;
		if (mills-ctime<3000){
			itemids.push(itemid);
		}
	}
	return itemids;
}

//未完成任务；
//快到期保险产品
Player.prototype.getQuests = function(status) {
	var itemids = [];
	if (!this.data||!this.data.quest)
		return itemids;
		
	for (var i=0;i<this.data.quest.length;i++){
		if (this.data.quest[i].status==status){
			itemids.push(this.data.quest[i].itemid);
		}
	}
	return itemids;
}
 
Player.prototype.getStockItem = function(itemid) {
	var qty = 0;
	var amount = 0;
	var pitems = this.stock[itemid];
	if (pitems){
		for (var i=0;i<pitems.length;i++){
	    	amount += pitems[i].amount;
	    	qty += pitems[i].qty;
		}
	}
	return {qty:qty,amount:amount};
}
 
Player.prototype.setStockItemPs = function(itemid,newps) {
	var pitems = this.stock[itemid];
	if (!pitems) return;
	
	for (var i=0;i<pitems.length;i++){
	 pitems[i].price = newps;
	}
}

Player.prototype.getSavingItem = function(itemid) {
	var pitem = this.saving[itemid];
	if (!pitem)
		pitem = {amount:0,profit:0,qty:0};
	return pitem;
}

Player.prototype.getInsureItem = function(itemid) {
	var pitem = this.insure[itemid];
	if (!pitem)
		pitem = {amount:0,price:0,profit:0,qty:0};
	return pitem;
}

Player.prototype.buyItem = function(tname,id,qty,ps){
	if (id<=0||qty==0) return false;
	
	var items = store.get(tname);
	var item = items[id];
		
	if (item==null) return false;
	
	var cash = this.saving[1].amount;
	var amount = parseInt(ps * qty);
	if (cash<amount){
		 g_msg.tip('你的钱不够');
		return;
	}

	var tgoods = {itemid:id,playerid:this.data.playerid,
			qty:qty,price:ps,amount:amount};
			
	var dataParam = obj2ParamStr(tname,tgoods);
	//alert("增加: "+dataParam);
	var ret = myajax(tname+"_add",dataParam);
	if (ret==null||ret.code!=0)
	{
		g_msg.tip("操作失败:"+ERR_MSG[ret.code]);
		return {ret:false};
	}
	
	var pitems = this.getData(tname);
	if (tname==g_saving.name||tname==g_insure.name){
		if (amount>0){
			pitems[id] = tgoods;
			pitems[id].createtime = Date.parse(new Date());
			pitems[id].profit = 0;
		}else
			pitems[id] = null;
	}else if (tname==g_stock.name) {
		if (!pitems[id])
			pitems[id] = [];
		if (qty>0){
		 pitems[id].push(tgoods);
		}else {
		 var needRemoveQty = 0 - qty;
		 for (var i=0;i<pitems[id].length;i++){
		   if (pitems[id][i].qty<=needRemoveQty){
		    needRemoveQty -= pitems[id][i].qty;
		    pitems[id].splice(i,1);
		    i--;
		   }else {
		    pitems[id][i].qty -= needRemoveQty;
		    pitems[id][i].amount = pitems[id][i].qty*pitems[id][i].price;
		    break;
		   }
		 }
		}
		g_player.setStockIds();
	}
				
	cash -= amount;
	var pupdate = {"cash":cash};
	this.updateData(pupdate);
	g_quest.onBuyItem(tname,item,qty);
	
	tgoods.name = item.name;	
	return {ret:true,item:tgoods};
		
}

Player.prototype.find = function(playerid){
     var serverPlayer = {};
	var dataobj = $.ajax({url:"/cf/login_get.do?player.playerid="+playerid,async:false});
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = cfeval(dataobj.responseText);
			if (obj!=null){
				serverPlayer.data = obj;
				if (obj.saving)
					serverPlayer.saving = cfeval(obj.saving);
				if (obj.quest)
					serverPlayer.data.quest = cfeval(obj.quest);
				if (obj.stock)
					serverPlayer.stock = cfeval(obj.stock);
				if (obj.insure)
					serverPlayer.insure = cfeval(obj.insure);
			}
		}
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   // return false;
	}
	return serverPlayer;
}

store.remove("player");
store.remove("playerlog");
var g_playerlog = new Playerlog()
g_playerlog.init();
var g_player = new Player();
g_player.init();