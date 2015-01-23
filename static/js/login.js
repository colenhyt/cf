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
	tdata = store.get(g_player.name);
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
	var tdata = store.get(g_player.name);
	if (image.name=="chosegirl"||image.name=="choseboy"){
		if (tdata!=null&&tdata.playerid==this.loginPlayerid)
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
		if (tdata==null||tdata.playerid!=this.loginPlayerid)
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
	var ppname = tag.value;
     var player = {
        "accountid":1,"playerid":-1,"playername":ppname,"exp":0,
		quest:[],sex:this.sex,createtime:createtime
        };
           
    var dataParam = obj2ParamStr("player",player);
	var dataobj = $.ajax({type:"post",url:"/cf/login_register.do",data:dataParam,async:false});
	var retcode = 0;
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = cfeval(dataobj.responseText);
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
	var logindata = store.get(this.name);
	var lastPlayer = logindata[logindata.length-1];
	if (lastPlayer==null||lastPlayer.playerid!=player.playerid)
	 logindata.push(player);
	store.set(g_player.name,player);
	
	return true;
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
	g_player.insure = {};
	var idata = cfeval(obj.insure);
	for (itemid in idata){
		var item = store.get(g_insure.name)[itemid];
		if (!item) continue;
		
		if (idata[itemid].profit==0)
		  g_player.insure[itemid]= idata[itemid];
		 else { //保险产品到期
			msg.push({type:g_insure.name,t:parseInt(item.type),name:item.name,profit:parseInt(idata[itemid].profit)});
		 }
	}
	g_player.stock = cfeval(obj.stock);
	
	g_player.setStockIds();
	
	return msg;
}

Login.prototype.loginCallback = function(obj){
	//进入场景:
	g_game.m_scene.m_map.enter();
	
     var player = store.get(g_player.name);
     g_player.data = player;

     var loginMsg = this.loadData(cfeval(obj));

	for (var i=0;i<loginMsg.length;i++){
		var msg = loginMsg[i];
		if (msg.type==g_saving.name){
			var ppf = parseInt(msg.profit);
			if (msg.t==0)
				g_msg.tip("获得"+msg.name+"利息:"+ppf);
			else
				g_msg.tip("你的"+msg.name+"存款到期, 获得利息:"+ppf);
		}else if (msg.type==g_insure.name){
			if (msg.profit==-1){
				g_msg.tip("你的"+msg.name+"已到期");
			}else
				g_msg.tip("你的"+msg.name+"已到期,获得收益:"+parseInt(msg.profit));
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
}

Login.prototype.login = function(){
	g_msg.showload();
	
     var player = store.get(g_player.name);
     if (player==null){
     	alert("本地数据缺失，登录失败");
     	return;
     }
     
     var ppobj = {playerid:player.playerid,pwd:player.pwd,playername:player.playername}
   	var dataParam = obj2ParamStr("player",ppobj);
    var serverPlayer;
	try    {
		$.ajax({type:"post",url:"/cf/login_login.do",data:dataParam,success:function(data){
		 g_login.loginCallback(data);
         g_msg.destroyload();
		}});
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   return false;
	}	

	
   return true;
}

var g_login = new Login();