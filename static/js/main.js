function aaa(){
	log("aaa");
}

function aaa2(){
	//alert("aaa2");
}

Main = function(){
}

Main.prototype.init = function(){
    var canvas=document.getElementById('canvas');
	g_game = new Game();
	g_game.init(canvas);
}
 
var g_main = new Main();
g_main.init();
