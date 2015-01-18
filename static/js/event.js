Event = function(){
    this.name = "event";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.tick = 0;
}

Event.prototype = new Datamgr();

Event.prototype.init = function(){
store.remove(this.name);
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_eventdata);
	}  
}

//随机事件触发
Event.prototype.triggerEvent = function(){
	var tdata = store.get(this.name);
	var index = Math.floor(Math.random()*tdata.length);
	var item = tdata[index]; 

	if (item.type==1){
		this.badEvent(item);
	}else {
		this.goodEvent(item);
	}
}

Event.prototype.goodEvent = function(item){
	var found;
	var insures = g_player.insure;
	if (insures[item.itemid]!=null){		
			found = true;
	}
	var content = ""
	//if (found==true)
	{
	var pp = cfeval(item.prize);
	var money = itemValue(pp,ITEM_TYPE.CASH);
			var targetInsure = store.get(g_insure.name)[item.itemid];
			var iname = targetInsure?targetInsure.name:"";
			content = "<div>"
			content += "<img src='static/img/icon_good.png'>"
			content += "</div>"
			content += "<div>"
			content += item.descs+"<br>"
			content += "获得金钱: "
			content += "<span id='cfevent_prize' class='cfevent_prize'>"+ money+"</span>"
			content += "</div>"
	}
	g_msg.openModal(item.name,content,"g_event.eventOkCallback",item.itemid);
	
}

Event.prototype.badEvent = function(item){
	var insures = g_player.insure;
	var pp = cfeval(item.prize);
	var tip = itemStr2(pp,",");
	if (insures[item.itemid]!=null){
		var desc = "成功避开意外<span style='color:yellow'>"+item.descs;
		var content = "</span><div>"
		content += "<img src='static/img/icon_good.png'>"
		content += "</div>"
		content += "<div>"
		content += desc +"<br>"
		content += "减少损失:"+tip
		content += "</div>"
		//alert('成功避开意外')
		g_msg.openModal(item.name,content);
	}else{
		var money = 0-itemValue(pp,ITEM_TYPE.CASH);
		var targetInsure = store.get(g_insure.name)[item.itemid];
		var iname = targetInsure?targetInsure.name:"";
		var content = "<div>"
		content += "<img src='static/img/icon_bad.png'>"
		content += "</div>"
		content += "<div>"
		content += item.descs +"<br>"
		content += "损失金钱: "
		content += "<span id='cfevent_prize' class='cfevent_prize'>"+ money+"</span>"
		content += "<br>"
		content += "(你可购买<span style='color:red'>"+iname+"</span>来避免该意外)"
		content += "</div>"
		g_msg.openModal(item.name,content,"g_event.eventOkCallback",item.itemid);
	}

}

Event.prototype.eventOkCallback = function(itemid){
	var tdata = store.get(this.name);
	var item;
	for (var i=0;i<tdata.length;i++){
		if (tdata[i].itemid==itemid){
			item = tdata[i];
			break;
		}
	}
	
  var div=$("#cfevent_prize");
  div.animate({fontSize:'2.5em'},300);
  div.animate({left:'-='+getSizes().EventMoney[0],top:'-='+getSizes().EventMoney[1]},1000);
  div.animate({fontSize:'1em'},300,function(){
    div.remove();
 	if (item){
	   var pp = cfeval(item.prize);
	   g_player.prize(pp);
	}
	g_msg.hide();
   });
  
}

Event.prototype.aniCallback = function(){
var div=$("#cfevent_prize");
 div.remove();
}

//
Event.prototype.update = function(){
	this.tick++;
	//g_msg.tip(this.tick);
	if (this.tick%EventTriggerTime==0)
	{
		this.triggerEvent();
	}
}
var g_event = new Event();
g_event.init();