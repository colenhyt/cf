// JavaScript Document
Title = function(){
	this.name = "title"
	this.tagname = "my"+this.name;
	this.item = {};
	this.data = [];
}

Title.prototype = new Datamgr();

Title.prototype.init = function(){
	//this.loadData();
}

Title.prototype.loadDataCallback = function(tx,results){
	for (var i=0;i<results.rows.length;i++){
		var item = results.rows.item(i);
        g_title.data.push(item);
    }
    g_title.loadTitle();
}

Title.prototype.loadTitle = function(){
	var pexp = g_player.data.exp;
	var id = -1;
	for (var i=0;i<this.data.length;i++){
		var item = this.data[i];
		if (item.exp<=pexp){
			if (id==-1||this.data[i].exp<item.exp)
				id = i;
		}
    }
    if (id>=0){
    	this.item = this.data[id];
    	//alert('称号是：'+this.item.name);
    }
}
var g_title = new Title();
g_title.init();

