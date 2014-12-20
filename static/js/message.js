
var MSG = {
	SUCCESS:0,
	INFO : 1,
	WARN : 2,
	ERROR : 3,
	DANGER : 4,
}

Msg = function(){
	this.name ="msg"
    this.tagname = "my"+this.name;
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
        pclass = "cfpagedetail small";
        content =     "<div class=\"tab-pane in active\" id='quest2'>";
        content += "<div class='"+pclass+"' id='"+this.pagename+"'>"
        content += "</div></div>"
        pagedetail.addContent(content);
        document.write(pagedetail.toString());  
}

Msg.prototype.open = function(desc,okCallback,cbParam1,cbParam2)
{
var content = "            <div>"+desc+"</div>"
content += "           <div style='margin-top:10px'>  "
if (okCallback==null){
	content += "          <button class='cf_bt' data-dismiss='modal'>确认</button>"	
}else {
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>"
	content += "          <button class='cf_bt' onclick='"+okCallback+"("+cbParam1+","+cbParam2+")'>确认</button>"
}
	content += "             </div>"
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
	 $('#'+this.tagname).modal({position:200,show: true});  
}

var g_msg = new Msg()
g_msg.init();
