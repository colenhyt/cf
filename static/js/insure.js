Insure = function(){
    this.name = "insure";
    this.pageCount = 5;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.data = {};
}

Insure.prototype = new Datamgr();

Insure.prototype.init = function(){
 store.remove(this.name)
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_insuredata);
	}     
    this.buildHTML();
}

Insure.prototype.buildPage = function(page)
{
	if (page<0)
		return
		
	var tdata = store.get(this.name);
	var content = 	"";
	if (tdata.length<=0){
		  content += "<div class='cfpanel' ID='insure_d1'><div class='panel-body'>没有产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>tdata.length)
			end = tdata.length;
		  content += "<div style='height:330px'>"
		for (var i=start;i<end;i++){
			var item = tdata[i];
		  content += "<div class='cfpanel' ID='insure_d1' onclick='g_insure.showDetail("+item.id+")'>"
		     content += "<h2 class='cf_h'>"+item.name+"</h2>"
			 content += "        <table id='toplist1_tab' width='100%'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td>价格:￥"+item.price+"</td>"
			 content += "               <td class='td_right'>周期:"+item.period+"h</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
      		content += "</div>"
		}
   		content += "</div>"
        
        content += this.buildPaging(page,tdata.length);
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
content += "          <button class='btn btn-primary' onclick='g_insure.buy("+id+")'>购买</button>"
content += "          <button class='btn btn-primary' data-dismiss='modal'>取消</button>      "  
content += "             </div>"
content += "           </div>  "
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
	$('#'+this.tagdetailname).modal('show');
}

Insure.prototype.buy = function(id){
if (id>0)
{
	var item = this.findItem(id);
	if (item!=null){
		if (g_player.data.cash<item.price){
			alert('你的钱不够');
			return;
		}
		var pinsure = g_player.data.insure;
		if (pinsure==null)
			pinsure=[];
		var jsonCurr = Date.parse(new Date());
		pinsure.push({id:item.id,accept:jsonCurr,status:0});
		var cash = g_player.data.cash - item.price;
		var pdata = {"insure":pinsure,"cash":cash};
		g_player.updateData(pdata);
		g_quest.onBuyInsure(item);
	}
}
  $('#myinsure_detail').modal('hide');
}

var g_insure = new Insure();
 g_insure.init();