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
	g_msg.tip("event trigger:"+item.id);
	
	//this.isTrigger = true;
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
			g_player.prize(pp);
			var targetInsure = store.get(g_insure.name)[item.itemid];
			var iname = targetInsure?targetInsure.name:"";
			content = "<div>"
			content += "<img src='static/img/icon_good.png'>"
			content += "</div>"
			content += "<div>"
			content += item.descs+"<br>"
			content += "获得: "+itemStr2(pp,",");
			content += "</div>"
	}
	g_msg.open2(item.name,content);
	
}

Event.prototype.badEvent = function(item){
	var insures = g_player.insure;
	var pp = cfeval(item.prize);
	var tip = itemStr2(pp,",");
	if (insures[itemid]!=null){
		var desc = "成功避开意外:"+item.name;
		var content = "<div>"
		content += "<img src='static/img/icon_good.png'>"
		content += "</div>"
		content += "<div>"
		content += desc +"<br>"
		content += "减少损失:"+tip
		content += "</div>"
		g_msg.open2(item.name,content);
	}else{
		g_player.prize(pp);
		var targetInsure = store.get(g_insure.name)[item.itemid];
		var iname = targetInsure?targetInsure.name:"";
		var content = "<div>"
		content += "<img src='static/img/icon_bad.png'>"
		content += "</div>"
		content += "<div>"
		content += item.descs +"<br>"
		content += "损失: "+ tip+"<br>"
		content += "提示:购买<span style='color:red'>"+iname+"</span>可避免该类意外"
		content += "</div>"
		g_msg.open2(item.name,content);
	}

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