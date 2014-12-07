// JavaScript Document
Bank = function(){
	this.name = "bank"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
}

Bank.prototype = new Datamgr();

Bank.prototype.init = function(){
	this.buildHTML("title_bank.png");
}

Bank.prototype.buildPage = function(page)
{
//	var page = new PageUtil(this.tagname,0,'modal-content2');
//	var content = page.buildHeader1('签到','g_bank.doBank');
//	page.addHeader(content);

	var content = "<div class='cf-bank-prize'>"
	content +=	"<div> 今天获得:"
    content +=      "      <div id='bank_gettoday'></div>"
    content +=       " </div>"
    content +=           "  <div> 明天将获得:"
    content +=            "     <div id='bank_gettomorrow'></div>"
    content +=            " </div></div>"
    content +=            " <div class='cf-bank-feeling'> 请选择你今天的心情:"
    content +=            "     <div id ='bank_feeling'>bank_feeling</div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;

}

Bank.prototype.show = function(){
	
     var option = {
    	position : 50,
    	show     : true
	}
    $('#'+this.tagname).modal(option);       
}

var g_bank = new Bank();
g_bank.init();
