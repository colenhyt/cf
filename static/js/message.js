
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
	this.tips = [];
    this.tagname = "my"+this.name;
    this.tipTop = "300px";
    this.pagename = this.tagname+"page";
}

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
	//alert('msgtipCallback');
	var tipid = g_msg.tips.shift();
	if (tipid!=null){
		var tag = document.getElementById(tipid);
		document.body.removeChild(tag);
		//alert('remove'+tipid);
	}
}

Msg.prototype.tip = function(desc)
{
	var tag = document.createElement("DIV");
	tag.id = "tip"+Date.parse(new Date());
	tag.style.position = "absolute";
	tag.style.left = "60px";
	tag.style.top = this.tipTop;
	tag.style.width = "500px";
	tag.style.display = "none";
	tag.style.color = "yellow";
	tag.style.textAlign = "center";
	tag.style.border = "1px solid pink";
	tag.innerHTML = desc;
	tag.style.zIndex = 2000;
    document.body.appendChild(tag);
	//this.tips.push(tag.id);

	//$('#'+tag.id).animate({marginTop:-200, opacity:'show'},1200,MsgTipCallback).fadeOut(2000);
}

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

var g_msg = new Msg()
g_msg.init();
