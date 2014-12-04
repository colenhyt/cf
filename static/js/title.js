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

Title.prototype.getName = function(lv){
	var pexp = g_player.data.exp;
	var id = -1;
	var name = "";
	var tdata = store.get(this.name);
	for (var i=0;i<tdata.length;i++){
		var item = tdata[i];
		if (item.level==lv){
			name = item.name;
			break;
		}
    }
    return name;
}
var g_title = new Title();
g_title.init();

