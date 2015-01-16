
// JavaScript Document
Playerinfo = function(){
    this.name = "playerinfo";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.pageheader = this.tagname+"header";
    this.graphName = this.name+"graph";
}

Playerinfo.prototype = new Datamgr();

Playerinfo.prototype.init = function(){
    this.buildHTML();
}

Playerinfo.prototype.show = function(){
	this.showInfo(g_player);
}

Playerinfo.prototype.showInfo = function(jsondata){
	var texp = store.get("exp");
	var lv = g_title.getLevel();
	var content = "<div class='cfplayer_panel'>"
	content += "<table>"
	content += " <tr>"
	content += "  <td width='100'>"
	content += " <div class='cfplayer_head_bg'>"
    content += "<img src='"+head_imgs[jsondata.data.sex].src+"'/>"
    content += " </div>"
	content += "</td>"
	content += "   <td>"
    content +=            " <div class='cfplayer_panel_text'> "
    content += "昵            称: <span style='color:yellow'>"+jsondata.data.playername.substring(0,10)+"</span><br>"	
    content += "当前等级: <span style='color:yellow'>"+lv+"</span> "+g_title.getData(lv).name+"<br>"	
    content += "下一等级: <span style='color:yellow'>"+(lv+1)+"</span> "+g_title.getData(lv+1).name+"<br>"
    content += "经            验: <span style='color:yellow'>"+g_title.getData(lv).exp+"</span>/"+jsondata.data.exp	+"<br>"
    content += "当周排名: <span style='color:yellow'>"+jsondata.data.weektop+"</span><br>"	
    content +=            " </div>"
	content += "</td>"
	content += " </tr>"
	content += "</table>"
    content += " </div>"
    
	var data = g_player.getTotal(jsondata);
    
	content += "<div class='cfplayer_info'>"
	content += "<table>"
	content += " <tr>"
	content += "  <td class='cfplayer_info2'>"
	content +=	"<div class='cfprop p1'>定期存款<br>"+data.saving2+"</div>"
	content +=	"<div class='cfprop p2'>投资股票<br>"+data.stock+"</div>"
	content +=	"<div class='cfprop p3'>投资保险<br>"+data.insure+"</div>"
	content +=	"<div class='cfprop p4'>活期存款<br>"+data.saving+"</div>"
	content +=	"<div class='cfprop p5'>总资产值<br>"+data.total+"</div>"
	content += "</td>"
	content += "   <td>"
	content += "<div id='"+this.graphName+"' class='cfplayer_pie'></div>"
	content += "</td>"
	content += " </tr>"
	content += "</table>"
    content +=  " </div>"
    
    var get = document.getElementById(this.pagename);
    get.innerHTML = content;
    
	this.showPie(data,this.graphName);
	
    $('#'+this.tagname).modal({position:getSizes().PageTop,show:true});       
}


Playerinfo.prototype.showPie = function(data,divName){
	var data = [
	        	{name : '定期',value : data.saving2,color:'#00ae57'},
	        	{name : '股票',value : data.stock,color:'#51d344'},
	        	{name : '保险',value : data.insure,color:'#ff4400'},
 	        	{name : '活期',value : data.saving,color:'#2894B8'},
        	];
	
	new iChart.Pie3D({
		render : divName,
		data: data,
		title : {
			text:"资产分布",
			fontsize:getSizes().PieFontSize,
			offsety:30,
			color:'#ffffff'
		},
		sub_option:{
					mini_label_threshold_angle : 0,//迷你label的阀值,单位:角度
					mini_label:{//迷你label配置项
						fontsize:10,
						fontweight:600,
						color : '#000000'
					},
					label : {
						background_color:null,
						sign:false,//设置禁用label的小图标
						padding:'0 1',
						border:{
							enable:false,
							color:'#666666'
						},
						fontsize:10,
						fontweight:600,
						color : '#000000'
					},
				},		
		border:{
				enable:false,
			 },		
		padding: 5,
		background_color: "#275868",
		showpercent:true,
		animation:true,
		decimalsnum:2,
		width : getSizes().PieWidth,
		height : getSizes().PieHeight,
		radius:140,
		offset_angle:12,
	}).draw();	
}

Playerinfo.prototype.showOneInfo = function(playerid){
	var jsondata = g_player.find(playerid);
	this.showInfo(jsondata);
}

var g_playerinfo = new Playerinfo();
g_playerinfo.init();