// JavaScript Document
	var loadingTag;

var RES_START = 0;
var RES_DISTJS = 5;
var RES_CSS	= 10;
var RES_DATAJS = 15;
var RES_JS1 = 20;
var RES_JS2 = 25;
var RES_JS3 = 30;
var RES_INIT = 40;
var RES_FINISH = 100;

Loading = function(){
	this.name = "loading"
	this.currPer = 0;
	var div = document.createElement("div");
	div.id = "loading";
	div.style="position:absolute; left: 100px;top: 0px;height:10px;width:10px; color:rgb(0, 0, 0)";
	document.body.appendChild(div);
	loadingTag = document.getElementById("loading");
}

Loading.prototype.add = function(per){
	this.set(this.currPer+per);
}

Loading.prototype.set = function(per){
 if (loadingTag==null) return;
 
 loadingTag.innerHTML = per+"%";
 this.currPer = per;
if (per==RES_FINISH){
	$('#loading').remove();
}
}

var g_loading = new Loading();

