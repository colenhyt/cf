Insure = function(){
    this.name = "insure";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.data = {};
}

Insure.prototype = new Datamgr();

Insure.prototype.init = function(){
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_insuredata);
	}     
    this.buildHTML_body();
}

Insure.prototype.loadDataCallback = function(tx,results){
	for (var i=0;i<results.rows.length;i++){
		var item = results.rows.item(i);
		g_insure.data[item.id] = item;
    }
    g_insure.buildHTML_data();
}

Insure.prototype.buildHTML_body = function()
{
	var page = new PageUtil(this.tagname);
	page.buildSingleTab("保险业务");
	var content = 	"<div class=\"tab-pane in active\" id=\"insure1\">";
	content += "<div class=\"cfpage\" id='"+this.pagename+"'>"
    
    content += "</div></div>"
	page.addContent(content);
	document.write(page.toString());
	
	var pagedetail = new PageUtil(this.tagdetailname);
	content = 	"<div class=\"tab-pane in active\" id=\"insure2\">";
	content += "<div class=\"cfpage\" id='"+this.pagedetailname+"'>"
    
    content += "</div></div>"
	pagedetail.addContent(content);
	document.write(pagedetail.toString());
}

Insure.prototype.show = function()
{
	var tdata = store.get(this.name);
	var content = 	"";
	if (tdata.length<=0){
		  content += "<div class=\"panel\" ID=\"insure_d1\"><div class=\"panel-body\">no insure item</div>"
      content += "</div>"
	}else {
		for ( key in tdata){
			var item = tdata[key];
		  content += "<div class=\"panel\" ID=\"insure_d1\" onclick=\"g_insure.showDetail("+item.id+")\">"
		     content += "<div class=\"panel-body\"><h2>"+item.name+"</h2>"
			 content += "        <table id='toplist1_tab'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td class='td-c-name'>价格</td>"
			 content += "               <td class='td-c-value'>"+item.price+"</td>"
			 content += "               <td class='td-c-name'>收益</td>"
			 content += "               <td class='td-c-value'>"+item.profit+"</td>"
			 content += "               <td class='td-c-name'>周期</td>"
			 content += "               <td class='td-c-value'>"+item.period+"</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
      content += "</div></div>"
		}
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
    $('#my'+this.name).modal('show');    
}

Insure.prototype.buildHTML_detail = function()
{
	
}

Insure.prototype.loadDetail = function(id){
  
}

Insure.prototype.showDetail = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id-1];
   if (item==null) return;
        
var content =      "        <div><h2>"+item.name+"</h2>"
 content += "            <div>投保后，在保险期间，可以规避对应的风险，规避经济上的损失</div>"
 content += "        <table id='toplist1_tab'>"
 content += "           <thead>"
 content += "             <tr>"
 content += "               <td class='td-c-name'>价格</td>"
 content += "               <td class='td-c-value'>"+item.price+"</td>"
 content += "               <td class='td-c-name'>收益</td>"
 content += "               <td class='td-c-value'>"+item.profit+"</td>"
 content += "               <td class='td-c-name'>周期</td>"
 content += "               <td class='td-c-value'>"+item.period+"</td>"
content += "              </tr>"
content += "            </thead>"
content += "          </table>     "
content += "          <button class='btn btn-primary' data-toggle='modal' onclick='g_insure.buy("+id+")'>购买</button>"
content += "          <button class='btn btn-primary' data-toggle='modal' data-dismiss='modal'>取消</button>      "  
content += "             </div>"
content += "           </div>  "
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
	$('#'+this.tagdetailname).modal('show');
}

Insure.prototype.buy = function(id){
//alert('buy'+id);
  $('#myinsure_detail').modal('hide');
}

var g_insure = new Insure();
 g_insure.init();