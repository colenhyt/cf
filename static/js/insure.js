Insure = function(){
    this.name = "insure";
    this.pageCount = 2;
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
		  content += "<div class='cfpanel' ID='insure_d1'><div class='cfpanel_body'>没有产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>tdata.length)
			end = tdata.length;
		  content += "<div class='cfpanel_body'>"
		for (var i=start;i<end;i++){
			var item = tdata[i];
		  content += "<div class='cfpanel' ID='insure_d1' onclick='g_insure.showDetail("+item.id+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
			 content += "	<div style='height:150px;'>"
			 content += "<span class='cfpanel_text'>价格: ￥"+ForDight(item.price)+"</span>"
			 content += "<span class='cfpanel_text right'>周期:"+item.period+"</span>"
			content += "     </div>"
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
        
 var content = "            <div>投保后，在保险期间，可以规避对应的风险，规避经济上的损失</div>"
 content += "           <div>  "
 content += "        <table id='toplist1_tab'>"
 content += "             <tr>"
 content += "               <td class='td-c-name'>价格</td>"
 content += "               <td class='td-c-value'>"+item.price+"</td>"
 content += "               <td class='td-c-name'>收益</td>"
 content += "               <td class='td-c-value'>"+item.profit+"</td>"
 content += "               <td class='td-c-name'>周期</td>"
 content += "               <td class='td-c-value'>"+item.period+"</td>"
content += "              </tr>"
content += "          </table>     "
content += "           </div>  "

    g_msg.open2(item.name,content,"g_insure.",id,0,0,"购买");
	
}

Insure.prototype.showDetail_finan = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id-1];
   if (item==null) return;
        
var content =      "        <div><h2 style='margin-top:-5px;margin-bottom:-5px;text-align:center'>"+item.name+"</h2>"
content += "<img src='static/img/pop_line.png'>"
 content += "            <div>投保后，在保险期间，可以规避对应的风险，规避经济上的损失</div>"
 content +=	"</div>"
 content += "           <div style='margin-top:10px;margin-bottom:30px;height:150px'>  "
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
 content += "             <tr>"
 content += "               <td colspan='2' class='td-c-name'><input type='button' class='cf_count' onclick='g_insure.countBuy(-1)'></td>"
 content += "               <td colspan='2' class='td-c-name'><input type='text' id='insure_count' value='1' style='width:150px;text-align:center;height:58px;font-size:32px'></td>"
 content += "               <td colspan='2' class='td-c-name'><input type='button' class='cf_count add' onclick='g_insure.countBuy(1)'></td>"
content += "              </tr>"
content += "            </thead>"
content += "          </table>     "
content += "           </div>  "
content += "           <div style='margin-top:10px'>  "
content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
content += "          <button class='cf_bt' onclick='g_stock.buy("+id+")'>购买</button>"
content += "             </div>"
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
	$('#'+this.tagdetailname).modal('show');
}

Insure.prototype.countBuy = function(count){
    var tag = document.getElementById('insure_count');
    var currCount = parseInt(tag.value);
    if (currCount+count<=0) {
	currCount = 1;
    }else
	currCount += count;
    tag.value = currCount;
    //alert(tag.value);
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
 g_insure.show();