var SiteUrl = "http://202.69.27.238:8081/cf"

var Share_Img = SiteUrl+"/static/img/app_icon_share.png"

var Share_Url = SiteUrl+"/dl.html"

var Dl_Url_ios = "http://itunes.apple.com/cn/app/id549421060"

var Dl_Url_android = "http://dd.myapp.com/16891/32B5098A37D290206587504942EBB30D.apk?fsname=com.pingan.lifeinsurance_2.5.6.2_52.apk&asr=8eff"

var g_game;

var g_username = null;

var g_openid = null;

var ZAN_COUNT = 10;	//每天点赞次数限制

var LoginMode = 0;	//1为注册模式，0为登陆模式

var UpdateDuration = 200;	//刷帧频率(ms)

var MsgDuration = 100;	//消息刷帧频率(ms)

var StockDuration = 1000;	//股票刷帧频率(ms)

var NetReqWait = 20000;	//网络请求等待时间(ms)

var QUOTETIME = 300;		//行情跳动时间(秒)

var FirstEventTriggerTime = 60*5;	//注册后首次意外事件触发事件,

var EventTriggerTime = 60*5;	//随机事件跳动;*UpdateDuration/1000 秒

var Panel_ClickColor = "#123123";

var Is_InBrowser = true;

var Share_Prize = 500

var Share_PageText = "又开始了新的一天！是否分享您今日的心情给您的好朋友们?"

var Share_PageText2 = "成功分享将获得<span style='color:red'>"+Share_Prize+"元</span>游戏币奖励"

var Share_Title = "我正在使用平安人寿APP玩<<财富人生>>游戏，快跟我一起来吧，更多惊喜和活动等着您:"+Share_Url

var Share_Text = "财富人生"

var Share_PageText_Prize = "获得<img class='cficon_money' src='static/img/money.png'/><span style='color:red'>"+Share_Prize+"</span>"

//var Share_Url = "http://elife.pingan.com/"

var Login_InputDft = "输入昵称";

