var loadingTag;

var RES_START = 0;
var RES_DISTJS = 5;
var RES_DISTJS1 = 8;
var RES_DISTJS2 = 12;
var RES_CSS	= 18;
var RES_CSS2 = 23;
var RES_DATAJS = 27;
var RES_JS1 = 30;
var RES_JS2 = 35;
var RES_JS3 = 40;
var RES_INIT = 50;
var RES_FINISH = 100;

Loading = function(){
	this.name = 'loading'
	this.currPer = 0;
	var div = document.createElement('div');
	div.id = 'loading';
	var left = window.screen.width/2;
	var top = window.screen.height/2-100;
	div.style.position='absolute';
	div.style.left= left+'px';
	div.style.top=top+'px';
	document.body.appendChild(div);
	loadingTag = document.getElementById('loading');
}

Loading.prototype.add = function(per){
	this.set(this.currPer+per);
}

Loading.prototype.set = function(per){
 if (loadingTag==null) return;
 
 loadingTag.innerHTML = per+'%';
 this.currPer = per;
if (per==RES_FINISH){
	$('#loading').remove();
}
}

var g_loading = new Loading();