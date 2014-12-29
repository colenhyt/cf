Event = function(){
    this.name = "event";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.tick = 0;
    if (data_eventdata_feq==null||data_eventdata_feq==0)
    	data_eventdata_feq = 2;
    this.duration = data_eventdata_feq * 60;
    this.duration = 1;
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
	log("event trigger:"+item.prize.id);
	
	this.isTrigger = true;
	var content;
	if (item.type==1){
		content = this.badEvent(item);
	}else {
		var pp = eval ("(" + item.prize + ")");
		g_player.prize(pp);
		content = "<div>"
		content += "<img src='static/img/icon_good.png'>"
		content += "</div>"
		content += "<br><div>"
		content += item.descs
		content += "</div>"
	}
	g_msg.open2("意外保险",content);
}

Event.prototype.badEvent = function(item){
	var imgSrc = "icon_bad"
	var desc;
	var found;
	var insures = g_player.insure;
	if (insures.length>0){		
	for (var i=0;i<insures.length;i++){
		if (insures[i].itemid == item.itemid){
			desc = "(你有保险) 成功避开意外:"+item.descs;
			imgSrc = "icon_good";
			found = true;
			break;
		}
	}
	}
	if (found!=true)
	{
			var pp = eval ("(" + item.prize + ")");
			g_player.prize(pp);
			desc = item.descs;
			var targetInsure = g_insure.findItem(item.itemid);
			var iname = targetInsure?targetInsure.name:"";
			desc += "<br>提示:购买"+iname+"可避免该类意外"	
	}
	var content = "<div>"
	content += "<img src='static/img/"+imgSrc+".png'>"
	content += "</div>"
	content += "<div>"
	content += desc
	content += "</div>"
	return content;
}

//
Event.prototype.update = function(){
	this.tick++;
	if (this.tick%this.duration==0){
		if (this.isTrigger!=true)
		this.triggerEvent();
	}
}
var g_event = new Event();
g_event.init();