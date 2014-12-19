// JavaScript Document
Help = function(){
	this.name = "help"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
}

Help.prototype = new Datamgr();

Help.prototype.init = function(){
	this.buildHTML("title_help.png");
}

Help.prototype.buildPage = function(page)
{
	var content = "<div class='cf-help-prize'>"
	content +=	"<div> 今天获得:"
    content +=      "      <div id='help_gettoday'></div>"
    content +=       " </div>"
    content +=           "  <div> 明天将获得:"
    content +=            "     <div id='help_gettomorrow'></div>"
    content +=            " </div></div>"
    content +=            " <div class='cf-help-feeling'> 请选择你今天的心情:"
    content +=            "     <div id ='help_feeling'>help_feeling</div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;

}

Help.prototype.show = function(){
	
     var option = {
    	position : 50,
    	show     : true
	}
    $('#'+this.tagname).modal(option);       
}

var g_help = new Help();
g_help.init();
