function aaa(){
	div1.style.left = "150px";
	log(div1.style.left);
	var tt=document.getElementById('b2222');
	tt.style.z-index = 30;
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

var mm = new Main();
mm.init();