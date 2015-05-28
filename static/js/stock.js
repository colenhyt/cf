Stock = function(){
    this.name = "stock";
    this.quotename = "quote";
    this.pagequote = "pageq";
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
 	var tquotedata = store.get(this.quotename);
 	if (!tquotedata){
    	store.set(this.quotename,{});
 	}
	
    this.buildHTML();
    
	setInterval(function(){
	   g_stock.update_self();
	  },StockDuration
	);	    
}

Stock.prototype.onEnter = function(){
	this.stockChange();
}

Stock.prototype.loadPageLastQuote = function()
{
	g_msg.showload("g_stock.loadPageLastQuote");
	
	if (!g_stock.currPageIds) return;
	
	var jids = "stockids="+JSON.stringify(g_stock.currPageIds);
	try  {
		$.ajax({type:"post",url:"/cf/stock_pagelastquotes.do",data:jids,success:function(data){
			var lastquotes = cfeval(data);
			g_stock.lastquotes = lastquotes;
			var pname = g_stock.pagequote+g_stock.currPage;
			//g_msg.tip("load back save quote:"+pname);
			store.set(pname,lastquotes);
			for (itemid in lastquotes){
				var tagps = document.getElementById(g_stock.name+"_ps"+itemid);
				if (!tagps) continue;
				
				var lastps = ForDight(lastquotes[itemid]);
				tagps.innerHTML = lastps;
				var pitem = g_player.getStockItem(itemid);
				if (pitem.qty<=0)continue;
				
				var tagpr = document.getElementById(g_stock.name+"_pr"+itemid);
				var pr = pitem.qty*lastps - pitem.amount;
				tagpr.innerHTML = parseInt(pr);
				if (pr>0)
					tagpr.style.color = "red";
				else
					tagpr.style.color = "green";
			}
			g_msg.destroyload();
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
	playAudio('open.wav');	
	if (g_player.data.openstock!=1){
		g_msg.open2("证券开户","您需要开通证券账户才能投资股票，请点击'确认'按钮开通","g_stock.confirmOpen",1,1,1,null,"g_stock.onClose");
	}else {
		var myDate = new Date();
		var ss = myDate.getSeconds(); 
		var ms = myDate.getMilliseconds();		
		this.buildPage(0);
		this.isOpen = true;
        $('#'+this.tagname).modal({position:getSizes().PageTop,show: true});    
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
	
	playAudio('open.wav');	
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
		var pageps = store.get(this.pagequote+page);
		if (!pageps){
			pageps = {};
		}
		for (var i=start;i<end;i++){
			var item = tdata[rids[i]];
			if (!item)continue;
			var itemid = rids[i];
			pageids.push(itemid);
			var pitem = g_player.getStockItem(itemid);
			var ps = 0;
			var profit = 0;
			var psColor = "none";
			if (pageps[itemid]){
				ps = ForDight(pageps[itemid]);
				if (pitem.qty>0){
				profit = parseInt(pitem.qty*ps - pitem.amount);
				if (profit>0)
					psColor = "red"  	
				else
				  psColor = "green" 	
				}		
				//g_msg.tip('取本地:')
			}
		     content += "<div class='cfpanel' ID='"+this.name+"_d"+itemid+"' onclick='g_stock.showDetail("+itemid+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
		     content += "<span class='cfpanel_text right'>目前持有<span style='color:yellow'> "+pitem.qty/100+"</span> 手</span>"
			 content += "	<div>" 
			 content += "<span class='cfpanel_text'>当前价格: <img class='cficon_money' src='static/img/money.png'/>"
			 content += "<span id='"+this.name+"_ps"+itemid+"'>"+ps+"</span></span>"
			 content += "<span class='cfpanel_text right'>总盈亏: "
			 content += "<span id='"+this.name+"_pr"+itemid+"' style='color:"+psColor+"'>"+profit+"</span></span>"
			content += "     </div>"
      		content += "</div>"
		}
			content += "           <div class='cfinsure_tip'>  "
			content += "          股市开市为9:00AM-9:00PM"
			if (this.isStockOpen()==true){
			 content += "<br>下次行情跳动: <span id='"+this.name+"_quotetime' style='color:yellow'></span>"
			}  
			content += "             </div>"
     		content += "</div>"
		
		 
		this.currPage = page;
        content += this.buildPaging(page,rids.length);
        this.currPageIds = pageids;
        this.loadPageLastQuote();
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
}

Stock.prototype.showDetail = function(id,isflush){    
	this.onPanelClick(id);
	playAudio('open.wav');	
	
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
   this.waitCount = 0;
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
   	 strPro = "盈亏:<span style='color:"+psColor+"'>"+parseInt(profit)+"</span>";
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
	 content += "               <td><span id='stockTradeCount' style='color:red'>0</span> 手</td>"
	 content += "               <td>可买入:</td>"
	 content += "               <td><span id='canBuyCount'>"+canBuyQty+"</span>手</td>"
	content += "              </tr>"
	 content += "             <tr>"
	 content += "               <td colspan='2'><input type='button' class='cf_bt_green' value='卖出100股' onclick='g_stock.countBuy("+id+",-100,"+ps+")'></td>"
	 content += "               <td colspan='2'><input type='button' class='cf_bt_green right' value='买入100股' onclick='g_stock.countBuy("+id+",100,"+ps+")'></td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "
	content += "           <div style='align:center'>  "
	content += "          <button class='cf_bt bt_cancel' onclick='g_stock.closeDetail()'>退出</button>      "  
	content += "          <button class='cf_bt' onclick='g_stock.doBuy()'>确定</button>"
	content += "             </div>"
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
	
	this.currShowStockId = id;
	this.currStockPs = ps;
	
	var quotes = this.findQuotes();
	if (quotes)
    	g_stockdetail.drawQuote(id,ps,quotes,this.graphName);
        
    if (isflush==null)
		$('#'+this.tagdetailname).modal({position:5,show: true});  
}

Stock.prototype.closeDetail = function(id){ 
	playAudio('close.wav');	
	$('#'+this.tagdetailname).modal('hide');  
}

Stock.prototype.isStockOpen = function()
{
	var open = false;
	var now = new Date();
	if (now.getHours()>=9&&now.getHours()<21)
		open = true;
	return open;
}

Stock.prototype.countBuy = function(stockid,count,price)
{
	this.waitStockid = stockid;
	if (this.waitCount==null)
		this.waitCount = 0;
		
   var pitem = g_player.getStockItem(stockid);
   var count2 = this.waitCount+ count;
   if (count2<0&&pitem.qty<(0-count2)){
   		g_msg.tip("卖出数量不能大于您持有数量!");	
   		return;
   }
   		
	this.waitCount += count;
	this.buyPrice = price;
	var tag = document.getElementById('stockTradeCount');
	tag.innerHTML = this.waitCount/100;
//	tag = document.getElementById('canBuyCount');
//	tag.innerHTML = this.waitCount/100;
	
}

Stock.prototype.doBuy = function()
{
	if (!this.isStockOpen()){
		g_msg.tip("现在休市，不能交易");
		return;
	}	
	if (this.waitCount==null||this.waitCount==0){
		g_msg.tip("请买入或者卖出");
		return;
	}
	
   var item = store.get(this.name)[this.waitStockid];
   if (!item){
   		g_msg.tip("找不到"+this.name+"数据");
   		return;
   }
	   
   var ps = this.buyPrice;
    
   if (this.waitCount>0){
	    var needCash = ps * this.waitCount;
	    var cash = g_player.saving[1].amount;
	    if (cash<needCash){
		    g_msg.tip("您的现金不够，购买失败!");
		    return;
	    }		
    }else {
	   var pitem = g_player.getStockItem(this.waitStockid);
	   if (pitem.qty<(0-this.waitCount)){
	   		g_msg.tip("持有数量少于您抛售数量!");	
	   		return;
	   }
    }
	
	//alert(this.waitCount);
	this.requestBuy(this.waitStockid,this.waitCount,this.buyPrice);
}

Stock.prototype.requestBuy = function(id,qty,ps) {
 g_msg.showload("g_stock.requestBuy",true);
 
 if (id){
  g_stock.buyItem = {itemid:id,playerid:g_player.data.playerid,
			qty:qty,price:ps,amount:parseInt(ps*qty)};  
 }
 
 if (!g_stock.buyItem) return false; 
 
	var dataParam = obj2ParamStr(g_stock.name,g_stock.buyItem);
	try    {
		$.ajax({type:"post",url:"/cf/stock_add.do",data:dataParam,success:function(data){
		 g_stock.buyCallback(cfeval(data));
         g_msg.destroyload();
		}});
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   return false;
	}	 
}

	
Stock.prototype.buyCallback = function(ret){
    if (ret.code) {
     g_msg.tip("操作失败:"+ERR_MSG[ret.code]);
     return;
    }

   var buyitem = this.buyItem;
	var id = buyitem.itemid;
	var qty = buyitem.qty;
   var item = store.get(this.name)[id];
   if (item==null) return;
   
   var amount = buyitem.amount;
   
   var pitems = g_player.getData(this.name);
	if (!pitems[id])
		pitems[id] = [];
	if (qty>0){
	 pitems[id].push(buyitem);
	}else {
	 var needRemoveQty = 0 - qty;
	 for (var i=0;i<pitems[id].length;i++){
	   if (pitems[id][i].qty<=needRemoveQty){
	    needRemoveQty -= pitems[id][i].qty;
	    pitems[id].splice(i,1);
	    i--;
	   }else {
	    pitems[id][i].qty -= needRemoveQty;
	    pitems[id][i].amount = pitems[id][i].qty*pitems[id][i].price;
	    break;
	   }
	 }
	}
	g_player.setStockIds();
  playAudio('money.wav');	
	
		
	var cash = g_player.saving[1].amount;	   
	cash -= amount;
	g_player.updateData({"cash":cash});
	g_quest.onBuyItem(this.name,item,qty);
				   
	//tip:
	if (qty>0)
		g_msg.tip("购买<span style='color:red'>"+item.name+"</span>成功,金额:"+buyitem.amount);
	else
		g_msg.tip("抛售<span style='color:red'>"+item.name+"</span>成功,金额:"+(0-buyitem.amount));
	
	$('#'+this.tagdetailname).modal("hide");  
	
	//this.showDetail(id,true);
	//刷新list 页面:
	this.buildPage(0);
}

Stock.prototype.findQuotes = function()
{
	var stockid = g_stock.currShowStockId;
	if (!stockid) return;

	var lsec = g_stock.findQuoteLostTime();
	var islocal = Math.abs(lsec-QUOTETIME)<QUOTETIME/3;
	var qdatas = store.get(this.quotename);
	var squotes = qdatas[stockid];
//	if (islocal&&squotes){
//		return squotes;
//	}
	
	g_msg.showload("g_stock.findQuotes");

	try  {
		$.ajax({type:"post",url:"/cf/stock_quotes.do",data:"stock.id="+stockid,success:function(dataobj){
			squotes =cfeval(dataobj);
			qdatas[stockid] = squotes;	
			store.set(g_stock.quotename,qdatas);
			g_stockdetail.drawQuote(stockid,g_stock.currStockPs,squotes,g_stock.graphName);
			g_stock.currShowStockId = null;
			g_msg.destroyload();
		}});

	}   catch  (e)   {
	    logerr(e.name);
	} 
}

Stock.prototype.playerStockQuoteCallback = function(data)
{
}

Stock.prototype.queryStockLastQuote = function(stockids,callback)
{
	var jids = "stockids="+JSON.stringify(stockids);
	try  {
		$.ajax({type:"post",url:"/cf/stock_pagelastquotes.do",data:jids,success:callback});
	}   catch  (e)   {
	    logerr(e.name);
	    return;
	} 
}

Stock.prototype.stockChange = function()
{
	if (g_player.stockids.length<=0) return;
	
	var jids = "stockids="+JSON.stringify(g_player.stockids);
	try  {
		$.ajax({type:"post",url:"/cf/stock_pagelastquotes.do",data:jids,success:function(data){
			var lastquotes = cfeval(data);
			g_stock.lastquotes = lastquotes;
			var floatAmounts = {};
			var closeIds = {};
			var prs = 0;
			for (itemid in lastquotes){
				var pitem = g_player.getStockItem(itemid);
				if (pitem.qty<=0) continue;
				var newps = ForDight(lastquotes[itemid]);
				floatAmounts[itemid] = pitem.qty*newps; 
				prs += pitem.qty*newps - pitem.amount;
				//g_player.setStockItemPs(itemid,newps);
				this.hasTip = true;
			}
			for (key in closeIds)
			{
				g_player.buyItem(g_stock.name,key,closeIds[key].qty,closeIds[key].price);
			}
			//g_msg.tip("股票资产盈亏变化:"+parseInt(prs));
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
	
	playAudio('close.wav');	
	$('#'+g_msg.tagname).modal('hide'); 
	//alert(this.name+"close");
}

Stock.prototype.findQuoteLostTime = function(){
	if (!g_player.data||!g_player.data.lastlogin) return 0;
	
	var now = Date.parse(new Date());
	var enterTime = (now - g_player.data.lastlogin.time)/1000;		//进入系统时间(秒);
	var quotePassTime = parseInt(enterTime+g_player.data.quotetime);
	var mm = quotePassTime%QUOTETIME;
	var lsec = QUOTETIME - mm;
	return lsec;
}

//股票更新:
Stock.prototype.update_self = function(){
	if (!g_player.data||!g_player.data.lastlogin) return;
	
	var lsec = this.findQuoteLostTime();
	var rebuild = false
	if (lsec==QUOTETIME){
		this.stockChange();	//股票涨跌获取通知:
		rebuild = true;
	}

	if (!this.isOpen||!this.isStockOpen()) return;
	
	var tag = document.getElementById(this.name+"_quotetime");
	var min = parseInt(lsec/60);
	var sec = lsec%60;
	//行情消逝时间:
	if (sec<10)
		sec = "0"+sec;
	tag.innerHTML = "0"+min+":"+sec;
	if (rebuild){
		this.loadPageLastQuote();
	}
}

//page打开才执行:
Stock.prototype.update = function(){
	if (!this.hasTip||!g_game.enter) return;
	
		var tag = document.getElementById("tag2"+this.name);
		if (!tag){
	        var div = document.createElement("DIV");
	        div.id = "tag2"+this.name;
	        div.className = "cfpage_text "+this.name;
	        document.body.appendChild(div);	  	
	        tag = document.getElementById("tag2"+this.name);
		}	
			
		var tip = tag.innerHTML;
		var index = tip.indexOf("stocktip2");
		if (index>0){
			tag.innerHTML = "<img src='static/img/tip_stock.png' class='cfpage_text stocktip' onclick='g_stock.show()'>"
		}else
			tag.innerHTML = "<img src='static/img/tip_stock.png' class='cfpage_text stocktip2' onclick='g_stock.show()'>"
}

var g_stock = new Stock();
 g_stock.init();
