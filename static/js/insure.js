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
		g_msg.open("你还没有购买任何保险，风险较高");
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
			 content += "<span class='cfpanel_text'>价格: ￥"+ForDight(item.price)+"</span>"
			 if (pitem.qty<=0)
			 	content += "<span class='cfpanel_text right'>期限:"+item.period+"天</span>"
			 else {
			 	var timeout = calculateTimeout(pitem,item);
			 	content += "<span class='cfpanel_text right'>到期:<span style='color:red'>"+timeout+"</span>天</span>"
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
   if (type==0)
   	g_insure.show_insuredetail(id)
   else
   	g_insure.show_finandetail(id)
}

Insure.prototype.showDetail = function(title,desc,okCallback,itemid,qty,confmText){
	var content =      "        <div class='cfinsure_content'>"
	content += "<div class='cfmsg_h2'>"+title+"</div>"
	content += "<img src='static/img/pop_line.png'>"
	content += "            <div class='cfmsg_text insure'>"+desc+"</div>"
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>"
	if (confmText)
	content += "          <button class='cf_bt' onclick='"+okCallback+"("+itemid+","+qty+")'>"+confmText+"</button>"
	content += "             </div>"
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
		
	$('#'+this.tagdetailname).modal({position:getSizes().DetailPageTop,show: true});  
}

Insure.prototype.closeDetail = function(id){ 
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
	 content += "               <td>"+item.descs+" </td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "
	content += "<br> "
	 content += "<div>  "
	 content += "<table class='cfinsure_tabl2'>"
	 content += "<tr><td>价格: ￥"+item.price+" </td></tr>"
	 content += "<tr><td>周期: "+item.period+"天 </td></tr>"
	content += "</table>"
	if (pitem.qty>0){
	     var timeout = calculateTimeout(pitem,item);
      	content += "(已购买该保险,  还有<span style='color:red'>"+timeout+"</span>天到期)";
 	}
	content += "           </div>  "

	var confirm;
	if (pitem.qty<=0)
		confirm = "购买";		
    this.showDetail(item.name,content,"g_insure.preBuy",id,1,confirm);
	
}

Insure.prototype.show_finandetail = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
	var pitem = g_player.getInsureItem(id);

	 var content = "            <div>"
	 content += "        <table class='cfinsure_tabl'>"
	 content += "             <tr>"
	 content += "               <td width='30%' ><div class='cfplayer_head_bg insure'><img src='static/img/insure_"+id+".png' class='cfinsure_img'></div></td>"
	 content += "               <td>"+item.descs+" </td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "
 content += "           <div class='cfinsure_finan'>  "
 content += "<table class='cfinsure_tabl2'>"
 content += "<tr><td>价格: ￥"+item.price+"</td></tr>"
 content += "<tr><td>收益: ￥"+item.profit+"/份</td></tr>"
 content += "<tr><td>周期 "+item.period+"天</td></tr>"
content += "</table>     "
 	if (pitem.qty>0){
	     var timeout = calculateTimeout(pitem,item);
      	content += "已购买  <span style='color:red'>"+pitem.qty+"</span>份, 还有<span style='color:red'>"+timeout+"</span>天到期";
 	}
 	if (pitem.qty<=0){
 content += "        <table class='cfinsure_tabl'>"
	 content += "             <tr>"
	 content += "               <td><input type='button' class='cf_count' onclick='g_insure.countBuy(-1)'></td>"
	 content += "               <td><input type='text' id='finan_count' value='1' class='cfinsure_finalcount'></td>"
	 content += "              <td> <input type='button' class='cf_count add' onclick='g_insure.countBuy(1)'></td>"
	content += "              </tr>"
content += "          </table>     "
}
content += "           </div>  "
	
	var confirm;
	if (pitem.qty<=0)
		confirm = "购买";		
    this.showDetail(item.name,content,"g_insure.preBuy",id,0,confirm);
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


Insure.prototype.preBuy = function(id,qty) {
	var item = store.get(this.name)[id];
	if (!item) return;
	if (item.type==1){
		var tag = document.getElementById('finan_count');
		if (tag!=null)
			qty = tag.value;	
	}
	this.confirmBuy(id,qty);
}

Insure.prototype.update = function(){
	this.count++;
	if (this.count%25==0){
		var ids = g_player.getOfftimeInsure();
		if (ids.length>0)
			this.hasTip = true;
	}
	
	this.hasTip = true;
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
	var index = tip.indexOf("tip2");
	if (index>0){
		tag.innerHTML = "<img src='static/img/tip_cash.png' class='cfpage_text tip' onclick='g_insure.show()'>"
	}else
		tag.innerHTML = "<img src='static/img/tip_cash.png' class='cfpage_text tip2' onclick='g_insure.show()'>"
}

var g_insure = new Insure();
 g_insure.init();
