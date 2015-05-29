// JavaScript Document
Login = function(){
    this.name = "login";
    this.count = 0;
    this.syncDuration = 5;
    this.data = {};
    this.loginPlayerid = -1;
    this.isLogin = false;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.pageheader = this.tagname+"header";
}

Login.prototype = new Datamgr();

Login.prototype.init = function(){
    var isRe = false;
    var ret = true;
  	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,[]);
	} 
    this.draw();
}

Login.prototype.draw = function()
{
	var localdata = store.get(this.name);
	var tdata = localdata[localdata.length-1];
	for (var i=0;i<login_imgs.length;i++){
		var img = login_imgs[i];
   	 	g_game.addImg(img);
	   	 if (img.name=="inputnick"){
		    var div = document.createElement("div");
		    div.id = "inputnickdiv";
		    var nickName = tdata?tdata.playername:Login_InputDft;
		   // nickName = Date.parse(new Date());
		    var title = "<span>昵称:</span>"
		    var input = "<input type='text' id='inputnick' value='"+nickName+"' class='cflogin_input' onfocus='g_login.clearInput()'>";
		    div.innerHTML = title+input;
		    div.className = "cflogin_input_div";
		    document.body.appendChild(div);   
		    	 
	   	 }
   	}
		    var div = document.createElement("div");
		    div.id = "errmsg";
		    div.className = "cflogin_tip";
		    document.body.appendChild(div);     	
   	if (tdata!=null){
   		this.drawChoseBorder(tdata.sex,100,100);
   		this.loginPlayerid = tdata.playerid;
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
//	if (image.name=="choseboy")
//	oneKeyShareContentClickHandler();
//	else if (image.name=="chosegirl")
//	shareContentClickHandler();//showShareViewClickHandler();
//	else
//	showShareMenuClickHandler();	
//	return;

	var tdata = store.get(g_player.name);
	var tag = document.getElementById("inputnick");
	if (image.name=="chosegirl"||image.name=="choseboy"){
		if (tdata!=null&&tdata.playername==tag.value)
			return;
		
		var sex = 0;
		if (image.name=="chosegirl") sex = 1;
		
		this.drawChoseBorder(sex);
		
	}else if (image.name=="btstart"){
		if (tag.value==null||tag.value==""||tag.value==Login_InputDft){
			this.msg("输入您的昵称");
			return;		
		}
		if (this.sex==null){
			this.msg("选择您的性别");
			return;
		}
//		if (tdata!=null&&tdata.playername!=tag.value)
//			this.register();
//		else{
			this.login();
//		}
	}
}

Login.prototype.registerCallback = function(data){
	var obj = cfeval(data);
	if (obj.code!=null&&obj.code>0){
	 this.msg('注册失败: '+ERR_MSG[obj.code]);
	 return;
	}
	
	if (obj.pwd==null){
	 return;
	}
	
	var player = obj;	
	player.quest = [];
	player.createtime = Date.parse(new Date());
	
	var logindata = store.get(this.name);
	var lastPlayer = logindata[logindata.length-1];
	if (lastPlayer==null||lastPlayer.playerid!=player.playerid)
	 logindata.push(player);
	store.set(g_player.name,player);
	
	this.loginCallback(data);
}

Login.prototype.register = function(){
	g_msg.showload("g_login.register");

	var tag = document.getElementById("inputnick");
	var ppname = tag.value;
	var accountid = 11;
           
    var dataParam = "playername="+ppname+"&accountid="+accountid+"&sex="+g_login.sex;
    
	dataParam = "player.playername="+ppname+"&player.accountid="+accountid+"&player.sex="+g_login.sex;
	try    {
		$.ajax({type:"post",url:"/cf/login_register.do",data:dataParam,success:function(data){
		 g_login.registerCallback(data);
         g_msg.destroyload();
		}});
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataParam);
	   return false;
	}	
}

Login.prototype.loadInsureData = function(indata){
	g_player.insure = {};
	var msg = [];
	var idata = cfeval(indata);
	for (itemid in idata){
		var item = store.get(g_insure.name)[itemid];
		if (!item) continue;
		
		if (idata[itemid].profit==0)
		  g_player.insure[itemid]= idata[itemid];
		 else { //保险产品到期
			msg.push({type:g_insure.name,t:parseInt(item.type),name:item.name,profit:parseInt(idata[itemid].profit)});
		 }
	}
	return msg;
}

