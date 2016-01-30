// JavaScript Document
Login = function(){
    this.name = "login";
    this.count = 0;
    this.syncDuration = 5;
    this.data = {};
    this.loginPlayerid = -1;
    this.imgLoaded = 0;
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
		tdata = store.get(this.name);
	} 
	
	g_playerid = -1;
	for (var i=0;i<tdata.length;i++){
	  var data = tdata[i];
	  if (data.openid==g_openid){
	   g_username = data.playername;
	   g_playerid = data.playerid;
 		this.sex = data.sex;
 		break;
	  }
	}	

	//已有用户自动登录:
	if (g_username!=null){
	   this.login();
	}else
      this.draw();
}

Login.prototype.loadImgCallback = function()
{
	this.imgLoaded++;
	g_loading.add(5);
	if (this.imgLoaded>=login_imgs.length){
	 g_loading.set(RES_FINISH);
	 g_game.m_scene.m_map.draw();
	}
}

Login.prototype.draw = function()
{
	var localdata = store.get(this.name);
	var tdata = null;
	for (var i=0;i<localdata.length;i++){
	  var data = localdata[i];
	  if (data.openid==g_openid){
	   tdata = data;
 		break;
	  }
	}	
	
	var imgs = login_imgs;
   	if (tdata!=null){
 		this.sex = tdata.sex;
   		var choseImg = this.choseBorderImg(tdata.sex);
   		imgs.push(choseImg);
   		this.loginPlayerid = tdata.playerid;
   	}
	for (var i=0;i<imgs.length;i++){
		var img = imgs[i];	    		
   	 	g_game.addImg(img,imgs.length,"g_login.drawCallback");
	   	 if (img.name=="inputnick"){
		    var div = document.createElement("div");
		    div.id = "inputnickdiv";
		    var nickName = Login_InputDft;
		    if (tdata&&tdata.playername){
		    	nickName = tdata.playername;
		    }else if (g_username)
		    	nickName = g_username;
		    	
		   // nickName = Date.parse(new Date());
		    var title = "<span>昵称:</span>"
		    var input = "<input type='text' id='inputnick' value='"+nickName+"' class='cflogin_input' onfocus='g_login.clearInput()'>";
		    div.innerHTML = title+input;
		    div.className = "cflogin_input_div";
		    this.div = div;
	   	 }
   	}   	
}

Login.prototype.drawCallback = function()
{
    document.body.appendChild(this.div);   
    var div = document.createElement("div");
    div.id = "errmsg";
    div.className = "cflogin_tip";
    document.body.appendChild(div);   
    this.div = null;  	
}

Login.prototype.clearInput = function()
{
	var tag = document.getElementById("inputnick");
	if (tag.value==Login_InputDft)
		tag.value = "";
}

Login.prototype.choseBorderImg = function(sex)
{
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
	return img22;
}

Login.prototype.drawChoseBorder = function(sex)
{
	this.sex = sex;
	
	var img22 = this.choseBorderImg(sex);
	var img = g_game.m_scene.m_map.findImg(img22.name);
	if (img){
		img.x = img22.x;
		img.y = img22.y;
		g_game.m_scene.m_map.draw();
	}else {
		g_game.addImg2(img22);
	}
}

Login.prototype.msg = function(msg)
{
	var tag = document.getElementById("errmsg");
	if (tag==null){
      var div = document.createElement("div");
      div.id = "errmsg";
      div.className = "cflogin_tip";
      document.body.appendChild(div); 	
      tag = document.getElementById("errmsg");
	}
	tag.innerHTML = msg;
}

Login.prototype.onImgClick = function(image)
{
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
		this.login();
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

Login.prototype.syncLoadData = function(playerid){
	try    {
    	g_login.loadCount = 0;
	
		var dataParam = "playerid="+playerid+"&type=1";
		$.ajax({type:"post",url:"/cf/login_load.jsp",data:dataParam,success:function(data){
		 g_login.syncLoadDataCallback_saving(data);
		}});
    	g_login.loadCount++;
		
		dataParam = "playerid="+playerid+"&type=2";
		$.ajax({type:"post",url:"/cf/login_load.jsp",data:dataParam,success:function(data){
		 g_login.syncLoadDataCallback_insure(data);
		}});
    	g_login.loadCount++;
		
		dataParam = "playerid="+playerid+"&type=3";
		$.ajax({type:"post",url:"/cf/login_load.jsp",data:dataParam,success:function(data){
		 g_login.syncLoadDataCallback_stock(data);
		}});
    	g_login.loadCount++;
		
		dataParam = "playerid="+playerid+"&type=4";
		$.ajax({type:"post",url:"/cf/login_load.jsp",data:dataParam,success:function(data){
		 g_login.syncLoadDataCallback_top(data);
		}});
    	g_login.loadCount++;
				
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataParam);
	   return false;
	}
}

