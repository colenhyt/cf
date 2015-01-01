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
	
    var canvas = document.createElement("canvas");
    canvas.width = Scene_Width;
    canvas.height = Scene_Height;
    document.body.appendChild(canvas);

	var meme = {};
//    g_msg.tip("aaaaa","500px",-200);
//    g_msg.tip("fdafda","100px",100);
//    g_msg.tip("fdafda","100px",100);
//    g_msg.tip("fdafda","100px",100);
//    g_msg.tip("fdafda","100px",100);
//    g_msg.tip("fdafda","100px",100);
	g_game = new Game();
	g_game.init(canvas);
}
 
var g_main = new Main();
g_main.init();
