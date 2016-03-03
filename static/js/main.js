function aaa(){
	log("aaa");
}

function aaa2(){
	//alert("aaa2");
}

Main = function(){
}

Main.prototype.init = function(){
//	$.getJSON("/cf/login_connect.do", function(json){
//		if (json.code==-1)
//			alert('联网失败，请检查你的网络');
//	})	
	
	//loadStyle('static/css/cf.css');
//outputCssFiles(800);
//return;
	
	//g_event.triggerEvent();
	 
	var openid = getPar("OPENID");
	//!isNumValue(tel)
	var reg = new RegExp("^[0-9]*$");
	if (!openid||openid.length<=0||!reg.test(openid)){
		var desc = "获取初始化参数失败，无法进入游戏!";
		if (openid)
		 desc += ",openid:"+openid;
		gameErr(desc);
		return;
	}
	
	g_pwd = getPar("pwd");
	
	g_openid = openid;
	
	g_toplist.init();
	g_playerinfo.init();
	g_playerlog.init();
	g_player.init();
	g_quest.init();
	g_signin.init();
	g_insure.init();
	g_stockdetail.init();
	g_stock.init();
	g_bank.init();
	g_saving.init();
	g_help.init();
	g_title.init();
	g_msg.init();
	g_uplevel.init();
	g_event.init();
	
    var canvas = document.createElement("canvas");
    canvas.width = getSizes().SceneWidth;
    canvas.height = getSizes().SceneHeight;
    document.body.appendChild(canvas);

	g_game = new Game();
	g_game.init(canvas);
}

var g_main = new Main();
g_main.init();    


