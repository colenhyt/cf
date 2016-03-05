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
	this.dataLoaded = false;
    this.data = {};
	this.reloadCount = 0;
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
}

Stock.prototype.loadPlayerLastQuote = function()
{
	if (g_stock.isOpen) return;

	var stockids = g_player.stockids;
	if (!stockids||stockids.length<=0) return;
	
	var jids = "stockids="+JSON.stringify(stockids);
	try  {
		$.ajax({type:"post",url:"/cf/stock_pagelastquotes.do",data:jids,success:function(data){
			var lastquotes = cfeval(data);
			var prelastquotes = g_stock.playerlastquotes
			g_stock.playerlastquotes = lastquotes;
			if (!prelastquotes)return;
			
			for (itemid in lastquotes){
				var prelastps = ForDight(prelastquotes[itemid]);
				var lastps = ForDight(lastquotes[itemid]);
				if (prelastps!=lastps){
					g_stock.hasTip = true
				}
			}
		}});
	}   catch  (e)   {
	    logerr(e.name);
	    return;
	} 
}

Stock.prototype.loadPageLastQuote = function()
{
	
	var stockids = g_stock.currPageIds;
	if (!stockids) return;
	
	g_msg.showload("g_stock.loadPageLastQuote");
	
	var jids = "stockids="+JSON.stringify(stockids);
	try  {
		$.ajax({type:"post",url:"/cf/stock_pagelastquotes.do",data:jids,success:function(data){
	 		g_msg.destroyload();
			var lastquotes = cfeval(data);
			g_stock.lastquotes = lastquotes;
			//当前页股票属性变化:
			var pname = g_stock.pagequote+g_stock.currPage;
			store.set(pname,lastquotes);
			for (itemid in lastquotes){
				var lastps = ForDight(lastquotes[itemid]);
				//当前股票最新价格修改:
				var tagps = document.getElementById(g_stock.name+"_ps"+itemid);
				if (!tagps) continue;
				
				tagps.innerHTML = lastps;
				
				var pitem = g_player.getStockItem(itemid);
				if (pitem.qty<=0)continue;
				//玩家股票盈亏变化:				
				var pr = parseInt(pitem.qty*lastps - pitem.amount);
				var tagpr = document.getElementById(g_stock.name+"_pr"+itemid);
				tagpr.innerHTML = pr;
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
	var countNeed = 16;
	
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
	var others = randomItems(data,countNeed-ids.length);
	for (var i=0;i<others.length;i++){
	 if (ids.length>=16) break;
	 if (others[i]!=null)
	   ids.push(others[i]);
	}
	return ids;
}

Stock.prototype.show = function(){
	playAudioHandler('open1');	
	this.hasTip = false;
	
	if (g_player.isopenstock()!=1){
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
	g_player.setOpenstock();
	g_msg.hide();
	g_msg.tip("证券账户已开通");
	this.show();
	
	g_player.commitData(2,0,0);		
}

Stock.prototype.buildPage = function(page)
{
	if (page<0)	return
	
	if (this.dataLoaded==false&&this.reloadCount<3)
	{
		this.hide();
		g_login.syncLoadData_stock(g_player.data.playerid,1);
		this.reloadCount++;
		return;
	}
		
	playAudioHandler('open1');	
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
			 content += "<br>距离下次行情跳动还有: <span id='"+this.name+"_quotetime' style='color:yellow'></span>"
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
	playAudioHandler('open1');	
	
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
   this.waitCount = 0;
   var strPro = "尚未持有";
	currentquotes = store.get(g_stock.pagequote+g_stock.currPage);   
 	var quote = currentquotes[id];
	var ps = 0;
 	if (quote!=null) {
		ps = ForDight(quote);
	}
   if (ps<=0){
   		g_msg.tip("行情尚未获取，请重新打开交易所重新加载!");	
   		return;    
   }
   	
   var pitem = g_player.getStockItem(id);
   pitem.buyps = "";
	if (pitem.qty>0){
		profit = ps*pitem.qty - pitem.amount;
		var psColor = "red";
		if (profit<0)
			psColor = "green"   
		pitem.buyps = "("+ForDight(pitem.amount/pitem.qty)+")";
   	 strPro = "盈亏:<span style='color:"+psColor+"'>"+parseInt(profit)+"</span>";
   }
   
    var amount = 0;
    if (g_player.saving!=null&&g_player.saving[1]!=null)
    	amount = g_player.saving[1].amount;
    var canBuyQty = parseInt(amount/(ps*100));
    var sizePer = 100;
    if (item.descs.length>18)
     sizePer = 80;
	var content =      "        <div class='cfpanel_text'><div class='cpgapedetail_h2'>"+item.name
	content += "<span class='cfpanel_text right'>"+strPro+" </span>"
	content += "</div>"
	content += "<div class='cf_line stockdetail'></div>"
	 content += "<div style='font-size:"+sizePer+"%'> "+item.descs+"</div>"
	content += "<div class='cf_line stockdetail'></div>"
	 content +=	"</div>"
	content += "<div id='"+this.graphName +"'></div>"
	 content += "           <div class='cfstock_content'>  "
	 content += "        <table id='toplist1_tab'>"
	 content += "             <tr>"
	 content += "               <td>买入价</td>"
	 content += "               <td>"+ps+"</td>"
	 content += "               <td>持有:</td>"
	 content += "               <td>"+pitem.qty/100+"手"+pitem.buyps+"</td>"
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
	content += "           <div class='cf_stockdetail_btn'>  "
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
		$('#'+this.tagdetailname).modal({position:0,show: true});  
}

Stock.prototype.closeDetail = function(id){ 
	playAudioHandler('close1');	
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
		
   if (price<=0){
   		g_msg.tip("价格不能为零，购买失败!");	
   		return;    
   }
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
 
 if (id){
  g_stock.buyItem = {itemid:id,playerid:g_player.data.playerid,
			qty:qty,price:ps,amount:parseInt(ps*qty)};  
 }
 
 if (!g_stock.buyItem) return false; 
  g_msg.showload("g_stock.requestBuy",true);
 
	var dataParam = "type=3&playerid="+g_stock.buyItem.playerid+"&itemid="+g_stock.buyItem.itemid;
	var amount = g_stock.buyItem.amount;
	dataParam += "&qty="+g_stock.buyItem.qty+"&price="+g_stock.buyItem.price+"&amount="+amount;
	var sessionid = g_player.getSession(g_player.data.playerid);
  	if (sessionid!=null)
  		dataParam += "&sessionid="+sessionid;

	try    {
		$.ajax({type:"post",url:"/cf/data_update.jsp",data:dataParam,success:function(data){
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
	 if (ret.desc)
	 {
	 	g_player.resetSession(ret.desc);
	 }
     return;
    }

   var buyitem = this.buyItem;
	var id = buyitem.itemid;
	var qty = buyitem.qty;
   var item = store.get(this.name)[id];
   if (item==null) return;
   
   var stockback = cfeval(ret.desc);
   
	g_player.updateData({"cash":stockback.liveamount});
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
  playAudioHandler('money');	
	
		
	var stockback = cfeval(ret.desc);
			
	g_player.resetSession(stockback.sessionid);
	g_player.updateData({"cash":stockback.liveamount});
	g_quest.onBuyItem(this.name,item,qty);
				   
	//tip:
	if (qty>0)
		g_msg.tip("购买<span style='color:red'>"+item.name+"</span>成功,金额:"+buyitem.amount);
	else
		g_msg.tip("抛售<span style='color:red'>"+item.name+"</span>成功,金额:"+(0-buyitem.amount));
	
	//卖出利润:
   if (qty<0&&stockback.profit!=0){
		var desc;
    	if (stockback.profit>0){
    		desc = "卖出收益: "+ ForDight(stockback.profit);
    	}else
    		desc = "卖出亏损: "+ ForDight((0-stockback.profit));
		g_msg.tip(desc);
   }
	
	$('#'+this.tagdetailname).modal("hide");  
	
	 g_player.flushPageview();
	
	//this.showDetail(id,true);
	//刷新list 页面:
	this.buildPage(0);
}

Stock.prototype.findQuotes = function()
{
	var stockid = g_stock.currShowStockId;
	if (!stockid) return;

	var qdatas = store.get(this.quotename);
	var squotes = qdatas[stockid];
	
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

Stock.prototype.onClose = function()
{
	var tag = document.getElementById("tag"+this.name);
	if (tag){
		tag.innerHTML = ""
	}	
	this.isOpen = false;
	
	playAudioHandler('close1');	
	$('#'+g_msg.tagname).modal('hide'); 
	//alert(this.name+"close");
}

Stock.prototype.findQuoteLostTime = function(){
	if (!g_player.data||!g_player.data.lastlogin) return 0;
	
	var now = Date.parse(new Date());
	var enterTime = (now - g_player.data.lastlogin)/1000;		//进入系统时间(秒);
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
		//股票涨跌图标提示:
		if (!this.isOpen){
			this.loadPlayerLastQuote();
		}
		rebuild = true;
	}

	if (!this.isOpen||!this.isStockOpen()) return;
	
	var tag = document.getElementById(this.name+"_quotetime");
	var min = parseInt(lsec/60);
	var sec = lsec%60;
	//行情消逝时间:
	if (sec<10)
		sec = "0"+sec;
	if (min>0)
	tag.innerHTML = "0"+min+"分"+sec+"秒";
	else
	tag.innerHTML = sec+"秒";
	if (rebuild){
		this.loadPageLastQuote();
	}
}

//page打开才执行:
Stock.prototype.update = function(){
	var tagid = "tag2"+this.name
	var tag=document.getElementById(tagid);
	if (!this.hasTip||!g_game.enter) {
		if (tag){
			document.body.removeChild(tag);
		}
		return;
	}
	
	if (!tag){
        var div = document.createElement("DIV");
        div.id = tagid;
        div.className = "cfpage_text "+this.name;
        document.body.appendChild(div);	  	
        tag = document.getElementById(tagid);
	}	
		
	var tip = tag.innerHTML;
	var index = tip.indexOf("stocktip2");
	if (index>0)
		tag.innerHTML = "<div class='cfpage_text stocktip' onclick='g_stock.show()'></div>"
	else
		tag.innerHTML = "<div class='cfpage_text stocktip2' onclick='g_stock.show()'></div>"
}

var g_stock = new Stock();