Login.prototype.loadData = function(obj){
	g_player.data = obj;
	g_player.data.quest = cfeval(obj.quest);
	g_player.saving = {};
	var msg = [];
	var sdata = cfeval(obj.saving);
	for (itemid in sdata){
		var item = store.get(g_saving.name)[itemid];
		if (!item) continue;
		if (item.type==0||sdata[itemid].profit<=0)
			g_player.saving[itemid] = sdata[itemid];
		//存款利息提示:
		if (sdata[itemid].profit>0){
			msg.push({type:g_saving.name,t:parseInt(item.type),name:item.name,profit:parseInt(sdata[itemid].profit)});
		}
	}
	var imsg = this.loadInsureData(obj.insure);
	for (var i=0;i<imsg.length;i++)
	{
	 msg.push(imsg[i]);
	}
	g_player.stock = {};
	if (obj.stock.length>0)
	 g_player.stock = cfeval(obj.stock);
	
	g_player.setStockIds();
	
	return msg;
}

Login.prototype.msgtip = function(loginMsg){
	for (var i=0;i<loginMsg.length;i++){
		var msg = loginMsg[i];
		if (msg.type==g_saving.name){
			var ppf = parseInt(msg.profit);
			if (msg.t==0)
				g_msg.tip("获得"+msg.name+"利息:"+ppf);
			else
				g_msg.tip("您的"+msg.name+"存款到期, 获得利息:"+ppf);
		}else if (msg.type==g_insure.name){
			if (msg.profit==-1){
				g_msg.tip("您的"+msg.name+"已到期");
			}else
				g_msg.tip("您的"+msg.name+"已到期,获得收益:"+parseInt(msg.profit));
		}
	}
}

Login.prototype.loginCallback = function(obj){
	//进入场景:
	g_game.m_scene.m_map.enter();
	
	$('#inputnickdiv').remove();
	$('#errmsg').remove();
	

	var objdata = cfeval(obj);
    var player = objdata;
	
	var logindata = store.get(this.name);
	var lastPlayer = logindata[logindata.length-1];
	if (lastPlayer==null||lastPlayer.playerid!=player.playerid){
	 logindata.push(player);
	 store.set(this.name,logindata);
	 }
	 	
	 store.set(g_player.name,player);
	 
     var loginMsg = this.loadData(objdata);
	this.msgtip(loginMsg);
	
	for (var i=0;i<loginMsg.length;i++){
		var msg = loginMsg[i];
		if (msg.type==g_saving.name){
			var ppf = parseInt(msg.profit);
			if (msg.t==0)
				g_msg.tip("获得"+msg.name+"利息:"+ppf);
			else
				g_msg.tip("您的"+msg.name+"存款到期, 获得利息:"+ppf);
		}else if (msg.type==g_insure.name){
			if (msg.profit==-1){
				g_msg.tip("您的"+msg.name+"已到期");
			}else
				g_msg.tip("您的"+msg.name+"已到期,获得收益:"+parseInt(msg.profit));
		}
	}
	
    //head img:
    var head_img = head_imgs[player.sex];
    head_img.name = player.playername;
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
		g_signin.show();
	}else {
		g_game.onEnter();
	}
	
	//g_event.triggerEvent();
}

Login.prototype.login = function(){
	g_msg.showload("g_login.login");
	
	var tag = document.getElementById("inputnick");
	var pname = tag.value;	
//     var player = store.get(g_player.name);
//     if (player==null){
//     	alert("本地数据缺失，登录失败");
//     	return;
//     }
     
     var ppobj = {playername:pname,sex:g_login.sex};
   	var dataParam = obj2ParamStr("player",ppobj);
    var serverPlayer;
    var now = new Date();
    
	try    {
		$.ajax({type:"post",url:"/cf/login_login.do",data:dataParam,success:function(data){
		 g_login.loginCallback(data);
         g_msg.destroyload();
		}});
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataParam);
	   return false;
	}	

	
   return true;
}

var g_login = new Login();