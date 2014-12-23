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
   var item = this.findItem(id);
   if (item==null) return;
        
    var dftAmount = 1000;
    var dftProfit = dftAmount * (1+item.rate/100);
	var content =      "        <div style='margin-top:-10px;text-align:center;'>"
 content += "        <div class='cpgapedetail_h2'>"+item.name+"存款"
	content += "<span class='cfpanel_text right'>存款 </span></div>"
	content += "<img src='static/img/pop_line.png' class='cf_line'>"
 content += "           <div style='margin-top:10px;margin-bottom:10px;height:150px'>  "
 content += "        <table id='toplist1_tab'>"
 content += "           <thead>"
 content += "             <tr>"
 content += "               <td>利率: </td>"
 content += "               <td colspan='3' style='text-align:left'>"+item.rate+"%</td>"
content += "              </tr>"
 content += "             <tr>"
 content += "               <td>存入:</td>"
 content += "               <td><input type='button' class='cf_count' onclick='g_saving.countBuy("+item.id+",-1000)'></td>"
 content += "               <td><input type='text' id='saving_amount' value='1000' style='width:220px;text-align:center;height:58px;font-size:32px'></td>"
 content += "               <td><input type='button' class='cf_count add' onclick='g_saving.countBuy("+item.id+",1000)'></td>"
content += "              </tr>"
content += "             <tr>"
 content += "               <td>预计利息:</td>"
 content += "               <td colspan='3' style='text-align:left'><span id='savingprofit'>"+dftProfit+"</span></td>"
content += "              </tr>"
content += "            </thead>"
content += "          </table>     "
		content += "             </div>"
	content += "           <div style='align:center'>  "
	content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
	content += "          <button class='cf_bt' onclick='g_saving.buy("+id+")'>确认</button>"
	content += "             </div>"

	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
        
	$('#'+this.tagdetailname).modal({position:PageDetail_Top,show: true});
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
    tag2.innerHTML = tag.value * (1+item.rate/100);
    //alert(tag.value);
}

Saving.prototype.buy = function(id){
   var item = this.findItem(id);
   if (item==null) return;
   
    var tag = document.getElementById('saving_amount');
    var amount = parseInt(tag.value);
	var ret = g_player.buyItem(this.name,id,1,amount);
    if (ret==true){
    	//刷新list 页面:
    	g_bank.buildPage(0);
    	//$('#'+this.tagdetailname).modal('hide');
    }    

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