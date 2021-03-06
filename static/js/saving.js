Saving = function(){
    this.name = "saving";
    this.cname = "存款";
    this.pageCount = 5;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.data = {};
}

Saving.prototype = new Datamgr();

Saving.prototype.init = function(){
 store.remove(this.name)
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_savingdata);
	}     
    this.buildHTML();
}

Saving.prototype.showDetail = function(id){    
	playAudioHandler('open1');	
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
    var pitem = g_player.getSavingItem(id);
    var content;
    if (pitem.amount>0){
    	//取款:
    	content = this.outContent(id,item,pitem);
    }else {
    	//存款:
    	content = this.inContent(id,item);
    }
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
	$('#'+this.tagdetailname).modal({position:getSizes().DetailPageTop,show: true});
}

Saving.prototype.inContent = function(id,item){
    var dftAmount = 1000;
    var dftProfit = ForDight(dftAmount * (item.rate/100));
	var content =      "        <div class='cfsaving_div'>"
 content += "        <div><span class='cpgapedetail_h2 left'>"+item.name+"存款</span>"
  content += "<span class='cpgapedetail_h3'>(1年=现实24小时)</span>"
	content += "<span class='cfsaving_stext'>存款 </span></div>"
	content += "<img src='static/img/pop_line.png' class='cf_line'>"
 content += "           <div class='cfmsg_text'>  "
 content += "        <table id='toplist1_tab' class='cfsaving_content'>"
 content += "             <tr>"
 content += "               <td colspan='4'>利率: "+item.rate+"%</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td>存入:</td>"
 content += "               <td><input type='button' class='cf_count' onclick='g_saving.countBuy("+id+",-1000)'></td>"
 content += "               <td><input type='text' id='saving_amount' onBlur='' value='1000' class='cfsaving_qty'></td>"
 content += "               <td><input type='button' class='cf_count add' onclick='g_saving.countBuy("+id+",1000)'></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td colspan='4'>预计利息: <img class='cficon_money' src='static/img/money.png'/> <span id='savingprofit'>"+dftProfit+"</span></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td colspan='4'><div style='padding-top:30px;padding-left:40px;font-size:120%'>"
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
	content += "          <button class='cf_bt' onclick='g_saving.doBuy("+id+")'>存入</button>"
 content += "</div></td>"
content += "          </table>     "
		content += "             </div>"
content += "             </div>"
 
 	return content;
}

Saving.prototype.outContent = function(id,item,pitem){
   var dftProfit = ForDight(pitem.amount * (item.rate/100));
   var timeout = timeoutDesc(pitem,item);
   var savedesc = "取出定期存款，将无法获得利息收益";
 	if (timeout=="已到期"){
 	 savedesc = "已到期，请取出并获得本金和利息";
 	 timeout = "";
 	}   
	var content =      "        <div class='cfsaving_div'>"
 content += "        <div><span class='cpgapedetail_h2 left'>"+item.name+"取款</span>"
  content += "<span class='cpgapedetail_h3'>(1年=现实24小时)</span>"
	content += "<span class='cfsaving_stext out'>取款 </span></div>"
	content += "<img src='static/img/pop_line.png' class='cf_line'>"
 content += "           <div class='cfmsg_text saving'>  "
 content += "        <table id='toplist1_tab' class='cfsaving_content'>"
 content += "             <tr>"
 content += "               <td>利率: "+item.rate+"%</td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td>当前存入: <span id='savingprofit' style='color:red'>"+pitem.amount+"</span> "+timeout+"</td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td>预计利息: <span>"+dftProfit+"</span></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td><span style='color:red'>"+savedesc+"</span></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td><div style='padding-top:15px;padding-left:40px;font-size:120%'>"
	content += "          <button class='cf_bt bt_cancel' onclick='g_saving.closeDetail()'>取消</button>      "  
	content += "          <button class='cf_bt' onclick='g_saving.doBuy("+id+")'>取出</button>"
 content += "</div></td>"
content += "              </tr>"
content += "          </table>     "
		content += "             </div>"
content += "             </div>"
 	return content;
 	
 }

Saving.prototype.closeDetail = function(id){ 
	playAudioHandler('close1');	
	$('#'+this.tagdetailname).modal('hide');  
}

Saving.prototype.countBuy = function(id,count){
    var tag = document.getElementById('saving_amount');
    var currCount = parseInt(tag.value);
    if (currCount+count<=100) {
	 g_msg.tip("定期存款不能少于100")
	 return;
    }else
	currCount += count;
    tag.value = currCount;
    
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
    
    var tag2 = document.getElementById('savingprofit');
    tag2.innerHTML = ForDight(tag.value * (item.rate/100));
    //alert(tag.value);
}

