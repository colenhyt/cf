
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
}

Msg.prototype.init = function()
{
	this.buildHTML();
}

Msg.prototype.buildHTML = function()
{
	var page = new PageUtil(this.tagname,2000);
	page.buildMsg("messager-content");
	document.write(page.toString());
}

Msg.prototype.show = function(strMsg,level)
{
	var tag = document.getElementById("messager-content");
	tag.innerHTML = strMsg;
	 $('#'+this.tagname).modal('show');  
}

var g_msg = new Msg()
g_msg.init();
