
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
	//alert('msgtipCallback');
	var desc = g_msg.tips.shift();
	if (desc!=null){
		g_msg.poptip(desc);
	}
}

Msg.prototype.tip = function(desc)
{
	var tag = document.getElementById(this.tipname);
	if (tag==null ){
	    this.poptip(desc);
    }else {
		this.tips.push(desc);
		if (this.tips.length==1)
		    this.poptip(desc);
	}
}

Msg.prototype.poptip = function(desc)
{
	var tag = document.getElementById(this.tipname);
	if (tag ){
		document.body.removeChild(tag);
	}
	tag = document.createElement("DIV");
	tag.id = this.tipname;
	tag.style.position = "absolute";
	tag.style.left = "60px";
	tag.style.top = "350px";
	tag.style.width = "500px";
	tag.style.height = "90px";
	tag.style.display = "none";
	tag.style.color = "yellow";
	tag.style.textAlign = "center";
	tag.style.border = "1px solid pink";
	tag.style.cssText += "background-color:blue";
	tag.style.zIndex = 2000;
	tag.innerHTML = desc;
    document.body.appendChild(tag);
    
	this.tips.shift();
    
	$('#'+this.tipname).animate({marginTop:-320, opacity:'show'},2500,MsgTipCallback).fadeOut(1500);
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

var g_msg = new Msg()
g_msg.init();