Saving.prototype.doBuy = function(id){
   var item = store.get(this.name)[id];
   if (item==null) return;
   
   var pitem = g_player.getSavingItem(id);
   
    var amount = 0;
   //存钱:
   if (pitem.amount==0) {
	    var tag = document.getElementById('saving_amount');
	    amount = parseInt(tag.value);
	    if (amount<=100) {
		 g_msg.tip("定期存款不能少于100")
		 return;
	    }	    
    }else 
    	amount = 0 - pitem.amount;
    
    this.requestBuy(id,1,amount);
}
	
Saving.prototype.requestBuy = function(id,qty,amount){
	 if (id){
		g_saving.buyItem = {itemid:id,playerid:g_player.data.playerid,
			qty:qty,price:amount,amount:amount};
	 }

	 if (!g_saving.buyItem) return false;
	
	 var cash = g_player.saving[1].amount;
	 if (cash<g_saving.buyItem.amount){
		 g_msg.tip('您没有这么多钱存入');
		return;
	 }	
	 
 g_msg.showload("g_saving.requestBuy",true);
	 
	var dataParam = "type=1&playerid="+g_saving.buyItem.playerid+"&itemid="+g_saving.buyItem.itemid;
	dataParam += "&qty="+g_saving.buyItem.qty+"&price="+g_saving.buyItem.price+"&amount="+g_saving.buyItem.amount;
	var sessionid = g_player.getSession(g_player.data.playerid);
  	if (sessionid!=null)
  		dataParam += "&sessionid="+sessionid;

	try    {
		$.ajax({type:"post",url:"/cf/data_update.jsp",data:dataParam,success:function(data){
		 g_saving.buyCallback(cfeval(data));
	 	 g_msg.destroyload();
		}});
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   return false;
	}	
}
	
Saving.prototype.buyCallback = function(ret){
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
   var item = store.get(this.name)[id];
   if (item==null) return;
   
   var amount = buyitem.amount;
   
   var pitems = g_player.getData(this.name);
   if (amount>0){
	pitems[id] = buyitem;
	pitems[id].createtime = Date.parse(new Date());
	pitems[id].profit = 0;
   }else
	pitems[id] = null;
			   
	var savingback = cfeval(ret.desc);
	
  playAudioHandler('money');	
	var cash = savingback.liveamount;	   
	g_player.resetSession(savingback.sessionid);
	g_player.updateData({"cash":cash});
	if (savingback.amount>0)
		g_quest.onBuyItem(this.name,item,1);
				   
	//tip:
	var desc;
	if (amount>0)
		desc = "定期存入<span style='color:red'>"+item.name+"</span>成功,金额:"+amount;
	else {
		desc = "取出<span style='color:red'>"+item.name+"</span>成功,金额:"+(0-amount);
		g_bank.removeTip();
	}
		
	g_msg.tip(desc);
	
	if (savingback.profit>=1)
	{
 	 g_msg.tip("得到利息:"+savingback.profit);
	}
	 g_player.flushPageview();
	 
	this.hide(this.tagdetailname);
	//刷新list 页面:
	g_bank.buildPage(1);
}

Saving.prototype.buy = function(id){
   var item = store.get(this.name)[id];
   if (item==null) return;
   
   var pitem = g_player.getSavingItem(id);
   
    var amount = 0;
   //存钱:
   if (pitem.amount==0) {
	    var tag = document.getElementById('saving_amount');
	    amount = parseInt(tag.value);
	    if (amount<=100) {
		 g_msg.tip("定期存款不能少于100")
		 return;
	    }	    
    }else 
    	amount = 0 - pitem.amount;
    	
	var ret = g_player.buyItem(this.name,id,1,amount);
    if (ret.ret==true){
    	//tip:
		var desc;
		if (amount>0)
			desc = "定期存入<span style='color:red'>"+item.name+"</span>成功,金额:"+amount;
		else 
			desc = "取出<span style='color:red'>"+item.name+"</span>成功,金额:"+(0-amount);
			
    	g_msg.tip(desc);
    	
    	this.hide(this.tagdetailname);
    	//刷新list 页面:
    	g_bank.buildPage(1);
    	//$('#'+this.tagdetailname).modal('hide');
    }    

}

var g_saving = new Saving();
