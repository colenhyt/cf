Insure = function(){
    this.name = "insure";
    this.cname = "保险";
    this.pageCount = 4;
    this.count = 0;
    this.currPage = 0;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
	this.dataLoaded = false;
	this.reloadCount = 0;
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
    tagdetail = $('#'+this.pagedetailname)
    this.finalPageDetail = tagdetail.css("height")
    this.smallPageDetail = parseInt(this.finalPageDetail.substring(0,this.finalPageDetail.indexOf("px")))*0.8+"px"
}

Insure.prototype.onEnter = function(){
	//
	return;
	var pitems = g_player.insure;
	var data = store.get(this.name);
	var hasRisk = true;
	for (key in pitems){
		if (data[key]&&data[key].type==0){
			hasRisk = false;
			break;
		}
	}
	if (hasRisk){
		g_msg.open("您还没有购买任何保险，风险较高");
	}
}

Insure.prototype.findIds = function()
{
	var tdata = store.get(this.name);
	var otherdata = [];
	var ids = [];
	for (key in tdata){
		var pitem = g_player.getInsureItem(key);
		if (pitem.qty>0)
			ids.push(key);
		else {
			otherdata.push(key);
		}
	}
	
	return ids.concat(otherdata);
}

Insure.prototype.buildPage = function(page)
{
	if (page<0)
		return

	if (this.dataLoaded==false&&this.reloadCount<3)
	{
		this.hide();
		g_login.syncLoadData_saving(g_player.data.playerid,2);
		this.reloadCount++;
		return;
	}

	playAudioHandler('open1');	
		
	var tdata = store.get(this.name);
	var sids = this.findIds();
	var content = 	"";
	if (sids.length<=0){
		  content += "<div class='cfpanel' ID='insure_d1'><div class='cfpanel_body'>没有产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>sids.length)
			end = sids.length;
		  content += "<div class='cfpanel_body'>"
		for (var i=start;i<end;i++){
			var itemid = sids[i];
			var item = tdata[itemid];
			var pitem = g_player.getInsureItem(itemid);
			if (!pitem)
			 pitem = {amount:0,price:0,profit:0,qty:0};
			var buyDesc;
			if (item.type==0){
				if (pitem.qty<=0)
					buyDesc = "尚未购买";
				else
					buyDesc = "已购 <span style='color:yellow'>"+pitem.qty+"</span> 份";
			}else {
				var psColor = "green";
				if (pitem.profit<0)
					psColor = "red"
				if (pitem.qty<=0)
					buyDesc = "尚未购买";
				else {
					buyDesc = "已购 <span style='color:yellow'>"+pitem.qty+"</span>份,预计盈亏:<span style='color:red'>"+parseInt(item.profit*pitem.qty)+"</span>";
				}			
			}
			
		  content += "<div class='cfpanel' ID='"+this.name+"_d"+itemid+"' onclick='g_insure.clickDetail("+itemid+","+item.type+")'>"
		     if (item.name.length>=5&&pitem.qty>0)
		      content += "<span class='cfpanel_title small'>"+item.name+"</span>"
		     else
		      content += "<span class='cfpanel_title'>"+item.name+"</span>"
			 content += "<span class='cfpanel_text right'>"+buyDesc+"</span>"
			 content += "	<div>"
			 content += "<span class='cfpanel_text'>价格: <img class='cficon_money' src='static/img/money.png'/>"+ForDight(item.price)+"</span>"
			 if (pitem.qty<=0)
			 	content += "<span class='cfpanel_text right'>期限:"+item.period+"小时</span>"
			 else {
			 	var timeout = timeoutDesc(pitem,item,1);
			 	content += "<span class='cfpanel_text right'>"+timeout+"</span>"
			 }
			content += "     </div>"
      		content += "</div>"
		}
			content += "           <div class='cfinsure_tip'>  "
			content += "          保险分为规避风险类产品和理财投资类产品"  
			content += "             </div>"
   		content += "</div>"
        
		this.currPage = page;
        content += this.buildPaging(page,sids.length);
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
}

Insure.prototype.clickDetail = function(id,type){  
 	this.onPanelClick(id);
	playAudioHandler('open1');	
   if (type==0)
   	g_insure.show_insuredetail(id)
   else
   	g_insure.show_finandetail(id)
}

Insure.prototype.showDetail = function(title,desc,okCallback,itemid,qty,confmText,isInsure){
	var tagname = this.tagdetailname
	var detail = this.pagedetailname
	var content =      "        <div class='cfinsure_content'>"
	if (confmText&&isInsure){
		content += "<div class='cfmsg_h2 insure'>"+title+"</div>"
		content += "<div class='cfmsg_text insure'>"+desc
		tagdetail = $('#'+this.pagedetailname)
		tagdetail.css("height",this.smallPageDetail)
	}else {
		content += "<div class='cfmsg_h2'>"+title+"</div>"
		content += "<div class='cfmsg_text final'>"+desc
		tagdetail = $('#'+this.pagedetailname)
		tagdetail.css("height",this.finalPageDetail)
	}
//	content += " dfa<br>fda<br>www<br>www<br>www<br>www<br> "
//	content += "           </div>"
	content += "             <div style='padding-top:47px;'>"
		
	if (confmText){
		content += "          <button class='cf_bt bt_cancel' onclick='g_insure.closeDetail()'>取消</button>"
		content += "          <button class='cf_bt' onclick='"+okCallback+"("+itemid+","+qty+")'>"+confmText+"</button>"
	}else {
		content += "          <button class='cf_bt bt_cancel' onclick='g_insure.closeDetail()'>确认</button>"
	}
	content += "             </div>"
		
	content += "             </div>"
	var tag = document.getElementById(detail);
	tag.innerHTML = content;
 		
	a = $('#'+tagname)
	a.modal({position:getSizes().DetailPageTop,show: true});  
}

Insure.prototype.closeDetail = function(id){ 
	playAudioHandler('close1');	
	$('#'+this.tagdetailname).modal('hide');  
}

Insure.prototype.show_insuredetail = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
       
	var pitem = g_player.getInsureItem(id);
       
	 var content = "            <div>"
	 content += "        <table class='cfinsure_tabl'>"
	 content += "             <tr>"
	 content += "               <td width='30%' ><div class='cfplayer_head_bg insure'><img src='static/img/insure_"+id+".png' class='cfinsure_img'></div></td>"
	 content += "               <td><div class='cfinsure_desc'>"+item.descs+"</div> </td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "
	content += "<img src='static/img/pop_line.png' style='width:95%'>"
	content += "<br> "
	 content += "<div>  "
	 content += "<table class='cfinsure_tabl2'>"
	 content += "<tr><td>价格: <img class='cficon_money' src='static/img/money.png'/> "+item.price+" </td></tr>"
	 content += "<tr><td>周期: "+item.period+"小时 </td></tr>"
	content += "</table>"
	if (pitem.qty>0){
	     var timeout = timeoutDesc(pitem,item,1);
      	content += "<br>(已购买该保险,  "+timeout+")";
 	}
	content += "           </div>  "

	var confirm;
	if (pitem.qty<=0)
		confirm = "购买";		
    this.showDetail(item.name,content,"g_insure.doBuy",id,1,confirm,true);
	
}

