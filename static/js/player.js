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
        var options = {
            keyboard : false,
            show     : true
        };	
	   $('#myregister').modal(options);  
	  // g_player.register();	   
    }else {
        var item = results.rows.item(0);
        g_player.login(item.playername,item.pwd);
    }
}


Player.prototype.register = function(){
    var pname = document.getElementById("player.playername");
    var ppwd = document.getElementById("player.pwd");
	var dataobj = $.ajax({url:"/cf/login_register.do?player.accountid=1&player.playername="+pname.value+"&player.pwd="+ppwd.value,async:false});
	var obj = eval ("(" + dataobj.responseText + ")");
	if (obj.playername!=null){
        var row = [];
        row.push([
            {'name' : 'accountid','value' : obj.accountid},
            {'name' : 'playerid','value' : obj.playerid},
            {'name' : 'playername','value' : obj.playername},
            {'name' : 'pwd','value' : obj.pwd},
            {'name' : 'version','value' : obj.version},
        ]);
 	
       var ret = g_db.insert(game_tables[this.name], row);	              
	}
}

Player.prototype.login = function(playername,pwd){
	var dataobj = $.ajax({url:"/cf/login_login.do?player.playername="+playername+"&player.pwd="+pwd,async:false});
//	var obj = eval ("(" + dataobj.responseText + ")");
//	alert("login "+obj);
}

var g_player = new Player();
g_player.init();
