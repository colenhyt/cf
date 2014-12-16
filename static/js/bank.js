// JavaScript Document
Bank = function(){
	this.name = "bank"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
    this.graphName = this.name+"graph";
}

Bank.prototype = new Datamgr();

Bank.prototype.init = function(){
	this.buildHTML();
}

Bank.prototype.show = function(){
	var playerId = g_player.data.playerid;
	this.showBank(playerId);
}

Bank.prototype.showBank = function(playerId){
	var player = g_player.find(playerId);
	if (player==null){
		return;
	}
	
	var content = "<div class='cfpanel bank'>"
	content +=	"<div> 存款:<span>¥"+player.cash+"</span></div>"
	content +=	"<div> 股票价值:<span>¥"+player.cash+"</span></div>"
	content +=	"<div> 保险价值:<span>¥"+player.cash+"</span></div>"
	content +=	"<div> 总资产值:<span>¥"+player.cash+"</span></div>"
	content +=	"<div> <img src='static/img/icon_toplist.png'>当前排名:<span>3</span></div>"
    content +=            " </div>"
    content +=	"</div>"
	content += "<div id='"+this.graphName+"' style='margin-left:-10px'></div>"
    content +=            " <div class='cf-bank-feeling'> <span>每天</span> 8:00AM~9:00PM"
    content +=            "     <div>每小时结算利息<div>  "
    content +=            "     <div><span>当前利率:</span><div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;	

	this.showPie(player);
	
    $('#'+this.tagname).modal({position:50,show:true});       
}

Bank.prototype.showPie = function(player){
	var divName = this.graphName;
	
	var data = [
	        	{name : '存款',value : 35.75,color:'#9d4a4a'},
	        	{name : '股票',value : 29.84,color:'#5d7f97'},
	        	{name : '保险',value : 24.88,color:'#97b3bc'},
        	];
	
	new iChart.Pie2D({
		render : divName,
		data: data,
		title : '当前财富分布',
		legend : {
			enable : true
		},
		showpercent:true,
		decimalsnum:2,
		width : 400,
		height : 400,
		radius:140
	}).draw();	
}

var g_bank = new Bank();
g_bank.init();
