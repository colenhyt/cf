
var MSG = {
	SUCCESS:0,
	INFO : 1,
	WARN : 2,
	ERROR : 3,
	DANGER : 4,
}

Msg = function(){
	this.name ="msg";
	this.tipname ="msgtip";
	this.loadname ="msgload";
	this.count = 0;
	this.tips = [];
	this.intips = [];
	this.outtips = [];
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
}

Msg.prototype = new Datamgr();

Msg.prototype.init = function()
{

	this.buildHTML();
	
	setInterval(function(){
	   g_msg.update();
	  },MsgDuration
	);	
}

Msg.prototype.buildHTML = function()
{
        var pagedetail = new PageUtil(this.tagname);
        var psubclass = "";
        content =     "<div class=\"tab-pane in active\" id='quest2'>";
        content += "<div class='cfpagedetail small' id='"+this.pagename+"'>"
        content += "</div></div>"
        pagedetail.addContent(content);
        document.write(pagedetail.toString()); 
        
 		var tag = document.getElementById(this.tagname+"_dialog");
 		if (tag){
		 tag.style.setProperty("height",getSizes().PageHeight);
 		}         
}

function MsgTipCallback(){
	var tagid = g_msg.outtips.shift();
	var tag = document.getElementById(tagid);
	if (tag ){
		document.body.removeChild(tag);
	}	
}

Msg.prototype.tip = function(desc)
{
	this.intips.push(desc);
}

Msg.prototype.createtip = function(desc)
{
	var tagname = "msgtip"+this.count;
	this.count ++;
	tag = document.createElement("DIV");
	tag.id = tagname;
	tag.className = "cfmsg";
	tag.innerHTML = desc;
    document.body.appendChild(tag);
	$('#'+tagname).animate({marginTop:-340},2800,MsgTipCallback);
	return tagname;
}

Msg.prototype.showload = function(callback,type)
{
    this.destroyload();
   	this.netmsgWait = NetReqWait;
    var index = callback.indexOf("findQuotes");
   	if (index>0){
   	 	this.netmsgWait = 10000;
   	}
	
	tag = document.createElement("DIV");
	tag.id = this.loadname;
	tag.className = "cfmsg load";

	var content = ""
content += "<img id='msg_load1' class='cfmsg_loadimg' src='static/img/load_1.png'>"
content += "<img id='msg_load2' class='cfmsg_loadimg' src='static/img/load_2.png'>"
if (!type){
content += "<img id='msg_load3' class='cfmsg_loadimg' src='static/img/load_3.png'>"
content += "<img id='msg_load4' class='cfmsg_loadimg' src='static/img/load_4.png'>"
}else{
content += "<img id='msg_load3' class='cfmsg_loadimg' src='static/img/load_5.png'>"
content += "<img id='msg_load4' class='cfmsg_loadimg' src='static/img/load_6.png'>"
}
content += "<img id='msg_load5' class='cfmsg_loadimg pp' src='static/img/load_0.png'>"
content += "<img id='msg_load6' class='cfmsg_loadimg pp' src='static/img/load_0.png'>"
content += "<img id='msg_load7' class='cfmsg_loadimg pp' src='static/img/load_0.png'>"
content += "</div>"

tag.innerHTML = content;

    document.body.appendChild(tag);

    var now = new Date();
	this.loadreq = {callback:callback,start:now.getTime()};
    
    loadAni();
}

Msg.prototype.destroyload = function()
{
 this.loadreq = null;

 var div=$("#"+this.loadname);
 if (div)
  div.remove();
}

//title自定义：
Msg.prototype.openModal = function(title,desc,okCallback,param)
{
	var content =      "        <div style='margin: auto;text-align:center;'>"
	content += "<div class='cfmsg_h2'>"+title+"</div>"
	content += "<br>"
	content += "            <div class='cfmsg_text'>"+desc+"</div>"
	if (okCallback==null){
		content += "          <button class='cf_bt' data-dismiss='modal'>确认</button>"	
	}else {
		var okParam = ""
		if (param)
			okParam = param
		content += "          <button class='cf_bt' onclick='"+okCallback+"("+okParam+")'>确认</button>"
	}
		content += "             </div>"
		var tag = document.getElementById(this.pagename);
		tag.innerHTML = content;
		
		 $('#'+this.tagname).modal({backdrop:'static',position:getSizes().MsgTop,show: true});  
}

//title自定义：
Msg.prototype.open2 = function(title,desc,okCallback,cbParam1,cbParam2,cbParam3,confmText,cancelCallback)
{
	var content =      "        <div style='margin: auto;text-align:center;'>"
	if (confmText==null)
		confmText = "确认";
	content += "<div class='cfmsg_h2'>"+title+"</div>"
	content += "<img src='static/img/pop_line.png'>"
	content += "            <div class='cfmsg_text'>"+desc+"</div>"
	if (okCallback==null){
		content += "          <button class='cf_bt' data-dismiss='modal'>确认</button>"	
	}else {
		if (!cancelCallback)
		content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>"
		else
		content += "          <button class='cf_bt bt_cancel' onclick='"+cancelCallback+"()'>取消</button>"
		if (cbParam1==null)
			cbParam1 = "1";
		if (cbParam2==null)
			cbParam2 = "1";
		if (cbParam3==null)
			cbParam3 = "1";
		content += "          <button class='cf_bt' onclick='"+okCallback+"("+cbParam1+","+cbParam2+","+cbParam3+")'>"+confmText+"</button>"
	}
		content += "             </div>"
		var tag = document.getElementById(this.pagename);
		tag.innerHTML = content;
		
		 $('#'+this.tagname).modal({position:getSizes().MsgTop,show: true});  
}

Msg.prototype.open = function(desc,okCallback,cbParam1,cbParam2,cbParam3)
{
	this.open2("提示",desc,okCallback,cbParam1,cbParam2,cbParam3);
}

Msg.prototype.openNetMsg = function(reqCallback)
{
	var desc = "网络异常，请确认网络畅通后重试";
	
	var content =      "        <div style='margin: auto;text-align:center;'>"
	var	confmText = "重试";
	content += "<div class='cfmsg_h2'>提示</div>"
	content += "<br>"
	content += "            <div class='cfmsg_text'>"+desc+"</div>"
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>"
	content += "          <button class='cf_bt' onclick='g_msg.requestAgain("+reqCallback+")'>"+confmText+"</button>"
	content += "             </div>"
		
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
	 $('#'+this.tagname).modal({position:getSizes().MsgTop,show: true});  
		 	
}

Msg.prototype.requestAgain = function(reqCallback){
	this.hide();
	reqCallback();
}


Msg.prototype.update = function()
{
	this.count++;
	if (this.count%20==0)
	{
		while (this.intips.length>0){
			var desc = g_msg.intips.shift();
			var tipid = this.createtip(desc);
			this.outtips.push(tipid);
			break;
		}
	}
	
	//net callback
	if (this.loadreq)
	{
	var now = new Date();
	var dura = now.getTime() - this.loadreq.start;
	if (dura<this.netmsgWait) return;
	 
		this.openNetMsg(this.loadreq.callback);
		this.destroyload();
	}	
}

var g_msg = new Msg()
g_msg.init();