Login.prototype.syncLoadDataCallback_saving = function(data){
    var msg = [];
	var sdata = cfeval(data);
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
	g_bank.onEnter();
	g_login.msgtip(msg);
	g_login.callbackDone();
	
}

Login.prototype.syncLoadDataCallback_insure = function(data){
	var msg = [];
	var idata = cfeval(data);
	for (itemid in idata){
		var item = store.get(g_insure.name)[itemid];
		if (!item) continue;
		
		if (idata[itemid].profit==0)
		  g_player.insure[itemid]= idata[itemid];
		 else { //保险产品到期
			msg.push({type:g_insure.name,t:parseInt(item.type),name:item.name,profit:parseInt(idata[itemid].profit)});
		 }
	}
	g_login.msgtip(msg);
	g_login.callbackDone();
}

Login.prototype.syncLoadDataCallback_stock = function(data){
	var sdata = cfeval(data);
    g_player.stock = sdata;
	
	g_player.setStockIds();
	
	g_login.callbackDone();
}

Login.prototype.syncLoadDataCallback_top = function(data){
 g_player.data.weektop = parseInt(data);
 g_login.callbackDone();
}

Login.prototype.callbackDone = function(){
 g_login.loadCount--;
 if (g_login.loadCount<=0)
  g_player.flushPageview();

}

Login.prototype.loginCallback = function(obj){
	var objdata = cfeval(obj);
	if (objdata.code!=null&&objdata.code>0){
	 this.msg('登陆失败: '+ERR_MSG[objdata.code]);
	 g_username = null;
	 return;
	}
		
    var player = objdata;
	
	//进入场景:先remove login页面元素
	$('#inputnickdiv').remove();
	$('#errmsg').remove();
    //head img:
    var head_img = head_imgs[player.sex];
    head_img.name = player.playername;
	g_game.enter(head_img);
	
	var logindata = store.get(this.name);
	var lastPlayer = logindata[logindata.length-1];
	if (lastPlayer==null||lastPlayer.playerid!=player.playerid){
	 logindata.push(player);
	 store.set(g_login.name,logindata);
	 }
	 	
	 store.set(g_player.name,player);
	 
    g_player.loginback(player);
    
    g_playerlog.addlog();
    
//    var istoday = false;
//    if (player.questDoneTime!=null){
//      var donetime = new Date(player.questDoneTime);
//      istoday = IsSameDay(new Date(),donetime);
//    }
//    
//     g_quest.reset(player.questdonecount);
//    if (!istoday)
//	{
//		var donecount = player.questdonecount;
//		if (donecount==2)
//			donecount = 0;
//        var accept = g_quest.onAcceptDaily(donecount);
//        if (accept)
//        {
//        	g_playerlog.updateQuest();
//        }
//    }
    
    this.syncLoadData(player.playerid);
    
    //player props
   // g_player.flushPageview();
    	   
    var hadTodaySignin = false;
    if (player.lastlogin!=null){
      var logintime = new Date(player.lastlogin);
      hadTodaySignin = IsSameDay(new Date(),logintime);
    }
       
	if (!hadTodaySignin)
	{
		g_signin.show();
	}else {
		g_game.onEnter();
	}
	
	//g_event.triggerEvent();
}

Login.prototype.login = function(){
	g_msg.showload("g_login.login");
	
	if (g_username==null){
      var tag = document.getElementById("inputnick");
	  g_username = tag.value;	
	}
//     var player = store.get(g_player.name);
//     if (player==null){
//     	alert("本地数据缺失，登录失败");
//     	return;
//     }
	var setting = "{android:"+browser.versions.android+",iphone:"+browser.versions.iPhone;
	setting += ",iPhone5:"+browser.versions.iPhone5;
	setting += ",iPad:"+browser.versions.iPad;
	setting += ",width:"+window.screen.width;
	setting += ",screekey:"+g_screenkey+"}";
   	var dataParam = "openid="+g_openid+"&playername="+g_username+"&sex="+g_login.sex+"&playerid="+g_playerid+"&setting="+setting;
    var serverPlayer;
    var now = new Date();
    
	try    {
		$.ajax({type:"post",url:"/cf/login_login.jsp",data:dataParam,success:function(data){
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