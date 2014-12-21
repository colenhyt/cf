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
		  content += "<div class='cfpanel_body'>"
		for (var i=start;i<end;i++){
			var item = tdata[i];
			var pstock = g_player.getPlayerStock(item);
			var psColor = "green";
			if (pstock.profit<0)
				psColor = "red"
		     content += "<div class='cfpanel' ID='stock_d1' onclick='g_stock.showDetail("+item.id+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
		     content += "<span class='cfpanel_text right'>目前持有<span style='color:yellow'> "+pstock.qty+"</span> 手</span>"
			 content += "	<div style='height:150px;'>"
			 content += "<span class='cfpanel_text'>当前价格: ￥"+ForDight(item.price)+"</span>"
			 content += "<span class='cfpanel_text right'>总盈亏: <span style='color:"+psColor+"'>"+ForDight(pstock.profit)+"</span></span>"
			content += "     </div>"
      		content += "</div>"
		}
			content += "           <div style='margin-top:10px;font-size:25px;color:pink'>  "
			content += "          股市开放时间为8:00AM-9:00PM <br>股票价格每10分钟变动一次"  
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
        
   var pstock = g_player.getPlayerStock(item);
   var strPro = "尚未持有";
   if (pstock){
	var psColor = "green";
	if (pstock.profit<0)
		psColor = "red"   
   	 strPro = "盈亏:<span style='color:"+psColor+"'>"+ForDight(pstock.profit)+"</span>";
   }
var content =      "        <div class='cfpanel_text'><div class='cpgapedetail_h2'>"+item.name
content += "<span class='cfpanel_text right'>"+strPro+" </span>"
content += "</div>"
content += "<img src='static/img/pop_line.png' class='cf_line'>"
 content += "<div> "+item.descs+"</div>"
content += "<img src='static/img/pop_line.png' class='cf_line'>"
 content +=	"</div>"
content += "<div id='"+this.graphName +"'></div>"
 content += "           <div>  "
 content += "        <table id='toplist1_tab'>"
 content += "             <tr>"
 content += "               <td>买入价</td>"
 content += "               <td>"+ForDight(item.price)+"</td>"
 content += "               <td>单位</td>"
 content += "               <td>"+item.unit+"</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td>持有:</td>"
 content += "               <td>"+pstock.qty+" 手</td>"
 content += "               <td>可买入:</td>"
 content += "               <td>"+item.unit+"手</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td colspan='2'><input type='button' class='cf_bt_green' value='减持100股' onclick='g_stock.confirmBuy("+item.id+",-1)'></td>"
 content += "               <td colspan='2'><input type='button' class='cf_bt_green right' value='加持100股' onclick='g_stock.confirmBuy("+item.id+",1)'></td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td colspan='2' style='float:right'><img src='static/img/icon_tip.png' style='width:20px;height:17px'></td>"
 content += "               <td colspan='2' class='cf_font4'>100股=1手</td>"
content += "              </tr>"
content += "          </table>     "
content += "           </div>  "
content += "           <div style='align:center'>  "
content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
//content += "          <button class='cf_bt' onclick='g_stock.buy("+id+")'>购买</button>"
content += "             </div>"
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
    g_stockdetail.drawQuote(item.name,this.graphName);
	$('#'+this.tagdetailname).modal({position:0,show: true});  
}

Stock.prototype.confirmBuy = function(id,qty){
   var item = this.findItem(id);
	var strDesc = "确定";
	var strQty = qty;
	if (qty>0) {
	    var needCash = item.price * qty;
	    var cash = g_player.saving[0].amount;
	    if (cash<needCash){
		    g_msg.open("你的现金不够，股票购买失败!");
		    return;
	    }		
		strDesc += "加持";
	}else {
		strDesc += "减持";
		strQty = 0 - strQty;
	}
		
	strDesc += " <span style='color:red'>"+strQty+"</span> 手 "+item.name+" ?";
	g_msg.open(strDesc,"g_stock.buy",id,qty);
}

Stock.prototype.buy = function(id,qty){

    if (id<=0)	return;

    var item = this.findItem(id);
    if (item==null){
		g_msg.open("没有该股票: id="+id);
		return;    
   	}
   	
    var needCash = item.price * qty;
	var cash = g_player.saving[0].amount;
    if (cash<needCash){
	    g_msg.open("你的现金不够，股票购买失败!");
	    return;
    }
     var jsonDate = Date.parse(new Date());
    var tstock = {stockid:item.id,playerid:g_player.data.playerid,createtime:jsonDate
    	,status:0,price:item.price,qty:qty,amount:item.price*qty};
			
	var dataParam = obj2ParamStr("stock",tstock);
	var ret = myajax("stock_add",dataParam);
	if (ret==null||ret.code!=0)
	{
		g_msg.open("购买失败:code="+ret.code);
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
	g_msg.open("成功购买:"+item.name);
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
