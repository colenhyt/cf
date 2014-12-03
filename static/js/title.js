// JavaScript Document
Title = function(){
	this.name = "title"
	this.tagname = "my"+this.name;
	this.item = {};
	this.data = [];
}

Title.prototype = new Datamgr();

Title.prototype.init = function(){
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_titledata);
	} 
}

Title.prototype.show = function(){
	var pexp = g_player.data.exp;
	var id = -1;
	var tdata = store.get(this.name);
	for (var i=0;i<tdata.length;i++){
		var item = tdata[i];
		if (item.exp<=pexp){
			if (id==-1||tdata[i].exp<item.exp)
				id = i;
		}
    }
    if (id>=0){
    	this.item = tdata[id];
    	alert('称号是：'+this.item.name);
    }
}
var g_title = new Title();
g_title.init();