var login_imgs = [
	{name:"map",src:"static/img/login_bg.png",x:0,y:0,zindex:0},
	{name:"inputnick",src:"static/img/nick_input.png",x:130,y:110,zindex:0},
	{name:"choseboy",src:"static/img/player_head_bg.png",x:150,y:380,zindex:0},
	{name:"chosegirl",src:"static/img/player_head_bg.png",x:400,y:380,zindex:0},
	{name:"choseboy",src:"static/img/icon_boy.png",x:150,y:380,zindex:0},
	{name:"chosegirl",src:"static/img/icon_girl.png",x:400,y:380,zindex:0},
	{name:"wchoseboy",src:"static/img/bt_boy.png",x:130,y:510,zindex:0},
	{name:"wchosegirl",src:"static/img/bt_girl.png",x:390,y:510,zindex:0},
	{name:"btstart",src:"static/img/bt_start.png",x:205,y:590,zindex:0},
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
var MSG_PlayerNotExist = 5;
var MSG_WrongPlayerNameOrPwd = 6;
var MSG_StockIsClosed = 7;
var MSG_PlayerNameIsExist = 8;
var MSG_SavingNotExist = 9;
var MSG_SavingIsExist = 10;
var MSG_InsureNotExist = 11;
var MSG_InsureIsExist = 12;
var MSG_StockNotExist = 13;
var MSG_StockQtyIsZero = 14;

var ERR_MSG ={};
ERR_MSG[MSG_SQLExecuteError] = "sql出错";
ERR_MSG[MSG_MoneyNotEnough] = "您的钱不够";
ERR_MSG[MSG_NoThisStock] = "这个股票不存在";
ERR_MSG[MSG_NoSavingData] = "没有该存款";
ERR_MSG[MSG_PlayerNotExist] = "玩家不存在";
ERR_MSG[MSG_WrongPlayerNameOrPwd] = "用户名或密码不正确";
ERR_MSG[MSG_StockIsClosed] = "股市已关闭,不能买卖";
ERR_MSG[MSG_PlayerNameIsExist] = "该昵称已存在";
ERR_MSG[MSG_SavingNotExist] = "你没有该存款，不能取出";
ERR_MSG[MSG_SavingIsExist] = "已有该存款，不能重复存入";
ERR_MSG[MSG_InsureNotExist] = "你没有该保险，不能删除";
ERR_MSG[MSG_InsureIsExist] = "已有该保险，不能重复购买";
ERR_MSG[MSG_StockNotExist] = "找不到该股票，无法购买";
ERR_MSG[MSG_StockQtyIsZero] = "请选择股票手数";

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
	"640":{SceneWidth:640,SceneHeight:1008,PieWidth:350,PieFontSize:30,PieFontSize2:19,
	PieHeight:400,PageWidth:"580px",PageHeight:"237px",PageTop:80,
	DetailPageTop:100,MsgTop:120,
	StockView:[550,310,450,210,22]
	,EventMoney:["70px","410px"]
	,SigninMoney:["60px","20px","210px"]
	,QuestMoney:["120px","390px"]
	},
	
	"600":{SceneWidth:640,SceneHeight:1008,PieWidth:320,PieFontSize:25,PieFontSize2:17,
	PieHeight:350,PageWidth:"532px",PageHeight:"277px",PageTop:75,
	DetailPageTop:95,MsgTop:114,
	StockView:[495,300,410,195,20]
	,EventMoney:["70px","380px"]
	,SigninMoney:["55px","25px","185px"]
	,QuestMoney:["120px","390px"]
	},
	
	"560":{SceneWidth:640,SceneHeight:1008,PieWidth:300,PieFontSize:25,PieFontSize2:17,
	PieHeight:326,PageWidth:"499px",PageHeight:"277px",PageTop:70,
	DetailPageTop:90,MsgTop:110,
	StockView:[468,284,384,187,20]
	,EventMoney:["70px","360px"]
	,SigninMoney:["53px","30px","167px"]
	,QuestMoney:["120px","390px"]
	},
	
	"540":{SceneWidth:640,SceneHeight:1008,PieWidth:290,PieFontSize:23,PieFontSize2:15,
	PieHeight:300,PageWidth:"482px",PageHeight:"227px",PageTop:70,
	DetailPageTop:90,MsgTop:108,
	StockView:[450,280,380,200,19]
	,EventMoney:["70px","350px"]
	,SigninMoney:["50px","30px","150px"]
	,QuestMoney:["120px","390px"]
	},
	
	"520":{SceneWidth:640,SceneHeight:1008,PieWidth:280,PieFontSize:24,PieFontSize2:17,
	PieHeight:303,PageWidth:"465px",PageHeight:"277px",PageTop:65,
	DetailPageTop:85,MsgTop:105,
	StockView:[438,267,363,178,19]
	,EventMoney:["70px","340px"]
	,SigninMoney:["52px","35px","148px"]
	,QuestMoney:["120px","390px"]
	},
	
	"480":{SceneWidth:640,SceneHeight:1008,PieWidth:260,PieFontSize:24,PieFontSize2:14,
	PieHeight:280,PageWidth:"432px",PageHeight:"277px",PageTop:60,
	DetailPageTop:80,MsgTop:100,
	StockView:[410,250,340,170,18]
	,EventMoney:["70px","320px"]
	,SigninMoney:["50px","36px","130px"]
	,QuestMoney:["120px","390px"]
	},
	
	"440":{SceneWidth:640,SceneHeight:1008,PieWidth:240,PieFontSize:23,PieFontSize2:14,
	PieHeight:260,PageWidth:"392px",PageHeight:"247px",PageTop:53,
	DetailPageTop:70,MsgTop:84,
	StockView:[375,247,305,163,17]
	,EventMoney:["70px","286px"]
	,SigninMoney:["50px","38px","123px"]
	,QuestMoney:["120px","390px"]
	},
	
	"400":{SceneWidth:640,SceneHeight:1008,PieWidth:220,PieFontSize:22,PieFontSize2:13,
	PieHeight:240,PageWidth:"362px",PageHeight:"212px",PageTop:46,
	DetailPageTop:60,MsgTop:67,
	StockView:[335,243,265,157,16]
	,EventMoney:["70px","253px"]
	,SigninMoney:["50px","39px","117px"]
	,QuestMoney:["120px","390px"]
	},
	
	"360":{SceneWidth:640,SceneHeight:1008,PieWidth:200,PieFontSize:20,PieFontSize2:13,
	PieHeight:220,PageWidth:"328px",PageHeight:"177px",PageTop:40,
	DetailPageTop:50,MsgTop:50,
	StockView:[300,240,230,150,15]
	,EventMoney:["70px","220px"]
	,SigninMoney:["50px","40px","110px"]
	,QuestMoney:["120px","390px"]
	},

	"320":{SceneWidth:640,SceneHeight:1008,PieWidth:180,PieFontSize:16,PieFontSize2:12,
	PieHeight:190,PageWidth:"290px",PageHeight:"177px",PageTop:50,
	DetailPageTop:60,MsgTop:40,
	StockView:[270,220,210,120,14]
	,EventMoney:["70px","200px"]
	,SigninMoney:["40px","40px","100px"]
	,QuestMoney:["120px","390px"]
	},
}

