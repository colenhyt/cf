Saving = function(){
    this.name = "saving";
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

Saving.prototype.buildPage = function(page)
{
	if (page<0)
		return
		
	var tdata = store.get(this.name);
	var content = 	"";
	if (tdata.length<=0){
		  content += "<div class='cfpanel' ID='saving_d1'><div class='panel-body'>没有产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>tdata.length)
			end = tdata.length;
		  content += "<div style='height:330px'>"
		for (var i=start;i<end;i++){
			var item = tdata[i];
		  content += "<div class='cfpanel' ID='saving_d1' onclick='g_saving.showDetail("+item.id+")'>"
		     content += "<h2 class='cf_h'>"+item.name+"</h2>"
			 content += "        <table id='toplist1_tab' width='100%'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td>利率:￥"+item.rate+"</td>"
			 content += "               <td class='td_right'>周期:"+item.period+"h</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
      		content += "</div>"
		}
   		content += "</div>"
        
        content += this.buildPaging(page,tdata.length);
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
	
}

Saving.prototype.showDetail = function(id){    
	var tdata = store.get(this.name);
   var item = tdata[id-1];
   if (item==null) return;
        
var content =      "        <div><h2 style='margin-top:-5px;margin-bottom:-5px;text-align:center'>"+item.name+"</h2>"
content += "<img src='static/img/pop_line.png'>"
 content += "            <div>购买保险</div>"
 content +=	"</div>"
 content += "           <div style='margin-top:10px;margin-bottom:30px;height:150px'>  "
 content += "        <table id='toplist1_tab'>"
 content += "           <thead>"
 content += "             <tr>"
 content += "               <td class='td-c-name'>利率</td>"
 content += "               <td class='td-c-value'>"+item.rate+"</td>"
 content += "               <td class='td-c-name'>周期</td>"
 content += "               <td class='td-c-value'>"+item.period+"</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td colspan='2' class='td-c-name'><input type='button' class='cf_count' onclick='g_saving.countBuy(-1)'></td>"
 content += "               <td colspan='2' class='td-c-name'><input type='text' id='saving_count' value='1' style='width:80px;text-align:center'></td>"
 content += "               <td colspan='2' class='td-c-name'><input type='button' class='cf_count add' onclick='g_saving.countBuy(1)'></td>"
content += "              </tr>"
content += "            </thead>"
content += "          </table>     "
content += "           </div>  "
content += "           <div style='margin-top:10px'>  "
content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
content += "          <button class='cf_bt' onclick='g_stock.buy("+id+")'>购买</button>"
content += "             </div>"
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
	$('#'+this.tagdetailname).modal('show');
}

Saving.prototype.countBuy = function(count){
    var tag = document.getElementById('saving_count');
    var currCount = parseInt(tag.value);
    if (currCount+count<=0) {
	currCount = 1;
    }else
	currCount += count;
    tag.value = currCount;
    //alert(tag.value);
}

Saving.prototype.buy = function(id){
if (id>0)
{
	var item = this.findItem(id);
	if (item!=null){
		if (g_player.data.cash<item.price){
			alert('你的钱不够');
			return;
		}
		var psaving = g_player.data.saving;
		if (psaving==null)
			psaving=[];
		var jsonCurr = Date.parse(new Date());
		psaving.push({id:item.id,accept:jsonCurr,status:0});
		var cash = g_player.data.cash - item.price;
		var pdata = {"saving":psaving,"cash":cash};
		g_player.updateData(pdata);
		g_quest.onBuySaving(item);
	}
}
  $('#mysaving_detail').modal('hide');
}

Saving.prototype.add = function(playerid,id,amount) {
	var createtime = Date.parse(new Date());
	var item = this.findItem(id);
	
	var saving = {
		playerid:playerid,name:item.name,rate:item.rate,
		period:item.period,
		type:item.type,amount:amount,createtime:createtime
	};
	
	if (playerid==g_player.data.playerid){
		g_player.saving.push(saving);
	}
	
	var updateStr = "saving="+JSON.stringify(saving);
	try  {
		$.ajax({type:"post",url:"/cf/saving_add.do",data:updateStr,success:this.syncCallback});
	}   catch  (e)   {
	    document.write(e.name);
	}  	
}

Saving.prototype.update = function(playerid,id,amount) {
	var saving = {
		id:id,playerid:playerid,amount:amount,createtime:createtime
	};
	
	var updateStr = "saving="+JSON.stringify(saving);
	try  {
		$.ajax({type:"post",url:"/cf/saving_update.do",data:updateStr,success:this.syncCallback});
	}   catch  (e)   {
	    document.write(e.name);
	} 
}

var g_saving = new Saving();
 g_saving.init();