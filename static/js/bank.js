// JavaScript Document
Bank = function(){
	this.name = "bank"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
}

Bank.prototype = new Datamgr();

Bank.prototype.init = function(){
	this.buildHTML();
}

Bank.prototype.show = function(){
	
	var content = "<div class='cfpanel bank'>"
	content +=	"<div> 存款:<span>¥29381</span></div>"
	content +=	"<div> 股票价值:<span>¥5901</span></div>"
	content +=	"<div> 保险价值:<span>¥5901</span></div>"
	content +=	"<div> 总资产值:<span>¥15901</span></div>"
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

    $('#'+this.tagname).modal({position:50,show:true});       
}

var g_bank = new Bank();
g_bank.init();
