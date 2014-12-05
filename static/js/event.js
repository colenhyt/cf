Event = function(){
    this.name = "event";
}

Event.prototype = new Datamgr();

Event.prototype.init = function(){
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
	var ee = tdata[index]; 
	log("event trigger:"+ee.prize.id);
	g_player.prize(ee.prize);
}

//
Event.prototype.update = function(){
	//this.triggerEvent();
}
var g_event = new Event();
g_event.init();