Insure.prototype.show_finandetail = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
	var pitem = g_player.getInsureItem(id);

	var per = "100%"
	if (item.descs.length>35){
	 per = "80%"
	}
	 var content = "            <div>"
	 content += "        <table class='cfinsure_tabl'>"
	 content += "             <tr>"
	 content += "               <td width='30%' ><div class='cfplayer_head_bg insure'><img src='static/img/insure_"+id+".png' class='cfinsure_img'></div></td>"
	 content += "               <td><div class='cfinsure_desc'><span style='font-size:"+per+"'>"+item.descs+"</span></div></td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "
	content += "<img src='static/img/pop_line.png' style='width:95%'>"
 content += "           <div class='cfinsure_finan'>  "
 content += "<table class='cfinsure_tabl2'>"
 content += "<tr><td>价格: <img class='cficon_money' src='static/img/money.png'/> "+item.price+"</td></tr>"
 content += "<tr><td>收益: <img class='cficon_money' src='static/img/money.png'/> "+item.profit+"/份</td></tr>"
 content += "<tr><td>周期: "+item.period+"小时</td></tr>"
content += "</table>     "
 	if (pitem.qty>0){
	     var timeout = timeoutDesc(pitem,item,1);
      	content += "<br>已购买  <span style='color:red'>"+pitem.qty+"</span>份, "+timeout;
 	}
 	if (pitem.qty<=0){
 content += "        <table class='cfinsure_tabl'>"
	 content += "             <tr>"
	 content += "               <td><input type='button' class='cf_count insure' onclick='g_insure.countBuy(-1)'></td>"
	 content += "               <td><input type='text' id='finan_count' value='1' class='cfinsure_finalcount'></td>"
	 content += "              <td> <input type='button' class='cf_count add insure' onclick='g_insure.countBuy(1)'></td>"
	content += "              </tr>"
content += "          </table>     "
}
content += "           </div>  "
	
	var confirm;
	if (pitem.qty<=0)
		confirm = "购买";		
    this.showDetail(item.name,content,"g_insure.doBuy",id,0,confirm,false);
}

