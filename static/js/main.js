function aaa(){
	log("aaa");
}

function aaa2(){
	log("aaa2");
}

Main = function(){
}

Main.prototype.init = function(){
    var canvas=document.getElementById('canvas');
	this.m_game = new Game('game');
	this.m_game.init(canvas);
}

var g_main = new Main();
g_main.init();
