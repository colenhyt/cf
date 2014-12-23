// JavaScript Document
Login = function(){
    this.name = "login";
    this.count = 0;
    this.syncDuration = 5;
    this.data = {};
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
       this.pageheader = this.tagname+"header";
}

Login.prototype = new Datamgr();

Login.prototype.init = function(){
    var isRe = false;
    var ret = true;
    this.draw();
//	var tdata = store.get(g_player.name);
//	if (tdata==null)
//	{
//		ret = this.register();
//		isRe = true;
//	}   
//	if (ret==true) 
//     this.login(isRe); 
}

Login.prototype.draw = function()
{
    g_game.addImg(login_imgs[0]);
	
	
}

Login.prototype.register = function(){
    var createtime = Date.parse(new Date());
     var player = {
        "accountid":1,"playerid":-1,"playername":"player"+createtime,"exp":0,
		quest:[],sex:0,createtime:createtime
        };
           
    var dataParam = obj2ParamStr("player",player);
	var dataobj = $.ajax({type:"post",url:"/cf/login_register.do",data:dataParam,async:false});
	var retcode = 0;
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = eval ("(" + dataobj.responseText + ")");
			if (obj!=null){
			//error:
				if (obj.code!=null){
					retcode = obj.code;
				}else if (obj.pwd!=null){
					player.playerid = obj.playerid;
					player.pwd = obj.pwd;
				}
			}
		}
	}   catch  (e)   {
	    document.write(e.name  +   " :  "   +  dataobj.responseText);
	    return false;
	}	
	if (retcode>0){
		g_msg.open('注册失败: '+retcode);
		return;
	}
	
	store.set(g_player.name,player);
	
	return true;
}

Login.prototype.login = function(isRegister){
     var player = store.get(g_player.name);
     g_player.data = player;
   	var dataParam = obj2ParamStr("player",player);
    var serverPlayer;
	var dataobj = $.ajax({type:"post",url:"/cf/login_login.do",data:dataParam,async:false});
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = eval ("(" + dataobj.responseText + ")");
			if (obj!=null){
				if (obj.quest)
					g_player.data.quest = eval ("(" + obj.quest + ")");
				if (obj.saving)
					g_player.saving = eval ("(" + obj.saving + ")");
				if (g_player.stock)
					this.stock = eval ("(" + obj.stock + ")");
				if (obj.insure)
					g_player.insure = eval ("(" + obj.insure + ")");
			}
		}
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   // return false;
	}	
	  
    //head img:
    var head_img = head_imgs[0];
    if (head_imgs.length>player.sex)
        head_img = head_imgs[player.sex];
    head_img.name = g_player.name;
    g_game.addImg(head_img);
    
    g_playerlog.addlog();
    
 	var dlog = g_playerlog.findTodayLog();	   
    if (dlog[0]==true)
	{
	        var accept = g_quest.onAcceptDaily();
	        if (accept)
	        {
	        	g_playerlog.updateQuest();
	        }
    }
    
    //player props
    g_player.flushPageview();
    	   
	if (dlog[1]==true)
	{
		g_signin.start(0);
	}
   return true;
}

var g_login = new Login();