var g_game;
var g_db;

var CSS_FILE = "static/css/cf.css";

var Page_Top = 80;
var PageDetail_Top = 100;

var UpdateDuration = 200;	//刷帧频率(ms)

var MsgDuration = 200;	//消息刷帧频率(ms)

var QUOTETIME = 300;		//行情跳动时间(秒)

var EventTriggerTime = 30000;	//随机事件跳动;

var Panel_ClickColor = "#123123";

var Login_InputDft = "输入你的昵称";

var PlayerInfo_Pie_Width = 350;

var PlayerInfo_Pie_Height = 400;

var Screen_Status_Height = 40;
var Screen_Nav_Height = 88;

var Scene_Width = 640;
var Scene_Height = 1236 - Screen_Status_Height - Screen_Nav_Height;

var game_screen = {
	swidth:window.screen.width/Scene_Width,sheight:window.screen.height/Scene_Height
}

var login_imgs = [
	{name:"map",src:"static/img/login_bg.png",x:0,y:0,zindex:0},
	{name:"inputnick",src:"static/img/nick_input.png",x:130,y:400,zindex:0},
	{name:"choseboy",src:"static/img/player_head_bg.png",x:150,y:550,zindex:0},
	{name:"chosegirl",src:"static/img/player_head_bg.png",x:400,y:550,zindex:0},
	{name:"choseboy",src:"static/img/icon_boy.png",x:150,y:550,zindex:0},
	{name:"chosegirl",src:"static/img/icon_girl.png",x:400,y:550,zindex:0},
	{name:"wchoseboy",src:"static/img/bt_boy.png",x:130,y:680,zindex:0},
	{name:"wchosegirl",src:"static/img/bt_girl.png",x:390,y:680,zindex:0},
	{name:"btstart",src:"static/img/bt_start.png",x:180,y:80,zindex:0},
]

var game_imgs = [
	{name:"map",src:"static/img/map.png",x:0,y:0,zindex:0},
	{name:"quest",src:"static/img/icon_quest.png",x:555,y:7,hasDiv:true},
	{name:"playerinfo",src:"static/img/icon_head.png",x:0,y:0,hasDiv:true},
	{name:"toplist",src:"static/img/icon_toplist.png",x:555,y:120,hasDiv:true,zindex:100},
	{name:"help",src:"static/img/icon_help.png",x:170,y:280,hasDiv:true},
	{name:"stock",src:"static/img/icon_stock.png",x:385,y:207,hasDiv:true,zindex:100,},
	{name:"bank",src:"static/img/icon_bank.png",x:70,y:448,hasDiv:true,zindex:100,},
	{name:"insure",src:"static/img/icon_insure.png",x:342,y:622,hasDiv:true},
	{name:"saving",src:"static/img/icon_saving.png",x:150,y:15,hasDiv:true},
	{name:"weektop",src:"static/img/icon_weektop.png",x:365,y:15,hasDiv:true},
	{name:"level",src:"static/img/icon_level.png",x:-5,y:103,hasDiv:true},
	{name:"car1",src:"static/img/icon_car1.png",x:-45,y:338,hasDiv:true},
	{name:"car2",src:"static/img/icon_car2.png",x:625,y:243,hasDiv:true},
	{name:"car3",src:"static/img/icon_car3.png",x:585,y:843,hasDiv:true},
	{name:"car4",src:"static/img/icon_car2.png",x:585,y:603,hasDiv:true},
];

var cars_pos = {
"car1":[[25,363],[30,365],[35,367],[40,369],[45,371],[50,376],[55,378],[]],
"car2":[],
"car3":[],
};

var head_imgs = [
	{name:"man",src:"static/img/icon_boy.png",x:0,y:0,abs:true,hasDiv:true,divX:160,divY:10,zindex:10},
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
    {id:5,src:"static/img/feeling_5.png"},
    {id:6,src:"static/img/feeling_6.png"},
    {id:7,src:"static/img/feeling_7.png"},
    {id:8,src:"static/img/feeling_8.png"},
]

var QUEST_TYPE = {
	BUY_INSURE:0,BUY_STOCK:1,SELL_STOCK:2,
}

var QUEST_STATUS = {
	ACTIVE:0,DONE:1,
}

var ITEM_TYPE = {
	CASH : 0,EXP:1
};

var ITEM_NAME = {
	CASH : "工资",EXP:"经验"
};

var EVENT_TYPE = [
];

var MSG_OK = 0;
var MSG_SQLExecuteError = 1;
var MSG_MoneyNotEnough = 2;
var MSG_NoThisStock = 3;
var MSG_NoSavingData = 4;
var MSG_PlayerNameIsExist = 5;

var ERR_MSG ={};
ERR_MSG[MSG_SQLExecuteError] = "sql出错";
ERR_MSG[MSG_MoneyNotEnough] = "你的钱不够";
ERR_MSG[MSG_NoThisStock] = "这个股票不存在";
ERR_MSG[MSG_NoSavingData] = "没有该存款";
ERR_MSG[MSG_PlayerNameIsExist] = "这个昵称已经被使用";