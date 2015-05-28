// JavaScript Document
Bank = function(){
	this.name = "bank"
    this.count = 0;
    this.pageCount = 5;
    this.currPage = 0;
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
   this.pageheader = this.tagname+"header";
}

Bank.prototype = new Datamgr();

Bank.prototype.init = function(){
 store.remove(this.name)
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_savingdata);
	} 
	this.buildHTML2();
}

Bank.prototype.onEnter = function(){
	var pitems = g_player.saving;
	var hasRisk = true;
	for (key in pitems){
		if (key!=1){
			hasRisk = false;
			break;
		}
	}	
	if (hasRisk)
	 g_msg.tip("您还没有任何定期存款，收益偏低");
}

Bank.prototype.buildHTML2 = function()
{
		var page = new PageUtil(this.tagname);
            page.addHeader("<button type='button' class='close' data-dismiss='modal'><img src='static/img/close.png' class='cf_title_close top'></button>");
	var header = "<ul id='"+this.id+"Tab' class='nav nav-tabs'>"
            header += "<div id='"+this.pageheader+"'></div>"
           header += "</ul>"	
	page.addHeader(header);                

		var content = 	"<div class='tab-pane in active' id='quest1'>";
		content += "<div class='cfpage' id='"+this.pagename+"'>"
	    content += "</div></div>"
		page.addContent(content);
		document.write(page.toString());
		
        var page = this;
        
 		var tag = document.getElementById(this.tagname+"_dialog");
 		if (tag){
		 tag.style.setProperty("width",getSizes().PageWidth);
		 tag.style.setProperty("height",getSizes().PageHeight);
 		}
		         
		$('#'+this.tagname).on('hide.zui.modal', function()
		{
			if (page.onClose)
		  		page.onClose();
		}) 		
}

Bank.prototype.buildPage = function(page){
	this.currPage = page;
	this.showBank(page,true);
}

Bank.prototype.showBank = function(page,pop){
	
	playAudio('open.wav');	
    var   header ="";
    var desc = "";
	if (page==0) {
            header += "<button class='cf_title_bg saving2off' onclick='g_bank.showBank(1)'></button>"
            header += "<button class='cf_title_bg savingon'></button>"
	}
	else{
           header += "<button class='cf_title_bg savingoff' onclick='g_bank.showBank(0)'></button>"
            header += "<button class='cf_title_bg saving2on'></button>"
        }        	
	var tagHeader = document.getElementById(this.pageheader);
	tagHeader.innerHTML = header;
	
	if (page==0){
		this.showSaving();
	}else
		this.showSaving2(page-1);

	if (pop)
    	$('#'+this.tagname).modal({position:Page_Top,show:true});       
}

Bank.prototype.showSaving = function(){
	var player = g_player;
	var data = g_player.getTotal(g_player);
	var total = data.saving+data.saving2;
	
	var sdata = store.get(g_saving.name);
	var tt = ForDight0(data.saving/12*0.0035);
	var rate = sdata[1].rate;
	var content = "<div class='cfpanel bank'>"
	content +=	"<div> 活期存款: <span style='color:yellow'> <img class='cficon_money' src='static/img/money.png'/> "+data.saving+"</span></div>"
	content +=	"<div> 定期存款: <span style='color:yellow'> <img class='cficon_money' src='static/img/money.png'/> "+data.saving2+"</span></div>"
	content +=	"<div> 存款总额: <span style='color:yellow'> <img class='cficon_money' src='static/img/money.png'/> "+total+"</span></div>"
    content +=            " </div>"
    content +=	"</div>"
    content +=            " <div class='cf-bank-feeling'>"
    content +=            "     <div>两小时后将获得活期利息<img class='cficon_money' src='static/img/money.png'/>"+tt+"<div>  "
    content +=            "     <div><span>当前活期利率: "+rate+"% </span><div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;	

}

Bank.prototype.showSaving2 = function(page){
	var tdata = store.get(this.name);
	var ids = mapIDs(tdata);
	var content = 	"";
	if (ids.length<=0){
		  content += "<div class='cfpanel' ID='stock_d1'><div class='panel-body'>没有存款产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount+1;
		var end = (page+1)* this.pageCount+1;
		if (end>ids.length)
			end = ids.length;
		  content += "<div class='cfpanel_body'>"
		for (var i=start;i<end;i++){
			var item = tdata[ids[i]];
			var itemid = ids[i];
			var pitem = g_player.getSavingItem(itemid);
			var psColor = "red";
		     content += "<div class='cfpanel bank2' ID='"+this.name+"_d"+itemid+"' onclick='g_bank.showDetail("+itemid+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
		     if (pitem.amount>0){
		     	content += "<span class='cfpanel_text right'>已存入 <span style='color:yellow'>"+ForDight(pitem.amount)+"</span>元"
		     	var inter = ForDight(item.rate*pitem.amount/100);
			     content += ",利息 <span style='color:color'> "+inter+"</span> 元</span>"
		     }else {
		     content += "<span class='cfpanel_text right'>存款  <span style='color:yellow'>0</span> 元</span>"
		     }
		     content += "</span>";
			 content += "	<div>"
			 content += "<span class='cfpanel_text'>利率: "+ForDight(item.rate)+"%</span>"
			 if (pitem.amount<=0)
			 	content += "<span class='cfpanel_text right'>尚未存入</span>"
			 else {
			 	var timeout = timeoutDesc(pitem,item);
			 	content += "<span class='cfpanel_text right'>"+timeout+"</span>"
			 }
			content += "     </div>"
      		content += "</div>"
		}
     		content += "</div>"
		
        content += this.buildPaging(page,ids.length-1,"g_bank.showBank");
	}
	
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
     
}

Bank.prototype.showDetail = function(id){
	this.onPanelClick(id);
	g_saving.showDetail(id);
}

Bank.prototype.existTimeout = function() {
	var tdata = store.get(this.name);
	var hasTip = false;
	for (itemid in g_player.saving){
	 pitem = g_player.saving[itemid];
     var item = tdata[itemid];
	 var timeout = calculateTimeout(pitem,item);
	 if (itemid!=1&&timeout<0.5){
	  hasTip = true;
	  break;
	 }
	}
	
	return hasTip;
}
Bank.prototype.update = function(){
	this.count++;
	if (this.count%25==0){
	  this.hasTip = this.existTimeout();
	}
	
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
		tag.innerHTML = "<img src='static/img/tip_cash.png' class='cfpage_text banktip' onclick='g_bank.show()'>"
	}else
		tag.innerHTML = "<img src='static/img/tip_cash.png' class='cfpage_text banktip2' onclick='g_bank.show()'>"
}

var g_bank = new Bank();
g_bank.init();
