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
    
     var   content =     "<div class=\"tab-pane in active\" id='quest2'>";
        content += "<div class='cfpagedetail stock' id='"+this.pagename+"'>"
        content += "</div></div>"
    page.addContent(content);
    document.write(page.toString());  
    
	         
 		var tag = document.getElementById(this.tagname+"_dialog");
 		if (tag){
		 tag.style.setProperty("height",getSizes().PageHeight);
 		}    
}

Help.prototype.closeHelp = function(){
	playAudioHandler('close1');	
	$('#'+this.tagname).modal('hide');  
    	var tag = document.getElementById("tag"+this.name);
    	if (tag&&this.name!=g_playerinfo.name){
    		tag.innerHTML = ""
    	}		
}

Help.prototype.getDesc = function(){
	var desc = HELP_DESC;
	
	return desc;
}

Help.prototype.show = function(){
	playAudioHandler('open1');	

	var desc = this.getDesc();
	
	var content =      "        <div style='margin: auto;text-align:center;'>"
	content += "<div class='cfmsg_h2 help'>游戏帮助</div>"
	content += "<br>"
	content += "            <div class='cfmsg_text help' id='cphelp_txt'>"+desc+"</div>"
	content += "          <button class='cf_bt' onclick='g_help.closeHelp()'>关闭</button>"	
	content += "             </div>"
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
		
    $('#'+this.tagname).modal({backdrop:'static',position:20,show: true});     
}

Help.prototype.nextpage = function(page){
	var tag = document.getElementById('cphelp_txt');
	if (page==1)
	 tag.innerHTML = HELP_DESC;
	else
	 tag.innerHTML = HELP_DESC2;
}

var g_help = new Help();

