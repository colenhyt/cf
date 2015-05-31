Share = function(){
    this.name = "insure";
    this.cname = "分享";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.data = {};
}

Share.prototype = new Datamgr();

Share.prototype.init = function(){
 store.remove(this.name)
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_insuredata);
	}     
    this.buildHTML();
}

Share.prototype.buildPage = function(page)
{
	var content =      "        <div class='cfinsure_content'>"
	content += "<div class='cfmsg_h2'>"+title+"</div>"
	content += "<div class='cfmsg_text insure'>"+desc+"</div>"
	content += "          <button class='cf_bt bt_cancel' onclick='g_insure.closeDetail()'>取消</button>"
	if (confmText)
	content += "          <button class='cf_bt' onclick='"+okCallback+"("+itemid+","+qty+")'>"+confmText+"</button>"
	content += "             </div>"
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
		
	$('#'+this.tagname).modal({position:getSizes().DetailPageTop,show: true});  
     
	
}

Share.prototype.doShare = function(id,qty) {

    	
	this.requestShare();
}

Share.prototype.requestShare = function() {

}
	
var g_share = new Share();
 g_share.init();
