
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
	$('#'+tagname).animate({marginTop:-320, opacity:'show'},2500,MsgTipCallback).fadeOut(1500);
	return tagname;
}

//title自定义：
Msg.prototype.openLevel = function()
{
	var lv = g_title.getLevel();
	var content =      "        <div style='margin: auto;text-align:center;margin-top:-70px'>"
	//content += "<img src='static/img/light.png' style='z-index:-10'>"
	content += "<div><img src='static/img/title_up.png'></div>"
	content += "<img src='static/img/pop_line.png'>"
	content += " <div class='cfplayer_head_bg uplevel'>"
    content += "<img src='"+head_imgs[g_player.data.sex].src+"'/>"
    content += " </div>"
	content += "            <div class='cfmsg_text uplevel'>恭喜升级为"+lv+"级<br>获得新称号:"
	content +=  g_title.getData(lv).name+"</div>"
	content += "          <button class='cf_bt' data-dismiss='modal'>确认</button>"
	content += "             </div>"
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
		
	$('#'+this.tagname).modal({position:120,show: true});  
}

//title自定义：
Msg.prototype.open2 = function(title,desc,okCallback,cbParam1,cbParam2,cbParam3,confmText)
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
		content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>"
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
		
		 $('#'+this.tagname).modal({position:120,show: true});  
}

Msg.prototype.open = function(desc,okCallback,cbParam1,cbParam2,cbParam3)
{
	this.open2("提示",desc,okCallback,cbParam1,cbParam2,cbParam3);
}

Msg.prototype.update = function()
{
	while (this.intips.length>0){
		var desc = g_msg.intips.shift();
		var tipid = this.createtip(desc);
		this.outtips.push(tipid);
		break;
	}
}

var g_msg = new Msg()
g_msg.init();
