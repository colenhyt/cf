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
	  // $('#myregister').modal(options);  
	   g_player.register_c(0);	   
    }else {
        var item = results.rows.item(0);
        g_player.login(item);
    }
}


Player.prototype.register_c = function(sex){
    var newDate = new Date();
    var createtime = newDate.toJSON();
    var item = {
        "accountid":1,"playerid":1,"playername":"pname","pwd":"pwd","cash":100,
        sex:sex,createtime:createtime
        };
        var row = [];
        row.push([
            {'name' : 'accountid','value' : item.accountid},
            {'name' : 'playerid','value' : item.playerid},
            {'name' : 'playername','value' : item.playername},
            {'name' : 'cash','value' : item.cash},
            {'name' : 'sex','value' : item.sex},
            {'name' : 'createtime','value' : item.createtime},
            {'name' : 'pwd','value' : item.pwd}
        ]);
 	
       var ret = g_db.insert(game_tables[this.name], row);	
       alert("register"+item.createtime+";sex:"+item.sex);
       g_player.login(item);              
}

Player.prototype.register = function(){
    var pname = document.getElementById("player.playername");
    var pheadicon = document.getElementById("player.headicon");
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

Player.prototype.login = function(item){
	//var dataobj = $.ajax({url:"/cf/login_login.do?player.playername="+playername+"&player.pwd="+pwd,async:false});
//	var obj = eval ("(" + dataobj.responseText + ")");
    alert("login "+item.createtime);
    var head_img = head_imgs[0];
    if (head_imgs.length>item.sex)
        head_img = head_imgs[item.sex];
    head_img.name = this.name;
    g_game.m_scene.m_map.addImg(head_img);
    var tagcash = document.getElementById("tagcash");
    tagcash.innerHTML = "<b>"+item.cash+"</b>";
}

var g_player = new Player();
g_player.init();
