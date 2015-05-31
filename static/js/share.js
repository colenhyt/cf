Share = function(){
    this.name = "share";
    this.cname = "分享";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
}

Share.prototype = new Datamgr();

Share.prototype.init = function(){ 
    this.buildHTML();
}

Share.prototype.buildHTML = function()
{
        var pagedetail = new PageUtil(this.tagname);
        var psubclass = "";
        content =     "<div class=\"tab-pane in active\" id='quest2'>";
        content += "<div class='cfpagedetail small' id='"+this.pagename+"'>"
        content += "</div></div>"
        pagedetail.addContent(content);
        document.write(pagedetail.toString());  
}

Share.prototype.buildPage = function()
{
playAudioHandler('open');	

		var	desc = "<div style='cfevent_content'>"
		desc += "<table><tr>"
		desc += "<td><div>"+Share_PageText+"</div></td>"
		desc += "</tr></table>"
			desc += "</div>"
			desc += "<br><br>"
			desc += "<div>"+Share_PageText2+"</div>"

	var content =      "        <div style='margin: auto;text-align:center;'>"
	content += "<div class='cfmsg_h2'>游戏分享</div>"
	content += "<br>"
	content += "            <div class='cfmsg_text'>"+desc+"</div>"
	content += "           <button class='cf_bt bt_cancel' onclick='g_share.close()'>取消</button>"
	content += "  <button class='cf_bt' onclick='g_share.doShare()'>分享</button>"
	content += "             </div>"
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
}

Share.prototype.doShare = function() {
   showShareMenuClickHandler();
}

Share.prototype.requestShare = function() {
	
}
	
Share.prototype.close = function(id){ 
	playAudioHandler('close');	
	$('#'+this.tagname).modal('hide');  
}
	
var g_share = new Share();
 g_share.init();
