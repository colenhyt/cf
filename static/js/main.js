function aaa(){
	log("aaa");
}

function aaa2(){
	log("aaa2");
}

Layer = function(){
}

Layer.prototype.init = function(){

}

Main = function(){
}

Main.prototype.init = function(){
    var canvas=document.getElementById('canvas');
	this.m_game = new Game('game');
	this.m_game.init(canvas);
}

var mm = new Main();
mm.init();