Insure.prototype.countBuy = function(count) {
var tag = document.getElementById('finan_count');
    var currCount = parseInt(tag.value);
    if (currCount+count<=0) {
	currCount = 1;
    }else
	currCount += count;
    tag.value = currCount;
}


Insure.prototype.doBuy = function(id,qty) {
	var item = store.get(this.name)[id];
	if (!item) return;
	if (item.type==1){
		var tag = document.getElementById('finan_count');
		if (tag!=null)
			qty = tag.value;	
	}
	if (qty==0){
	    g_msg.tip(this.cname+"购买","份数不能为零!");
	    return;
	}	
	
	var ps = item.price;
    var amount = ps * qty;
    var cash = g_player.saving[1].amount;
    if (cash<amount){
	    g_msg.tip("您的现金不够，购买失败!");
	    return;
    }
    	
	this.requestBuy(id,qty,amount);
}

Insure.prototype.requestBuy = function(id,qty,amount) {
 
 if (id){
 var item = store.get(g_insure.name)[id];
  g_insure.buyItem = {itemid:id,playerid:g_player.data.playerid,
			qty:qty,price:item.price,amount:amount};  
 }
 
 if (!g_insure.buyItem) return false; 
 
 g_msg.showload("g_insure.requestBuy",true);
	var dataParam = "type=2&playerid="+g_insure.buyItem.playerid+"&itemid="+g_insure.buyItem.itemid;
	dataParam += "&qty="+g_insure.buyItem.qty+"&price="+g_insure.buyItem.price+"&amount="+g_insure.buyItem.amount;
	var sessionid = g_player.getSession(g_player.data.playerid);
  	if (sessionid!=null)
  		dataParam += "&sessionid="+sessionid;
	
	try    {
		$.ajax({type:"post",url:"/cf/data_update.jsp",data:dataParam,success:function(data){
		 g_insure.buyCallback(cfeval(data));
         g_msg.destroyload();
		}});
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj.responseText);
	   return false;
	}	 
}
	
Insure.prototype.buyCallback = function(ret){
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
		
	var insureback = cfeval(ret.desc);
		
  playAudioHandler('money');	
  
	g_player.updateData({"cash":insureback.liveamount});
	g_player.resetSession(insureback.sessionid);
	g_quest.onBuyItem(this.name,item,1);
				   
	//tip:
	g_msg.tip("您购买"+buyitem.qty+"份<span style='color:red'>"+item.name+"</span>成功");
	
	 g_player.flushPageview();
	
	this.hide(this.tagdetailname);
	//刷新list 页面:
	this.buildPage(0);
}

Insure.prototype.existTimeout = function() {
	var tdata = store.get(this.name);
	var hasTip = false;
	for (itemid in g_player.insure){
	 pitem = g_player.insure[itemid];
	 if (!pitem) continue;
     var item = tdata[itemid];
	 var timeout = calculateTimeout(pitem,item);
	 if (timeout<1){
	  hasTip = true;
	  break;
	 }
	}
	
	return hasTip;
}

Insure.prototype.update = function(){
	this.count++;
	if (this.count%25==0){
	  this.hasTip = this.existTimeout();
	}
	
	if (!this.hasTip||!g_game.enter) {
		var tag=$("#tag2"+this.name);
		if (tag){
			tag.remove();
		}
		return;
	}
	
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
		tag.innerHTML = "<div class='cfpage_text tip' onclick='g_insure.show()'></div>"
	}else
		tag.innerHTML = "<div class='cfpage_text tip2' onclick='g_insure.show()'></div>"
}

var g_insure = new Insure();

