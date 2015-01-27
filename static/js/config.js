var g_game;

var UpdateDuration = 200;	//刷帧频率(ms)

var MsgDuration = 100;	//消息刷帧频率(ms)

var StockDuration = 1000;	//股票刷帧频率(ms)

var NetReqWait = 5000;	//网络请求等待时间(ms)

var QUOTETIME = 300;		//行情跳动时间(秒)

var EventTriggerTime = 5*120;	//随机事件跳动;*UpdateDuration/1000 秒

var Panel_ClickColor = "#123123";

var Login_InputDft = "输入你的昵称";

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
	{name:"insure",src:"static/img/icon_insure.png",x:342,y:622,hasDiv:true,zindex:100,},
	{name:"saving",src:"static/img/icon_saving.png",x:150,y:15,hasDiv:true},
	{name:"weektop",src:"static/img/icon_weektop.png",x:365,y:15,hasDiv:true},
	{name:"level",src:"static/img/icon_level.png",x:-5,y:103,hasDiv:true},
];

var head_imgs = [
	{name:"man",src:"static/img/icon_boy.png",x:0,y:0,abs:true,hasDiv:true,divX:160,divY:10,zindex:10},
	{name:"women",src:"static/img/icon_girl.png",x:0,y:0,abs:true,hasDiv:true,divX:160,divY:10,zindex:10},
]

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
	BUY_INSURE:0,BUY_FINAN:1,BUY_STOCK:2,SELL_STOCK:3,SAVING:4
}

var QUEST_STATUS = {
	ACTIVE:0,DONE:1,FINISH:2
}

var ITEM_TYPE = {
	CASH : 0,EXP:1
};

var ITEM_NAME = {
	CASH : "金钱",EXP:"经验"
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

function loadStyle(url){
    var link = document.createElement('link');
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = url;
    var head = document.getElementsByTagName("head")[0];
    head.appendChild(link);
}

var Page_Top = 80;
var PageDetail_Top = 100;

var Screen_Status_Height = 40;
var Screen_Nav_Height = 88;

var Scene_Height = 1236 - Screen_Status_Height - Screen_Nav_Height;

var PageSizes = {
	"640":{SceneWidth:640,SceneHeight:1008,PieWidth:350,
	PieHeight:400,PageWidth:"580px",PieFontSize:30,PieFontSize2:23,
	PageHeight:"637px",PageTop:80,
	DetailPageTop:100,MsgTop:120,
	StockView:[540,420,480,280,22]
	,EventMoney:["70px","410px"]
	,SigninMoney:["90px","50px","210px"]
	,QuestMoney:["120px","390px"]
	},
	
	"480":{SceneWidth:640,SceneHeight:993,PieWidth:260,PieFontSize:24,PieFontSize2:18,
	PieHeight:280,PageWidth:"432px",PageHeight:"477px",PageTop:60,
	DetailPageTop:80,MsgTop:100,
	StockView:[410,350,400,230,18]
	,EventMoney:["70px","320px"]
	,SigninMoney:["60px","50px","160px"]
	,QuestMoney:["120px","390px"]
	},
	
	"360":{SceneWidth:640,SceneHeight:992,PieWidth:200,PieFontSize:20,PieFontSize2:16,
	PieHeight:220,PageWidth:"328px",PageHeight:"477px",PageTop:40,
	DetailPageTop:50,MsgTop:50,
	StockView:[300,300,280,190,15]
	,EventMoney:["70px","410px"]
	,SigninMoney:["90px","50px","210px"]
	,QuestMoney:["120px","390px"]
	},

	"320":{SceneWidth:640,SceneHeight:1000,PieWidth:180,PieFontSize:16,PieFontSize2:15,
	PieHeight:190,PageWidth:"290px",PageHeight:"477px",PageTop:50,
	DetailPageTop:60,MsgTop:40,
	StockView:[270,220,260,120,15]
	,EventMoney:["70px","410px"]
	,SigninMoney:["90px","50px","210px"]
	,QuestMoney:["120px","390px"]
	},
}

var SCREENKEY = 640;
var SIZEPER = 1;

 var browser={
    versions:function(){ 
           var u = navigator.userAgent, app = navigator.appVersion; 
           return {//移动终端浏览器版本信息 
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
                iPhone5: u.indexOf('iPhone OS 6_1') > -1 , //os版本
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
            };
         }(),
         language:(navigator.browserLanguage || navigator.language).toLowerCase()
} 

function initScreen(){
//alert(window.screen.width)
	var versions = browser.versions
	if (versions.iPhone==true){
	metas = window.parent.document.getElementsByTagName("meta");
	for(i=0;i<metas.length;i++)
     {
      if (metas[i].getAttribute("name")=="viewport"){
       //alert(metas[i].getAttribute("name"))
       metas[i].setAttribute("content","width=320");
       break;  
      }
	}
	}
 	 var width = window.screen.width;
	 if (width>=640)
		SCREENKEY = 640;
	 else if (width<640&&width>=480)
		SCREENKEY = 480;
	 else if (width<480&&width>=360)
		SCREENKEY = 360;
	 else
	    SCREENKEY = 320;

	//SCREENKEY = 480;
	SIZEPER = SCREENKEY/640;
	var cssFile = "static/css/cf"+SCREENKEY+".css";
	loadStyle(cssFile);		
}

initScreen();

getSizes = function(){	
	return PageSizes[SCREENKEY];
}


function aa(){
document.writeln("语言版本: "+browser.language);
document.writeln(" 是否为移动终端: "+browser.versions.mobile);
document.writeln(" ios终端: "+browser.versions.ios);
document.writeln(" android终端: "+browser.versions.android);
document.writeln(" 是否为iPhone: "+browser.versions.iPhone);
document.writeln(" 是否为iPhone5: "+browser.versions.iPhone5);
document.writeln(" 是否iPad: "+browser.versions.iPad);
document.writeln(navigator.userAgent);
}
//aa();
