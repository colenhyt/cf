// JavaScript Document
Title = function(){
	this.name = "title"
	this.tagname = "my"+this.name;
	this.item = {};
	this.data = [];
}

Title.prototype = new Datamgr();

Title.prototype.init = function(){
store.remove(this.name);
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_titledata);
	} 
}

Title.prototype.getLevel = function(exp){
	var pexp = g_player.data.exp;
	if (exp&&exp>0)
	 pexp = exp;
	var lv = 0;
	var tdata = store.get(this.name);
	for (var i=0;i<tdata.length;i++){
		if (tdata[i].exp<=pexp){
			lv = tdata[i].level;
		}
    }
    return lv;
}

Title.prototype.getData = function(lv){
	var id = -1;
	var item;
	var tdata = store.get(this.name);
	if (lv>tdata.length)
		lv = tdata.length;
	for (var i=0;i<tdata.length;i++){
		if (tdata[i].level==lv){
			item = tdata[i];
			break;
		}
    }
    return item;
}

var g_title = new Title();


