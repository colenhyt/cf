function aaa(){
	log("aaa");
}

function aaa2(){
	//alert("aaa2");
}

Main = function(){
}

Main.prototype.init = function(){
	$.getJSON("/cf/login_connect.do", function(json){
		if (json.code==-1)
			alert('联网失败，请检查你的网络');
	})	
	
	//loadStyle('static/css/cf.css');
	//outputCssFiles(320);
	//outputCssFiles(480);
	//outputCssFiles(360);
	//return;
	
	//g_event.triggerEvent();
	
    var canvas = document.createElement("canvas");
    canvas.width = getSizes().SceneWidth;
    canvas.height = getSizes().SceneHeight;
    document.body.appendChild(canvas);

var tt =	getStyleValue("zui.css",".modal-dialog","width");

	g_game = new Game();
	g_game.init(canvas);
}

var g_main = new Main();
g_main.init();
