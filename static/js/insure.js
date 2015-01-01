Insure = function(){
    this.name = "insure";
    this.cname = "保险";
    this.pageCount = 4;
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
					buyDesc = "你已购买 <span style='color:yellow'>"+pitem.qty+"</span> 份";
			}else {
				var psColor = "green";
				if (pitem.profit<0)
					psColor = "red"
				if (pitem.qty<=0)
					buyDesc = "尚未购买";
				else {
					buyDesc = "已购买"+pitem.qty+"份,总盈亏:"+pitem.profit;
				}			
			}
			
		  content += "<div class='cfpanel' ID='"+this.name+"_d"+itemid+"' onclick='g_insure.clickDetail("+itemid+","+item.type+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
			 content += "<span class='cfpanel_text right'>"+buyDesc+"</span>"
			 content += "	<div>"
			 content += "<span class='cfpanel_text'>价格: ￥"+ForDight(item.price)+"</span>"
			 content += "<span class='cfpanel_text right'>期限:"+item.period+"天</span>"
			content += "     </div>"
      		content += "</div>"
		}
			content += "           <div style='margin-top:10px;font-size:25px;color:pink'>  "
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
   if (type==1)
   	g_insure.show_insuredetail(id)
   else
   	g_insure.show_finandetail(id)
}

Insure.prototype.showDetail = function(title,desc,okCallback,cbParam1,cbParam2,cbParam3,confmText){
	var content =      "        <div style='margin-top:-10px;text-align:center'>"
	content += "<div class='cfmsg_h2'>"+title+"</div>"
	content += "<img src='static/img/pop_line.png'>"
	content += "            <div class='cfmsg_text'>"+desc+"</div>"
		content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>"
	if (confmText!=null){
		if (cbParam1==null)
			cbParam1 = "1";
		if (cbParam2==null)
			cbParam2 = "1";
		if (cbParam3==null)
			cbParam3 = "1";
		content += "          <button class='cf_bt' onclick='"+okCallback+"("+cbParam1+","+cbParam2+","+cbParam3+")'>"+confmText+"</button>"
	}
	content += "             </div>"
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
		
	$('#'+this.tagdetailname).modal({position:PageDetail_Top,show: true});  
}

Insure.prototype.closeDetail = function(id){ 
	$('#'+this.tagdetailname).modal('hide');  
}

Insure.prototype.show_insuredetail = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
       
	var pitem = g_player.getInsureItem(id);
       
	 var content = "            <div>"+item.descs+"</div>"
	 if (pitem.qty>0)
	 	content += "到期时间:"
	 content += "           <div>  "
	 content += "        <table id='toplist1_tab'>"
	 content += "             <tr>"
	 content += "               <td class='td-c-name'>价格: </td>"
	 content += "               <td class='td-c-value'>￥"+item.price+"</td>"
	 content += "               <td class='td-c-name'> 周期: </td>"
	 content += "               <td class='td-c-value'>"+item.period+"天</td>"
	content += "              </tr>"
	content += "          </table>     "
	content += "           </div>  "

	var confirm;
	if (pitem.qty<=0)
		confirm = "购买";		
    this.showDetail(item.name,content,"g_insure.confirmBuy",id,1,0,confirm);
	
}

Insure.prototype.show_finandetail = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
	var pitem = g_player.getInsureItem(id);
	var psColor = "green";
	if (pitem.profit<0)
		psColor = "red"
        
 var content = "            <div>"+item.descs+"</div>"
 content += "           <div style='margin-top:10px;margin-bottom:30px;height:150px'>  "
 content += "        <table id='toplist1_tab'>"
 content += "           <thead>"
 content += "             <tr>"
 content += "               <td class='td-c-name'>价格:</td>"
 content += "               <td class='td-c-value'>￥ "+item.price+"</td>"
 content += "               <td class='td-c-name'> 收益</td>"
 content += "               <td class='td-c-value'>"+item.profit+"</td>"
 content += "               <td class='td-c-name'> 周期</td>"
 content += "               <td class='td-c-value'>"+item.period+"天</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td colspan='2' class='td-c-name'><input type='button' class='cf_count' onclick='g_insure.countBuy(-1)'></td>"
 content += "               <td colspan='2' class='td-c-name'><input type='text' id='finan_count' value='1' style='width:150px;text-align:center;height:58px;font-size:32px'></td>"
 content += "               <td colspan='2' class='td-c-name'><input type='button' class='cf_count add' onclick='g_insure.countBuy(1)'></td>"
content += "              </tr>"
content += "            </thead>"
content += "          </table>     "
content += "           </div>  "
	
	var confirm;
	if (pitem.qty<=0)
		confirm = "购买";		
    this.showDetail(item.name,content,"g_insure.confirmBuy",id,0,0,confirm);
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
var g_insure = new Insure();
 g_insure.init();
