Insure = function(){
    this.name = "insure";
    this.tagname = "my"+this.name;
}

Insure.prototype = new Datamgr();

Insure.prototype.init = function(){
    g_game.register(this);
    this.buildHTML();
    //store.enable();
    store.set('colen11','rrrrr5555');
}

Insure.prototype.buildHTML = function()
{
	var page = new PageUtil(this.tagname);
	page.buildSingleTab("保险业务");
	var content = 	"<div class=\"tab-pane in active\" id=\"insure1\">";
	content += "<div class=\"cfpage\">"
	  content += "<div class=\"panel\" ID=\"insure_d1\" onclick=\"g_insure.showDetail(1)\">"
     content += "<div class=\"panel-body\">理财险</div>"
      content += "</div>"
	  content += "</div></div>"
	page.addContent(content);
	document.write(page.toString());
}

Insure.prototype.loadDetail = function(id){
  
}

Insure.prototype.showDetail = function(id){    
        this.loadDetail(id);
	$('#myinsure_detail').modal('show');
	var tag = document.getElementById("myinsure_detail");
}

Insure.prototype.buy = function(id){
  $('#myinsure_detail').modal('hide');
}

var g_insure = new Insure();
g_insure.init();