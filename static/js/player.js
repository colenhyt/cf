// JavaScript Document
Player = function(){
    this.name = "player";
}

Player.prototype = new Datamgr();

Player.prototype.init = function(){
    g_game.register(this);
    this.loadData();
}

Player.prototype.loadDataCallback = function(tx,results){
	if (results.rows.length==0){
	   g_player.register();	   
    }
}

Player.prototype.register = function(){
	//var dataobj = $.ajax({url:"../bbs/record.php",async:false});
	alert('ajax'+this.name);
}

var g_player = new Player();
g_player.init();
