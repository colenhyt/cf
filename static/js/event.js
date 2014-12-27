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
    this.duration = 500000;
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
	
	var pp = eval ("(" + item.prize + ")");
	//g_player.prize(pp);
	g_msg.open2("意外保险",item.desc);
}

//
Event.prototype.update = function(){
	this.tick++;
	if (this.tick%this.duration==0){
		this.triggerEvent();
	}
}
var g_event = new Event();
g_event.init();