var SCREENKEY = 640;
var SIZEPER = 1;

 var browser={
    versions:function(){ 
           var u = navigator.userAgent, app = navigator.appVersion; 
           //alert(u);
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
	g_versions = versions;
 	 var width = window.screen.width;
	if (versions.iPhone||versions.iPad){
		if (width<=320||width==375||width==414){
			metas = window.parent.document.getElementsByTagName("meta");
			for(i=0;i<metas.length;i++)
		     {
		      if (metas[i].getAttribute("name")=="viewport"){
		       //alert(metas[i].getAttribute("name"))
		       metas[i].setAttribute("content","width=320");
		       break;  
		      }
			}
			if (width<480&&width>=360)
			 SCREENKEY = 400;
			else
			 SCREENKEY = 320;
		 }else
			 SCREENKEY = 640;
		 
	}else{
	  if (width>=640)
		SCREENKEY = 640;
	  else if (width<640&&width>=600)
		SCREENKEY = 600;
	  else if (width<600&&width>=540)
		SCREENKEY = 540;
	  else if (width<540&&width>=480)
		SCREENKEY = 480;
	  else if (width<480&&width>=420)
		SCREENKEY = 440;
	  else if (width<420&&width>=380)
		SCREENKEY = 400;
	  else if (width<380&&width>=340)
		SCREENKEY = 360;
	  else
	    SCREENKEY = 320;
	}
	 //  SCREENKEY = 320;	  
//
//alert(browser.versions.iPhone)
	////--note:393,SCREENKEY = 640;
	//--6s:414, 正常, screekey = 640;
//alert(width);
//alert(SCREENKEY);
//SCREENKEY = 640;
	g_screenkey = SCREENKEY;
	SIZEPER = SCREENKEY/640;
	var cssFile = "static/css/cf"+SCREENKEY+".css";
	loadStyle(cssFile);		
}

initScreen();

getSizes = function(){	
	return PageSizes[SCREENKEY];
}

function sc(){
var s = "<div style='color:red'>"; 
s += " 网页可见区域宽："+ document.body.clientWidth+"<br />"; 
s += " 网页可见区域高："+ document.body.clientHeight+"<br />"; 
s += " 网页可见区域宽："+ document.body.offsetWidth + " (包括边线和滚动条的宽)"+"<br />"; 
s += " 网页可见区域高："+ document.body.offsetHeight + " (包括边线的宽)"+"<br />"; 
s += " 网页正文全文宽："+ document.body.scrollWidth+"<br />"; 
s += " 网页正文全文高："+ document.body.scrollHeight+"<br />"; 
s += " 网页被卷去的高(ff)："+ document.body.scrollTop+"<br />"; 
s += " 网页被卷去的高(ie)："+ document.documentElement.scrollTop+"<br />"; 
s += " 网页被卷去的左："+ document.body.scrollLeft+"<br />"; 
s += " 网页正文部分上："+ window.screenTop+"<br />"; 
s += " 网页正文部分左："+ window.screenLeft+"<br />"; 
s += " 屏幕分辨率的高："+ window.screen.height+"<br />"; 
s += " 屏幕分辨率的宽："+ window.screen.width+"<br />"; 
s += " 屏幕可用工作区高度："+ window.screen.availHeight+"<br />"; 
s += " 屏幕可用工作区宽度："+ window.screen.availWidth+"<br />"; 
s += " 您的屏幕设置是 "+ window.screen.colorDepth +" 位彩色"+"<br />"; 
s += " 您的屏幕设置 "+ window.screen.deviceXDPI +" 像素/英寸"+"<br />"; 
 s += "</div>"
document.writeln(s);
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

//sc();
//aa();
