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
     content += "<div class=\"panel-body\">保险</div>"
      content += "</div>"
	  content += "</div></div>"
	page.addContent(content);
	document.write(page.toString());
}

Insure.prototype.buildHTML_detail = function()
{
	var page = new PageUtil(this.tagname+"detail");
	page.buildSingleTab("");
var content =      "        <div><h2>平安保险</h2>"
 content += "            <div>投保后，在保险期间，可以规避对应的风险，规避经济上的损失</div>"
 content += "        <table id='toplist1_tab'>"
 content += "           <thead>"
 content += "             <tr>"
 content += "               <td class='td-c-name'>价格</td>"
 content += "               <td class='td-c-value'>123</td>"
 content += "               <td class='td-c-name'>收益</td>"
 content += "               <td class='td-c-value'>11</td>"
 content += "               <td class='td-c-name'>周期</td>"
 content += "               <td class='td-c-value'>99</td>"
content += "              </tr>"
content += "            </thead>"
content += "          </table>     "
content += "          <button class='btn btn-primary' data-toggle='modal' onclick='g_insure.buy(1)'>购买</button>"
content += "          <button class='btn btn-primary' data-toggle='modal' data-dismiss='modal'>取消</button>      "  
content += "             </div>"
content += "           </div>  "
	page.addContent(content);
	document.write(page.toString());
}

Insure.prototype.loadDetail = function(id){
  
}

Insure.prototype.showDetail = function(id){    
        this.loadDetail(id);
	$('#myinsure_detail').modal('show');
}

Insure.prototype.buy = function(id){
  $('#myinsure_detail').modal('hide');
}

var g_insure = new Insure();
g_insure.init();