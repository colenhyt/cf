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

Share.prototype.buildPage = function(page)
{
		var	desc = "<div style='cfevent_content'>"
		desc += "<div style='margin: auto;text-align:center;'>"+Share_PageText+"</div>"
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

Share.prototype.shareComplete = function() {
 playAudioHandler('open');

		var	desc = "<div style='cfevent_content'>"
		desc += "<br><div style='margin: auto;text-align:center;'>分享成功</div>"
			desc += "</div>"
			desc += "<br><br>"
			desc += "<div>"+Share_PageText_Prize+"</div>"

	var content =      "        <div style='margin: auto;text-align:center;'>"
	content += "<div class='cfmsg_h2'>游戏分享</div>"
	content += "<br>"
	content += "            <div class='cfmsg_text'>"+desc+"</div>"
	content += "  <button class='cf_bt' onclick='g_share.sharePrize()'>确认</button>"
	content += "             </div>"
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
	 $('#'+this.tagname).modal({position:getSizes().PageTop,show: true}); 
}	

Share.prototype.sharePrize = function() {
	var pp = [{t:0,v:Share_Prize}];
	g_player.prize(pp);
	playAudioHandler('money');
	g_share.close();
}

Share.prototype.close = function(id){ 
	playAudioHandler('close');	
	$('#'+this.tagname).modal('hide');  
}
	
var g_share = new Share();
 g_share.init();
