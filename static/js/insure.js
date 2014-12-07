Insure = function(){
    this.name = "insure";
    this.pageCount = 3;
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
    this.buildHTML("title_insure.png");
}

Insure.prototype.buildPage = function(page)
{
	if (page<0)
		return
		
	var tdata = store.get(this.name);
	var content = 	"";
	if (tdata.length<=0){
		  content += "<div class='panel' ID='insure_d1'><div class='panel-body'>没有产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>tdata.length)
			end = tdata.length;
		for (var i=start;i<end;i++){
			var item = tdata[i];
		  content += "<div class='panel' ID='insure_d1' onclick='g_insure.showDetail("+item.id+")'>"
		     content += "<div class='panel-body'><h2>"+item.name+"</h2>"
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
		
		var tpages = parseInt(tdata.length/this.pageCount);
		var tmodel = tdata.length%this.pageCount;
		if (tmodel>0)
			tpages++;
        
		content +=     "        <div style='color:#ffffff'>"
		content += "<input type='button' class='form-control2' value='上一页' onclick='g_insure.buildPage("+(page-1)+")'/>"
		content += ""+(page+1)+"/"+tpages
		content += "<input type='button' class='form-control2' value='下一页' onclick='g_insure.buildPage("+(page+1)+")'/>"
		content += "           </div>  "
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
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