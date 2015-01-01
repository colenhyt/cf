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
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
    var pitem = g_player.getSavingItem(id);
    var content;
    if (pitem.amount>0){
    	//取款:
    	content = this.outContent(id,item,pitem.amount);
    }else {
    	//存款:
    	content = this.inContent(id,item);
    }
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
	$('#'+this.tagdetailname).modal({position:PageDetail_Top,show: true});
}

Saving.prototype.inContent = function(id,item){
    var dftAmount = 1000;
    var dftProfit = ForDight(dftAmount * (item.rate/100));
	var content =      "        <div style='margin-top:-10px;text-align:center;'>"
 content += "        <div><span class='cpgapedetail_h2 left'>"+item.name+"存款</span>"
	content += "<span style='float:right;color:green;font-size:30px'>存款 </span></div>"
	content += "<img src='static/img/pop_line.png' class='cf_line'>"
 content += "           <div class='cfmsg_text saving'>  "
 content += "        <table id='toplist1_tab' style='text-align:left;line-height:60px'>"
 content += "             <tr>"
 content += "               <td colspan='4'>利率: "+item.rate+"%</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td>存入:</td>"
 content += "               <td><input type='button' class='cf_count' onclick='g_saving.countBuy("+id+",-1000)'></td>"
 content += "               <td><input type='text' id='saving_amount' onBlur='' value='1000' style='width:160px;text-align:center;height:58px;font-size:30px'></td>"
 content += "               <td><input type='button' class='cf_count add' onclick='g_saving.countBuy("+id+",1000)'></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td colspan='4'>预计利息: ￥<span id='savingprofit'>"+dftProfit+"</span></td>"
content += "              </tr>"
content += "          </table>     "
		content += "             </div>"
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
	content += "          <button class='cf_bt' onclick='g_saving.buy("+id+")'>确认</button>"
content += "             </div>"
 
 	return content;
}

Saving.prototype.outContent = function(id,item,amount){
   var dftProfit = ForDight(amount * (item.rate/100));
	var content =      "        <div style='margin-top:-10px;text-align:center;'>"
 content += "        <div><span class='cpgapedetail_h2 left'>"+item.name+"取款</span>"
	content += "<span style='float:right;color:red;font-size:30px'>取款 </span></div>"
	content += "<img src='static/img/pop_line.png' class='cf_line'>"
 content += "           <div class='cfmsg_text saving'>  "
 content += "        <table id='toplist1_tab' style='text-align:left;line-height:60px'>"
 content += "             <tr>"
 content += "               <td>利率: "+item.rate+"%</td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td>当前存入: <span id='savingprofit'>"+amount+"</span></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td>预计利息: <span id='savingprofit'>"+dftProfit+"</span></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td><span style='color:red'>取出定期存款，将无法获得利息收益</span></td>"
content += "              </tr>"
content += "          </table>     "
		content += "             </div>"
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
	content += "          <button class='cf_bt' onclick='g_saving.buy("+id+")'>取出</button>"
content += "             </div>"
 	return content;
 	
 }

Saving.prototype.countBuy = function(id,count){
    var tag = document.getElementById('saving_amount');
    var currCount = parseInt(tag.value);
    if (currCount+count<=0) {
	currCount = 1;
    }else
	currCount += count;
    tag.value = currCount;
    
	var tdata = store.get(this.name);
   var item = this.findItem(id);
   if (item==null) return;
    
    var tag2 = document.getElementById('savingprofit');
    tag2.innerHTML = ForDight(tag.value * (item.rate/100));
    //alert(tag.value);
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
    }else 
    	amount = 0 - pitem.amount;
    	
	var ret = g_player.buyItem(this.name,id,1,amount);
    if (ret.ret==true){
    	//tip:
		var desc;
		if (amount>0)
			desc = "存入<span style='color:red'>"+item.name+"</span>成功,金额:"+amount;
		else 
			desc = "取出<span style='color:red'>"+item.name+"</span>成功,金额:"+(0-amount);
			
    	g_msg.tip(desc);
    	
    	this.hide(this.tagdetailname);
    	//刷新list 页面:
    	g_bank.buildPage(g_bank.currPage);
    	//$('#'+this.tagdetailname).modal('hide');
    }    

}

var g_saving = new Saving();
 g_saving.init();