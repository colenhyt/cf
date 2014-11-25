var g_dbname = 'cfgame';
var g_dbversion = '1.0';
var g_dbfile = 'cfgame_file';
var g_dbsize = 200000;

var g_game;
var g_db;

var game_imgs = [
	{name:"map",src:"static/img/map.jpg",x:0,y:0,},
//	{name:"player",src:"static/img/icon_player.png",x:0,y:0,abs:true,},
	{name:"cash",src:"static/img/icon_cash.png",x:130,y:0,abs:true,hasDiv:true,divX:160,divY:10},
	{name:"toplist",src:"static/img/icon_toplist.jpg",x:90,y:220,},
	{name:"task",src:"static/img/icon_task.png",x:240,y:80,},
	{name:"insure",src:"static/img/icon_insure.png",x:30,y:80,},
];

var head_imgs = [
	{name:"man",src:"static/img/icon_head_man.png",x:0,y:0,abs:true},
	{name:"women",src:"static/img/icon_head_women.png",x:0,y:0,abs:true},
]

var game_tables = {
    "toplist":"t_toplist",
    "task":"t_task",
    "player":"t_player",
    "signin":"t_signin",
};

var feeling_imgs = [
    {id:1,src:"static/img/feeling_1.png"},{id:2,src:"static/img/feeling_1.png"},{id:3,src:"static/img/feeling_1.png"},
    {id:4,src:"static/img/feeling_1.png"},{id:5,src:"static/img/feeling_1.png"},{id:6,src:"static/img/feeling_1.png"},
]
