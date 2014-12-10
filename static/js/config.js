var g_dbname = 'cfgame';
var g_dbversion = '1.0';
var g_dbfile = 'cfgame_file';
var g_dbsize = 200000;

var g_game;
var g_db;

var Scene_Width = 640;
var Scene_Height = 1280;

var game_screen = {
	swidth:window.screen.width/Scene_Width,sheight:window.screen.height/Scene_Height
}

var game_imgs = [
	{name:"map",src:"static/img/map.png",x:0,y:0,zindex:0},
	{name:"cash_back",src:"static/img/icon_back.png",x:150,y:15,abs:true},
	{name:"card_back",src:"static/img/icon_back.png",x:340,y:15,abs:true},
	{name:"player",src:"static/img/icon_head.png",x:0,y:0,abs:true,hasDiv:true,divX:25,divY:115},
	{name:"cash",src:"static/img/icon_cash.png",x:135,y:5,abs:true,hasDiv:true,divX:205,divY:25,zindex:5},
	{name:"card",src:"static/img/icon_card.png",x:328,y:5,abs:true,hasDiv:true,divX:390,divY:25,zindex:5},
	{name:"quest",src:"static/img/icon_mail.png",x:550,y:15,abs:true,hasDiv:true},
	{name:"toplist",src:"static/img/icon_toplist.png",x:550,y:100},
	{name:"help",src:"static/img/icon_help.png",x:127,y:190,},
	{name:"stock",src:"static/img/icon_stock.png",x:375,y:260,},
	{name:"bank",src:"static/img/icon_bank.png",x:72,y:420,},
	{name:"insure",src:"static/img/icon_insure.png",x:377,y:575,},
	{name:"level",src:"static/img/icon_level.png",x:0,y:0,abs:true,hasDiv:true,divX:15,divY:8,zindex:20},
];

var head_imgs = [
	{name:"man",src:"static/img/icon_girl.png",x:0,y:0,abs:true,hasDiv:true,divX:160,divY:10,zindex:10},
	{name:"women",src:"static/img/icon_girl.png",x:0,y:0,abs:true,hasDiv:true,divX:160,divY:10,zindex:10},
]

var game_page_imgs = {
	"close.png":{width:77,height:69},
	"title_bg.png":{width:268,height:69},
	"title_signin.png":{width:185,height:41},
	"title_quest.png":{width:185,height:41},
	"title_insure.png":{width:185,height:41},
	"title_stock.png":{width:185,height:41},
	"title_bank.png":{width:185,height:41},
	"title_help.png":{width:185,height:41},
	"pop_big.png":{width:580,height:654},
}

var feeling_imgs = [
    {id:1,src:"static/img/feeling_1.png"},
    {id:2,src:"static/img/feeling_2.png"},
    {id:3,src:"static/img/feeling_3.png"},
    {id:4,src:"static/img/feeling_4.png"},
]

var QUEST_TYPE = {
	BUY_INSURE:0,BUY_STOCK:1,
}

var QUEST_STATUS = {
	ACTIVE:0,DONE:1,
}

var ITEM_TYPE = {
	CASH : 0,EXP:1
};

var ITEM_NAME = {
	CASH : "现金",EXP:"经验"
};

var EVENT_TYPE = [
];
