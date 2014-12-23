// JavaScript Document
Bank = function(){
	this.name = "bank"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
   this.pageheader = this.tagname+"header";
}

Bank.prototype = new Datamgr();

Bank.prototype.init = function(){
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
	var playerId = g_player.data.playerid;
	this.showBank(playerId,page);
}

Bank.prototype.showBank = function(playerId,page){
	
    var   header ="";
    var desc = "";
	if (page==0) {
            header += "<button class='cf_title_bg saving2off' onclick='g_bank.buildPage(1)'></button>"
            header += "<button class='cf_title_bg savingon'></button>"
	}
	else if (page==1){
           header += "<button class='cf_title_bg savingoff' onclick='g_bank.buildPage(0)'></button>"
            header += "<button class='cf_title_bg saving2on'></button>"
        }        	
	var tagHeader = document.getElementById(this.pageheader);
	tagHeader.innerHTML = header;
	
	if (page==0){
		this.showSaving(playerId);
	}else
		this.showSaving2(playerId);

    $('#'+this.tagname).modal({position:Page_Top,show:true});       
}

Bank.prototype.showSaving = function(playerId){
	var player = g_player.find(playerId);
	if (player==null){
		return;
	}
	
	var data = g_player.getTotal(player);
	var total = data.saving;
	
	var content = "<div class='cfpanel bank'>"
	content +=	"<div> 活期存款: <span style='color:yellow'> ¥"+data.saving+"</span></div>"
	content +=	"<div> 定期存款: <span style='color:yellow'> ¥"+data.saving+"</span></div>"
	content +=	"<div> 存款总额: <span style='color:yellow'> ¥"+total+"</span></div>"
    content +=            " </div>"
    content +=	"</div>"
    content +=            " <div class='cf-bank-feeling'> <span>每天</span> 8:00AM~9:00PM"
    content +=            "     <div>每小时结算利息<div>  "
    content +=            "     <div><span>当前利率:</span><div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;	

}

Bank.prototype.showSaving2 = function(playerId){
	var player = g_player.find(playerId);
	if (player==null){
		return;
	}
	
	var data = g_player.getTotal(player);
	var total = data.saving+data.insure+data.stock;
	
	var content = "<div class='cfpanel bank'>"
	content +=	"<div> 存款:<span>¥"+data.saving+"</span></div>"
	content +=	"<div> 股票价值:<span>¥"+data.stock+"</span></div>"
	content +=	"<div> 保险价值:<span>¥"+data.insure+"</span></div>"
	content +=	"<div> 总资产值:<span>¥"+total+"</span></div>"
	content +=	"<div> <img src='static/img/icon_toplist.png'>当前排名:<span>3</span></div>"
    content +=            " </div>"
    content +=	"</div>"
    content +=            " <div class='cf-bank-feeling'> <span>每天</span> 8:00AM~9:00PM"
    content +=            "     <div>每小时结算利息<div>  "
    content +=            "     <div><span>当前利率:</span><div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;	

}

var g_bank = new Bank();
g_bank.init();
