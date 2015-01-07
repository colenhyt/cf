Stock = function(){
    this.name = "stock";
    this.quotename = "quote";
    this.cname = "股票";
    this.pageCount = 4;
    this.currPage = 0;
    this.lastquotes = {};
    this.syncDuration = 1;
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

Stock.prototype.onEnter = function(){
	this.stockChange();
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

Stock.prototype.loadPageLastQuote = function(ids)
{
	var jids = "stockids="+JSON.stringify(ids);
	try  {
		$.ajax({type:"post",url:"/cf/stock_pagelastquotes.do",data:jids,success:function(data){
			var lastquotes = cfeval(data);
			g_stock.lastquotes = lastquotes;
			for (itemid in lastquotes){
				var pitem = g_player.getStockItem(itemid);
				var quote = ForDight(lastquotes[itemid]);
				var tagps = document.getElementById(g_stock.name+"_ps"+itemid);
				tagps.innerHTML = quote;
				if (pitem.qty<=00)continue;
				
				var tagpr = document.getElementById(g_stock.name+"_pr"+itemid);
				var pr = pitem.qty*quote - pitem.amount;
				tagpr.innerHTML = ForDight(pr);
				if (pr>0)
					tagpr.style.color = "red";
				else
					tagpr.style.color = "green";
			}
		}});
	}   catch  (e)   {
	    logerr(e.name);
	    return;
	} 
}

Stock.prototype.loadNextQuoteTime = function(ids)
{
	var quatetime;
	try  {
		var dataobj = $.ajax({type:"post",url:"/cf/stock_nextquotetime.do",async:false});
		if (dataobj!=null&&dataobj.responseText.length>0) {
			quatetime = parseInt(dataobj.responseText);
		}
	}   catch  (e)   {
	    logerr(e.name);
	    return;
	} 
	return quatetime;

}

Stock.prototype.findStockIds = function()
{
	var countNeed = 15;
	
	var tdata = store.get(this.name);
	var data = [];
	var ids = g_player.stockids.concat();
	for (itemid in tdata){
		var found = false;
		for (var i=0;i<ids.length;i++){
			if (itemid==ids[i]){
				found = true;
				break;
			}
		}
		if (!found){
			data.push(itemid);
		}
	}
	var others = randomItems(data,null,countNeed-ids.length);
	return ids.concat(others);
}

Stock.prototype.show = function(){
	if (g_player.data.openstock!=1){
		g_msg.open2("证券开户","您需要开通证券账户才能投资股票，请点击'确认'按钮开通","g_stock.confirmOpen",1,1,1,null,"g_stock.onClose");
	}else {
		var myDate = new Date();
		var ss = myDate.getSeconds(); 
		var ms = myDate.getMilliseconds();		
		this.buildPage(0);
		this.isOpen = true;
		this.hasTip = false;
        $('#'+this.tagname).modal({position:Page_Top,show: true});    
        var myDate22 = new Date();
		var ss2 = myDate22.getSeconds(); 
		var ms2 = myDate22.getMilliseconds();
		//g_msg.tip("cost: "+((ss2-ss)*1000+(ms2-ms)));    
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
		var loadFromServer = false;
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		var rids = this.findStockIds();		//35ms
		if (end>rids.length)
			end = rids.length;
		  content += "<div class='cfpanel_body'>"
		  
		var pageids = []
		for (var i=start;i<end;i++){
			var item = tdata[rids[i]];
			var itemid = rids[i];
			pageids.push(itemid);
			var pitem = g_player.getStockItem(itemid);
		     content += "<div class='cfpanel' ID='"+this.name+"_d"+itemid+"' onclick='g_stock.showDetail("+itemid+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
		     content += "<span class='cfpanel_text right'>目前持有<span style='color:yellow'> "+pitem.qty/100+"</span> 手</span>"
			 content += "	<div>" 
			 content += "<span class='cfpanel_text'>当前价格: ￥"
			 content += "<span id='"+this.name+"_ps"+itemid+"'>0</span></span>"
			 content += "<span class='cfpanel_text right'>总盈亏: "
			 content += "<span id='"+this.name+"_pr"+itemid+"'>0</span></span>"
			content += "     </div>"
      		content += "</div>"
		}
			content += "           <div class='cfinsure_tip'>  "
			content += "          股市开市为8:00AM-9:00PM<br>下次行情跳动: "
			content += "<span id='"+this.name+"_quotetime' style='color:yellow'></span>"  
			content += "             </div>"
     		content += "</div>"
		
		 
		this.currPage = page;
        content += this.buildPaging(page,rids.length);
        this.loadPageLastQuote(pageids);
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
}

Stock.prototype.showDetail = function(id,isflush){    
	this.onPanelClick(id);
	
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
   var strPro = "尚未持有";
 	var quote = this.lastquotes[id];
	var ps = 0;
 	if (quote!=null) {
		ps = ForDight(quote);
	}
	
   var pitem = g_player.getStockItem(id);
	if (pitem.qty>0){
		profit = ps*pitem.qty - pitem.amount;
		var psColor = "red";
		if (profit<0)
			psColor = "green"   
   	 strPro = "盈亏:<span style='color:"+psColor+"'>"+ForDight(profit)+"</span>";
   }
   
    var amount = g_player.saving[1].amount;
    var canBuyQty = parseInt(amount/(ps*100));
	var content =      "        <div class='cfpanel_text'><div class='cpgapedetail_h2'>"+item.name
	content += "<span class='cfpanel_text right'>"+strPro+" </span>"
	content += "</div>"
	content += "<img src='static/img/pop_line.png' class='cf_line'>"
	 content += "<div> "+item.descs+"</div>"
	content += "<img src='static/img/pop_line.png' class='cf_line'>"
	 content +=	"</div>"
	content += "<div id='"+this.graphName +"'></div>"
	 content += "           <div class='cfstock_content'>  "
	 content += "        <table id='toplist1_tab'>"
	 content += "             <tr>"
	 content += "               <td>买入价</td>"
	 content += "               <td>"+ps+"</td>"
	 content += "               <td>持有:</td>"
	 content += "               <td>"+pitem.qty/100+" 手</td>"
	content += "              </tr>"
	 content += "             <tr>"
	 content += "               <td>买卖:</td>"
	 content += "               <td><span id='stockBudyCount' style='color:red'>0</span> 手</td>"
	 content += "               <td>可买入:</td>"
	 content += "               <td>"+canBuyQty+"手</td>"
	content += "              </tr>"
	 content += "             <tr>"
	 content += "               <td colspan='2'><input type='button' class='cf_bt_green' value='减持100股' onclick='g_stock.countBuy("+id+",-100,"+ps+")'></td>"
	 content += "               <td colspan='2'><input type='button' class='cf_bt_green right' value='增持100股' onclick='g_stock.countBuy("+id+",100,"+ps+")'></td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "
	content += "           <div style='align:center'>  "
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>退出</button>      "  
	content += "          <button class='cf_bt' onclick='g_stock.doBuy()'>确定</button>"
	content += "             </div>"
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
	
	var quotes = this.findQuotes(id,false,ps);
	if (quotes)
    	g_stockdetail.drawQuote(id,ps,quotes,this.graphName);
        
    if (isflush==null)
		$('#'+this.tagdetailname).modal({position:30,show: true});  
}

Stock.prototype.isStockOpen = function()
{
	var open = false;
	var now = new Date();
	if (now.getHours()>=9&&now.getHours()<=20)
		open = true;
	return open;
}

Stock.prototype.countBuy = function(stockid,count,price)
{
	this.waitStockid = stockid;
	if (this.waitCount==null)
		this.waitCount = 0;
	this.waitCount += count;
	this.buyPrice = price;
	var tag = document.getElementById('stockBudyCount');
	tag.innerHTML = this.waitCount/100;
}

Stock.prototype.doBuy = function()
{
	if (this.waitCount==null||this.waitCount==0){
		g_msg.tip("请减持或者增持");
		return;
	}
	if (!this.isStockOpen()){
		g_msg.tip("现在休市，不能交易");
		return;
	}	
	//alert(this.waitCount);
	this.confirmBuy(this.waitStockid,this.waitCount,this.buyPrice);
	this.waitCount = 0;
}

Stock.prototype.findQuotes = function(stockid,fromServer,price)
{
	var qdata = store.get(this.quotename);
	if (qdata==null){
		qdata = {};
		store.set(this.quotename,qdata);
	}
	
	var squotes = qdata[stockid];
	if (squotes!=null&&fromServer!=true){
	//alert('from local');
		return squotes;
	}
	
	try  {
		$.ajax({type:"post",url:"/cf/stock_quotes.do",data:"stock.id="+stockid,success:function(dataobj){
			squotes =cfeval(dataobj);
			var qdatas = store.get(g_stock.quotename);		
			qdatas[stockid] = squotes;		
			store.set(g_stock.quotename,qdatas);
			//alert('from server');
			g_stockdetail.drawQuote(stockid,price,squotes,g_stock.graphName);
		}});

	}   catch  (e)   {
	    logerr(e.name);
	} 
}

Stock.prototype.stockChange = function()
{
	if (g_player.stockids.length<=0||this.queryChange==true) return;
	
	this.queryChange = true;
	
	var jids = "stockids="+JSON.stringify(g_player.stockids);
	try  {
		$.ajax({type:"post",url:"/cf/stock_pagelastquotes.do",data:jids,success:function(data){
			var lastquotes = cfeval(data);
			g_stock.lastquotes = lastquotes;
			var floatAmounts = {};
			for (itemid in lastquotes){
				var pitem = g_player.getStockItem(itemid);
				var newps = lastquotes[itemid];
				floatAmounts[itemid] = pitem.qty*newps; 
				var pr = pitem.qty*newps - pitem.amount;
				//涨:
				if (pr>0)
				{
					this.hasTip = true;
					//alert("价格涨:"+itemid);
				}
			}
			g_player.currStockAmounts = floatAmounts;
			this.queryChange = false;
			//g_msg.tip("取得当前盈亏:"+floatAmounts);
		}});
	}   catch  (e)   {
	    logerr(e.name);
	    return;
	} 
}

Stock.prototype.onClose = function()
{
	var tag = document.getElementById("tag"+this.name);
	if (tag){
		tag.innerHTML = ""
	}	
	this.isOpen = false;
	
	$('#'+g_msg.tagname).modal('hide'); 
	//alert(this.name+"close");
}

//page打开才执行:
Stock.prototype.update = function(){
//this.hasTip = true;
	if (this.hasTip&&g_game.enter){
		var tag = document.getElementById("tag2"+this.name);
		if (!tag){
	        var div = document.createElement("DIV");
	        div.id = "tag2"+this.name;
	        div.className = "cfpage_text "+this.name;
	        document.body.appendChild(div);	  	
	        tag = document.getElementById("tag2"+this.name);
		}	
			
		var tip = tag.innerHTML;
		var index = tip.indexOf("tip2");
		if (index>0){
			tag.innerHTML = "<img src='static/img/tip_stock.png' class='cfpage_text tip'>"
		}else
			tag.innerHTML = "<img src='static/img/tip_stock.png' class='cfpage_text tip2'>"
	}
	
	if (!g_player.data||!g_player.data.lastlogin) return;
	
	var now = Date.parse(new Date());
	var enterTime = (now - g_player.data.lastlogin.time)/1000;		//进入系统时间(秒);
	var quotePassTime = parseInt(enterTime+g_player.data.quotetime);
	var mm = quotePassTime%QUOTETIME;
	var lsec = QUOTETIME - mm;
	var min = parseInt(lsec/60);
	var sec = lsec%60;
	if (lsec==QUOTETIME){
		this.stockChange();	//股票涨跌获取通知:
	}

	if (!this.isOpen) return;
	
	var tag = document.getElementById(this.name+"_quotetime");
	//行情消逝时间:
	if (sec<10)
		sec = "0"+sec;
	tag.innerHTML = "0"+min+":"+sec;
	if (lsec==QUOTETIME){
		store.remove(this.quotename);		//清空本地行情;
		this.buildPage(this.currPage);
	}
}

var g_stock = new Stock();
 g_stock.init();
