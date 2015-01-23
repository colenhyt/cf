
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
	$('#'+tagname).animate({marginTop:-340},1800,MsgTipCallback);
	return tagname;
}

Msg.prototype.showload = function()
{
  this.destroyload();
	
	tag = document.createElement("DIV");
	tag.id = this.loadname;
	tag.className = "cfmsg load";

	var content = ""
content += "<img id='msg_load1' class='cfmsg_loadimg' src='static/img/load_1.png'>"
content += "<img id='msg_load2' class='cfmsg_loadimg' src='static/img/load_2.png'>"
content += "<img id='msg_load3' class='cfmsg_loadimg' src='static/img/load_3.png'>"
content += "<img id='msg_load4' class='cfmsg_loadimg' src='static/img/load_4.png'>"
content += "<img id='msg_load5' class='cfmsg_loadimg' src='static/img/load_0.png'>"
content += "<img id='msg_load6' class='cfmsg_loadimg' src='static/img/load_0.png'>"
content += "<img id='msg_load7' class='cfmsg_loadimg' src='static/img/load_0.png'>"
content += "</div>"

tag.innerHTML = content;

    document.body.appendChild(tag);
    
    loadAni();
}

Msg.prototype.destroyload = function()
{
 var div=$("#"+this.loadname);
 if (div)
  div.remove();
}

//title自定义：
Msg.prototype.openModal = function(title,desc,okCallback,param)
{
	var content =      "        <div style='margin: auto;text-align:center;'>"
	content += "<div class='cfmsg_h2'>"+title+"</div>"
	content += "<img src='static/img/pop_line.png'>"
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

Msg.prototype.update = function()
{
	this.count++;
	if (this.count%3==0)
	{
		while (this.intips.length>0){
			var desc = g_msg.intips.shift();
			var tipid = this.createtip(desc);
			this.outtips.push(tipid);
			break;
		}
	}
}

var g_msg = new Msg()
g_msg.init();
