// JavaScript Document
Bank = function(){
	this.name = "bank"
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
}

Bank.prototype.buildPage = function(page){
	this.currPage = page;
	this.showBank(-1,true);
}

Bank.prototype.showBank = function(page,pop){
	
    var   header ="";
    var desc = "";
	if (page==-1) {
            header += "<button class='cf_title_bg saving2off' onclick='g_bank.showBank(0)'></button>"
            header += "<button class='cf_title_bg savingon'></button>"
	}
	else{
           header += "<button class='cf_title_bg savingoff' onclick='g_bank.showBank(-1)'></button>"
            header += "<button class='cf_title_bg saving2on'></button>"
        }        	
	var tagHeader = document.getElementById(this.pageheader);
	tagHeader.innerHTML = header;
	
	if (page==-1){
		this.showSaving();
	}else
		this.showSaving2(page);

	if (pop)
    	$('#'+this.tagname).modal({position:Page_Top,show:true});       
}

Bank.prototype.showSaving = function(){
	var player = g_player;
	var data = g_player.getTotal(g_player);
	var total = data.saving+data.saving2;
	
	var sdata = store.get(g_saving.name);
	var rate = sdata[0].rate;
	var content = "<div class='cfpanel bank'>"
	content +=	"<div> 活期存款: <span style='color:yellow'> ¥"+data.saving+"</span></div>"
	content +=	"<div> 定期存款: <span style='color:yellow'> ¥"+data.saving2+"</span></div>"
	content +=	"<div> 存款总额: <span style='color:yellow'> ¥"+total+"</span></div>"
    content +=            " </div>"
    content +=	"</div>"
    content +=            " <div class='cf-bank-feeling'>"
    content +=            "     <div>每2小时结算利息<div>  "
    content +=            "     <div><span>当前活期利率: "+rate+"% </span><div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;	

}

Bank.prototype.showSaving2 = function(page){
	var tdata = store.get(this.name);
	var content = 	"";
	if (tdata.length<=0){
		  content += "<div class='cfpanel' ID='stock_d1'><div class='panel-body'>没有存款产品</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount+1;
		var end = (page+1)* this.pageCount+1;
		if (end>tdata.length)
			end = tdata.length;
		  content += "<div class='cfpanel_body'>"
		for (var i=start;i<end;i++){
			var item = tdata[i];
			var pitem = g_player.getItemData(g_saving.name,item);
			var psColor = "red";
		     content += "<div class='cfpanel bank2' ID='"+this.name+"_d"+item.id+"' onclick='g_bank.showDetail("+item.id+")'>"
		     content += "<span class='cfpanel_title'>"+item.name+"</span>"
		     content += "<span class='cfpanel_text right'>存款  <span style='color:yellow'> "+ForDight(pitem.amount)+"</span> 元</span>"
			 content += "	<div>"
			 content += "<span class='cfpanel_text'>利率: "+ForDight(item.rate)+"%</span>"
			 content += "<span class='cfpanel_text right'>已获得利息: <span style='color:"+psColor+"'>"+ForDight(pitem.profit)+"</span> 元</span>"
			content += "     </div>"
      		content += "</div>"
		}
     		content += "</div>"
		
        content += this.buildPaging(page,tdata.length-1,"g_bank.showBank");
	}
	
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
     
}

Bank.prototype.showDetail = function(id){
	this.onPanelClick(id);
	g_saving.showDetail(id);
}

var g_bank = new Bank();
g_bank.init();
