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
	//outputCssFiles(480);
	//return;
	
    var canvas = document.createElement("canvas");
    canvas.width = getSizes().SceneWidth;
    canvas.height = getSizes().SceneHeight;
    document.body.appendChild(canvas);

var tt =	getStyleValue("zui.css",".modal-dialog","width");

//alert(screen.width);
		//var width = getCSSValue("cfpage","width");
		//g_msg.tip(width);

//    g_msg.tip("aaaaa","500px",-200);
//    g_msg.tip("bb","100px",100);
//    g_msg.tip("cc","100px",100);
//    g_msg.tip("dd","100px",100);
//    g_msg.tip("ee","100px",100);
//    g_msg.tip("ff","100px",100);
//g_uplevel.open();
//g_toplist.zan(1,1);

	g_game = new Game();
	g_game.init(canvas);
}

var g_main = new Main();
g_main.init();
