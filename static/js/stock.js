Stock = function(){
    this.name = "stock";
    this.pageCount = 4;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.graphName = this.name+"graph";
    this.data = {};
}

Stock.prototype = new Datamgr();

Stock.prototype.init = function(){
 store.remove(this.name)
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_stockdata);
	}     
    this.buildHTML();
}

Stock.prototype.buildPage = function(page)
{
	if (page<0)
		return
		
	this.updateQuotes();
	
	var tdata = store.get(this.name);
	var content = 	"";
	if (tdata.length<=0){
		  content += "<div class='cfpanel' ID='stock_d1'><div class='panel-body'>没有产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>tdata.length)
			end = tdata.length;
		  content += "<div style='height:330px'>"
		for (var i=start;i<end;i++){
			var item = tdata[i];
			var pstock = g_player.getPlayerStock(item);
			if (pstock==null) {
			    pstock = {profit:0,avgPrice:0,unit:0};
			}
		  content += "<div class='cfpanel' ID='stock_d1' onclick='g_stock.showDetail("+item.id+")'>"
		     content += "<h2 class='cf_h'>"+item.name+"("+item.desc+")</h2>"
			 content += "        <table id='toplist1_tab'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td class='td-c-name'>价格</td>"
			 content += "               <td class='td-c-value'>"+item.price+"</td>"
			 content += "               <td class='td-c-name'>平均价格</td>"
			 content += "               <td class='td-c-value'>"+pstock.avgPrice+"</td>"
			 content += "               <td class='td-c-name'>持有</td>"
			 content += "               <td class='td-c-value'>"+pstock.unit+"手</td>"
			 content += "               <td class='td-c-name'>盈亏</td>"
			 content += "               <td class='td-c-value'>"+pstock.profit+"</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
      		content += "</div>"
		}
			content += "           <div style='margin-top:10px'>  "
			content += "          股市开放时间为8:00AM-9:00PM"  
			content += "             </div>"
     		content += "</div>"
		
        content += this.buildPaging(page,tdata.length);
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
}

Stock.prototype.showDetail = function(id){    
	var tdata = store.get(this.name);
   var item = this.findItem(id);
   if (item==null) return;
        
var content =      "        <div><h2 style='margin-top:-5px;margin-bottom:-5px'>"+item.name+"</h2>"
content += "<img src='static/img/pop_line.png'>"
 content += "<div style='margin-top:10px;margin-bottom:30px;height:30px'> "+item.desc+"</div>"
content += "<img src='static/img/pop_line.png'>"
 content +=	"</div>"
content += "<div id='"+this.graphName +"' style='margin-left:-12px'></div>"
 content += "           <div>  "
 content += "        <table id='toplist1_tab'>"
 content += "           <thead>"
 content += "             <tr>"
 content += "               <td class='td-c-name'>价格</td>"
 content += "               <td class='td-c-value'>"+item.price+"</td>"
 content += "               <td class='td-c-name'>单位</td>"
 content += "               <td class='td-c-value'>"+item.unit+"</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td colspan='3' class='td-c-name'><input type='button' class='cf_bt_green' value='减持100' onclick='g_stock.confirmBuy("+item.id+",-1)'></td>"
 content += "               <td colspan='3' class='td-c-name'><input type='button' class='cf_bt_green right' value='加持100' onclick='g_stock.confirmBuy("+item.id+",1)'></td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td colspan='3' style='float:right'><img src='static/img/icon_tip.png' style='width:20px;height:17px'></td>"
 content += "               <td colspan='3' class='cf_font4'>100股=1手</td>"
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
        
    //g_stockdetail.drawQuote(item.name,this.graphName);
	$('#'+this.tagdetailname).modal({position:0,show: true});  
}

Stock.prototype.confirmBuy = function(id,qty){
   var item = this.findItem(id);
	var strDesc = "确定";
	if (qty>0) {
	    var needCash = item.price * qty;
	    var cash = g_player.saving[0].amount;
	    if (cash<needCash){
		    g_msg.open("你的钱不够");
		    return;
	    }		
		strDesc += "加持";
	}else {
		strDesc += "减持";
	}
		
	strDesc += "股票"+item.name+qty+"手???";
	g_msg.open(strDesc,"g_stock.buy",id,qty);
}

Stock.prototype.buy = function(id,qty){

    if (id<=0)	return;

    var item = this.findItem(id);
    if (item==null){
		g_msg.open("找不到该股票: id="+id);
		return;    
   	}
   	
    var needCash = item.price * qty;
	var cash = g_player.saving[0].amount;
    if (cash<needCash){
	    g_msg.open("你的钱不够");
	    return;
    }
     var jsonDate = Date.parse(new Date());
    var tstock = {id:item.id,playerid:g_player.data.playerid,createtime:jsonDate
    	,status:0,price:item.price,qty:qty};
			
	var dataParam = obj2ParamStr("stock",tstock);
	var ret = myajax("stock_add",dataParam);
	if (ret==null||ret.code!=0)
	{
		g_msg.open("股票购买失败:code="+ret.code);
		return;
	}
	
    var pstock = g_player.stock;
    if (pstock==null)
	    pstock=[];
	pstock.push(tstock);
    var newcash = cash - needCash;
    var pdata = {"cash":newcash};
    g_player.updateData(pdata);
    g_quest.onBuyStock(item);
	g_msg.open("股票购买成功:"+item.name);
    $('#'+this.tagdetailname).modal('hide');
}

//取行情:
Stock.prototype.updateQuotes = function(){
	var quotes ;
	try  {
		var dataobj = $.ajax({type:"post",url:"/cf/stock_quote.do",async:false});
		if (dataobj!=null&&dataobj.responseText.length>0) {
			quotes = eval ("(" + dataobj.responseText + ")");
		}
	}   catch  (e)   {
	    document.write(e.name);
	} 
	if (quotes!=null){
		store.set(this.name,quotes);
		//var tdata = store.get(this.name);
	
	}
}
var g_stock = new Stock();
 g_stock.init();