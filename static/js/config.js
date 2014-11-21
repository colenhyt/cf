var g_dbname = 'cfgame';
var g_dbversion = '1.0';
var g_dbfile = 'cfgame_file';
var g_dbsize = 200000;

var g_game;
var g_db;

var game_imgs = [
	{name:"map",src:"static/img/map.jpg",x:0,y:0,},
	{name:"toplist",src:"static/img/icon_toplist.jpg",x:90,y:220,},
	{name:"task",src:"static/img/icon_task.png",x:220,y:50,},
	{name:"player",src:"static/img/icon_player.png",x:0,y:0,abs:true,},
	{name:"insure",src:"static/img/icon_insure.png",x:30,y:80,},
];

var game_tables = {
    "toplist":"t_toplist",
    "task":"t_task",
    "player":"t_player",
}
