Stock = function(){
    this.name = "stock";
    this.quotename = "quote";
    this.cname = "股票";
    this.pageCount = 4;
    this.currPage = 0;
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

Stock.prototype.load = function(data)
{
	store.set(this.name,data);
	var qdata = {};
	for (var i=0;i<data.length;i++){
		qdata[data[i].id] = eval ("(" + data[i].jsonquotes + ")");
	}
	store.set(this.quotename,qdata);
}

Stock.prototype.loadNextQuoteTime = function()
{
	var quatetime;
	try  {
		var dataobj = $.ajax({type:"post",url:"/cf/stock_nextquotetime.do",async:false});
		if (dataobj!=null&&dataobj.responseText.length>0) {
			quatetime = parseFloat(dataobj.responseText);
		}
	}   catch  (e)   {
	    logerr(e.name);
	    return;
	} 
	return quatetime;
}

Stock.prototype.loadQuotes = function(stockid,fromServer)
{
	var qdata = store.get(this.quotename);
	if (qdata==null){
		qdata = {};
	}
	
	var squotes = qdata[stockid];
	if (squotes!=null&&fromServer!=true){
		return;
	}
	try  {
		var dataobj = $.ajax({type:"post",url:"/cf/stock_quotes.do",data:"stock.id="+stockid,async:false});
		if (dataobj!=null&&dataobj.responseText.length>0) {
			squotes = eval ("(" + dataobj.responseText + ")");
		}
	}   catch  (e)   {
	    logerr(e.name);
	    return;
	} 
	qdata[stockid] = squotes;
	store.set(this.quotename,qdata);
}

Stock.prototype.findStockIds = function()
{
	var countNeed = 15;
	
	var tdata = store.get(this.name);
	var data = [];
	var ids = [];
	for (var i=0;i<tdata.length;i++){
		data.push(tdata[i].id);
		var pitem = g_player.getItemData(this.name,tdata[i]);
		if (pitem.qty>0)
			ids.push(tdata[i].id);
	}
	
	return randomItems(data,ids,countNeed);
}

Stock.prototype.show = function(){
	if (g_player.data.openstock!=1){
		g_msg.open2("证券开户","您需要开通证券账户才能投资股票，请点击'确认'按钮开通","g_stock.confirmOpen");
	}else {
		this.buildPage(0);
        $('#'+this.tagname).modal({position:Page_Top,show: true});     
	}
}

Stock.prototype.confirmOpen = function(){
	g_player.updateData({openstock:1});
	g_msg.hide();
	g_msg.tip("证券账户已开通");
	this.show();
}

Stock.prototype.buildPage = function(page)
{
	if (page<0)	return
	
	var tdata = store.get(this.name);
	var content = 	"";
	if (tdata.length<=0){
		  content += "<div class='cfpanel' ID='stock_d1'><div class='panel-body'>没有产品</div>"
      content += "</div>"
	}else {
		var tt = this.loadNextQuoteTime();
		var loadFromServer = false;
		if (tt>0.95)
			loadFromServer = true;
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		var ritems = this.findStockIds();
		if (end>ritems.length)
			end = ritems.length;
		  content += "<div class='cfpanel_body'>"
		for (var i=start;i<end;i++){
			var item = this.findItem(ritems[i]);
			this.loadQuotes(item.id,loadFromServer);
			var pitem = g_player.getItemData("stock",item);
			var quote = this.findLastQuote(item.id);
			var ps = 0;
			if (quote!=null)
				ps = quote.price;
			var psColor = "red";
			if (pitem.profit<=0)
				psColor = "green"
		     content += "<div class='cfpanel' ID='"+this.name+"_d"+item.id+"' onclick='g_stock.showDetail("+item.id+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
		     content += "<span class='cfpanel_text right'>目前持有<span style='color:yellow'> "+pitem.qty/100+"</span> 手</span>"
			 content += "	<div>"
			 content += "<span class='cfpanel_text'>当前价格: ￥"+ForDight(ps)+"</span>"
			 content += "<span class='cfpanel_text right'>总盈亏: <span style='color:"+psColor+"'>"+ForDight(pitem.profit)+"</span></span>"
			content += "     </div>"
      		content += "</div>"
		}
			content += "           <div style='margin-top:10px;font-size:25px;color:pink'>  "
			content += "          股市开市为8:00AM-9:00PM,下次股价变动:"  
			content += "             </div>"
			content += "    <div class='cfstock_qtime0'><p id='stock_qtime' class='cfstock_qtime'></p></div>"  
     		content += "</div>"
		
		 
		this.currPage = page;
        content += this.buildPaging(page,ritems.length);
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
	var mp = 530*tt;
	if (mp>500)
		mp = 500;
	mp += "px";
	$("#stock_qtime").animate({marginRight:mp});
	
}

Stock.prototype.showDetail = function(id,isflush){    
	this.onPanelClick(id);
	
	var tdata = store.get(this.name);
   var item = this.findItem(id);
   if (item==null) return;
        
   var pitem = g_player.getItemData("stock",item);
   var strPro = "尚未持有";
   if (pitem){
	var psColor = "red";
	if (pitem.profit<0)
		psColor = "green"   
   	 strPro = "盈亏:<span style='color:"+psColor+"'>"+ForDight(pitem.profit)+"</span>";
   }
   
	var quote = this.findLastQuote(item.id);
	var ps = 0;
	if (quote==null) {
		ps = 1;
		g_msg.tip("当前股票没有行情"+item.name);
	}else {
		ps = quote.price;
	}
   
    var amount = g_player.saving[0].amount;
    var canBuyQty = parseInt(amount/(ps*100));
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
	 content += "               <td>"+ForDight(ps)+"</td>"
	 content += "               <td>单位</td>"
	 content += "               <td>100 股/手</td>"
	content += "              </tr>"
	 content += "             <tr>"
	 content += "               <td>持有:</td>"
	 content += "               <td>"+pitem.qty/100+" 手</td>"
	 content += "               <td>可买入:</td>"
	 content += "               <td>"+canBuyQty+"手</td>"
	content += "              </tr>"
	 content += "             <tr>"
	 content += "               <td colspan='2'><input type='button' class='cf_bt_green' value='减持100股' onclick='g_stock.confirmBuy("+item.id+",-100)'></td>"
	 content += "               <td colspan='2'><input type='button' class='cf_bt_green right' value='加持100股' onclick='g_stock.confirmBuy("+item.id+",100)'></td>"
	content += "              </tr>"
	 content += "             <tr>"
	 content += "               <td colspan='2' style='float:right;height:30px'></td>"
	 content += "               <td colspan='2' class='cf_font4'></td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "
	content += "           <div style='align:center'>  "
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>退出</button>      "  
	//content += "          <button class='cf_bt' onclick='g_stock.buy("+id+")'>购买</button>"
	content += "             </div>"
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
	
    g_stockdetail.drawQuote(item.name,this.graphName);
        
    if (isflush==null)
		$('#'+this.tagdetailname).modal({position:30,show: true});  
}

Stock.prototype.findLastQuote = function(stockid)
{
	var qdata = store.get(this.quotename);
	var quotes = qdata[stockid];
	if (quotes!=null)
		return quotes[quotes.length-1];
	else
		return {price:0};
}

Stock.prototype.findQuotes = function(stockid)
{
	var qdata = store.get(this.quotename);
	var quote = qdata[stockid];
	return quote;
}

//取行情:未調用
Stock.prototype.syncData33 = function(){

	var lastquotes ;
	try  {
		var dataobj = $.ajax({type:"post",url:"/cf/stock_lastquote.do",async:false});
		if (dataobj!=null&&dataobj.responseText.length>0) {
			quotes = eval ("(" + dataobj.responseText + ")");
		}
	}   catch  (e)   {
	    document.write(e.name);
	} 
	if (quotes!=null){
		var qdata = store.get(this.quotename);
		for (var i=0;i<lastquotes.size;i++){
			var quotes = qdata[lastquotes[i].stockid];
			if (quotes!=null){
				quotes.shift();
				quotes.push(lastquotes[i]);
			}
		}
	}
}

var g_stock = new Stock();
 g_stock.init();
