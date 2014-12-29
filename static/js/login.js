// JavaScript Document
Login = function(){
    this.name = "login";
    this.count = 0;
    this.syncDuration = 5;
    this.data = {};
    this.isLogin = false;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
       this.pageheader = this.tagname+"header";
}

Login.prototype = new Datamgr();

Login.prototype.init = function(){
    var isRe = false;
    var ret = true;
    this.draw();
}

Login.prototype.draw = function()
{
	var tdata = store.get(g_player.name);
	for (var i=0;i<login_imgs.length;i++){
		var img = login_imgs[i];
   	 	g_game.addImg(img);
	   	 if (img.name=="inputnick"){
		    var div = document.createElement("div");
		    div.id = "inputnickdiv";
		    var nickName = tdata?tdata.playername:Login_InputDft;
		    var title = "<span>昵称:</span>"
		    var input = "<input type='text' id='inputnick' value='"+nickName+"' class='cflogin_input' onfocus='g_login.clearInput()'>";
		    div.innerHTML = title+input;
		    div.style.position = "absolute";
		    div.style.left = (img.x+10) + "px";
		    div.style.top =  (img.y+10) + "px";
		    document.body.appendChild(div);   
		    	 
	   	 }
   	}
		    var div = document.createElement("div");
		    div.id = "errmsg";
		    div.style.position = "absolute";
		    div.style.color = "black";
		    div.style.left = "130px";
		    div.style.top =  "490px";
		    document.body.appendChild(div);     	
   	if (tdata!=null){
   		this.drawChoseBorder(tdata.sex,100,100);
   	}
}

Login.prototype.clearInput = function()
{
	var tag = document.getElementById("inputnick");
	if (tag.value==Login_InputDft)
		tag.value = "";
}

Login.prototype.drawChoseBorder = function(sex)
{
	this.sex = sex;
		
	var img;
	for (var i=0;i<login_imgs.length;i++){
		if (sex==0){
			if (login_imgs[i].name=="choseboy"){
				img = login_imgs[i];
				break;
			}
		}else if (login_imgs[i].name=="chosegirl"){
				img = login_imgs[i];
				break;
		}
	}
	var img22 = {name:"choseBorder",src:"static/img/bt_head_chose.png",x:img.x-24,y:img.y-24};
	var img = g_game.m_scene.m_map.findImg(img22.name);
	if (img){
		img.x = img22.x;
		img.y = img22.y;
		g_game.m_scene.m_map.draw();
	}else {
		g_game.addImg(img22);
	}
}

Login.prototype.msg = function(msg)
{
	var tag = document.getElementById("errmsg");
	tag.innerHTML = msg;
}

Login.prototype.onImgClick = function(image)
{
	var tdata = store.get(g_player.name);
	if (image.name=="chosegirl"||image.name=="choseboy"){
		if (tdata!=null)
			return;
		
		var sex = 0;
		if (image.name=="chosegirl") sex = 1;
		
		this.drawChoseBorder(sex);
		
	}else if (image.name=="btstart"){
		var tag = document.getElementById("inputnick");
		if (tag.value==null||tag.value==""||tag.value==Login_InputDft){
			this.msg("输入你的昵称");
			return;		
		}
		if (this.sex==null){
			this.msg("选择你的性别");
			return;
		}
		var canLogin = true;
		if (tdata==null)
			canLogin = this.register();
		if (canLogin){
			var tagerm = document.getElementById("inputnickdiv");
			document.body.removeChild(tagerm);
			tagerm = document.getElementById("errmsg");
			document.body.removeChild(tagerm);
			this.login();
		}
	}
}

Login.prototype.register = function(){
    var createtime = Date.parse(new Date());
	var tag = document.getElementById("inputnick");
	var ppname = "player"+createtime;
	ppname = tag.value;
     var player = {
        "accountid":1,"playerid":-1,"playername":ppname,"exp":0,
		quest:[],sex:this.sex,createtime:createtime
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
		this.msg('注册失败: '+ERR_MSG[retcode]);
		return false;
	}
	
	store.set(g_player.name,player);
	
	return true;
}

Login.prototype.login = function(){
	//进入场景:
	g_game.m_scene.m_map.enter();
	
     var player = store.get(g_player.name);
     g_player.data = player;
     var ppobj = {playerid:player.playerid,pwd:player.pwd,playername:player.playername}
   	var dataParam = obj2ParamStr("player",ppobj);
    var serverPlayer;
	var dataobj = $.ajax({type:"post",url:"/cf/login_login.do",data:dataParam,async:false});
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = eval ("(" + dataobj.responseText + ")");
			if (obj!=null&&obj.playerid!=null){
				g_player.data = obj;
				if (obj.quest)
					g_player.data.quest = eval ("(" + obj.quest + ")");
				if (obj.saving)
					g_player.saving = eval ("(" + obj.saving + ")");
				if (obj.stock)
					g_player.stock = eval ("(" + obj.stock + ")");
				if (obj.insure)
					g_player.insure = eval ("(" + obj.insure + ")");
				if (obj.stockdata)
					g_stock.load(eval ("(" + obj.stockdata + ")"));
			}else {
				logerr("数据错误: "+obj);
			}
		}
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   return false;
	}	
	
	for (var i=0;i<g_player.saving.length;i++){
		var profit = g_player.saving[i].profit;
		if (profit>0){
			g_msg.tip("获得收益:"+ForDight(profit));
		}
		//g_player.saving[i].interest = 0;
	}
	
    //head img:
    var head_img = head_imgs[player.sex];
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
	}else {
		g_insure.onEnter();
	}
	
   return true;
}

var g_login = new Login();