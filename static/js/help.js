// JavaScript Document
Help = function(){
	this.name = "help"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
}

Help.prototype = new Datamgr();

Help.prototype.init = function(){
	this.buildHTML();
}

Help.prototype.buildHTML = function()
{
    var page = new PageUtil(this.tagname);
    
     var content =     "<div class='tab-pane in active' onclick='g_help.closeHelp()'>";
    content += "<div class='cfhelp' id='"+this.pagename+"'>"
	content += "<div class='cfhelp_info'><img src='static/img/arrow.png'><span class='cfhelp_info_text'>个人信息</span></div>"
	content += "<div class='cfhelp_quest'><img src='static/img/arrow.png' class='cfhelp_quest_img'><span class='cfhelp_quest_text'>任务</span></div>"
	content += "<div class='cfhelp_toplist'><img src='static/img/arrow.png' class='cfhelp_toplist_img'><span class='cfhelp_toplist_text'>排行榜</span></div>"    
    content += "</div>"
    content += "</div>"
    page.addContent(content);
    document.write(page.toString());  
}

Help.prototype.closeHelp = function(){
	$('#'+this.tagname).modal('hide');  
	
    	var tag = document.getElementById("tag"+this.name);
    	if (tag){
    		tag.innerHTML = ""
    	}		
}
Help.prototype.show = function(){
    $('#'+this.tagname).modal({position:0,show: true});       
}

var g_help = new Help();
g_help.init();
