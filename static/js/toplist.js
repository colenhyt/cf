
Toplist = function(){
    this.name = "toplist";
     this.pageCount = 5;
    this.currPage = 0;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
   this.pageheader = this.tagname+"header";
    this.zandata = this.name+"zan";    
    this.weekdata = [];
    this.monthdata = [];
    this.syncDuration = 5;
}

Toplist.prototype = new Datamgr();

Toplist.prototype.init = function(){
	var pzan = store.get(this.zandata);
	if (pzan==null){
		store.set(this.zandata,{});
	}	
	this.buildHTML2();
}

Toplist.prototype.buildHTML2 = function()
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

		page = this;
		$('#'+this.tagname).on('hide.zui.modal', function()
		{
			if (page.onClose)
		  		page.onClose();
		}) 	
		
 		var tag = document.getElementById(this.tagname+"_dialog");
 		if (tag){
		 tag.style.setProperty("width",getSizes().PageWidth);
		 tag.style.setProperty("height",getSizes().PageHeight);
 		}
}

Toplist.prototype.buildPage = function(page)
{
    var  header = "<button class='cf_title_bg moff' onclick='g_toplist.showToplist(1,0)'></button>"
    header += "<button class='cf_title_bg won'></button>"
       	
	var tagHeader = document.getElementById(this.pageheader);
	tagHeader.innerHTML = header;
	
	this.syncData2();
	this.currPage = page;
}

Toplist.prototype.showToplist = function(type,page)
{
	if (page<0)
		return
		
	playAudioHandler('open1');	
	var tdata;
        var   header ="";
	if (type==0) {
	    tdata = this.weekdata;
            header += "<button class='cf_title_bg moff' onclick='g_toplist.showToplist(1,0)'></button>"
            header += "<button class='cf_title_bg won'></button>"
	}
	else if (type==1){
		tdata = this.monthdata;
           header += "<button class='cf_title_bg woff' onclick='g_toplist.showToplist(0,0)'></button>"
            header += "<button class='cf_title_bg mon'></button>"
        }        	
	var tagHeader = document.getElementById(this.pageheader);
	tagHeader.innerHTML = header;
		
	var content = 	"";
	  
	if (tdata.length<=0){
		  content += "<div class='cfpanel' ID='insure_d1'>目前还没有排名数据"
      	  content += "</div>"
	}else {
			 content += "<div class='cf_top'>";
 			 content += "        <table class='cf_top_header'>"
			 content += "             <tr>"
			 content += "               <td class='cftoplist_content'>昵称</td>"
			 content += "               <td class='cftoplist_content'>资产(万)</td>"
			 content += "               <td class='cftoplist_c4'>排名</td>"
			 content += "               <td class='cftoplist_c30'>赞</td>"
			content += "              </tr>"
			content += "          </table>"
			 content += "        <table>"
  
			 var me = tdata[tdata.length-1];
			 var amount = g_player.getTotal(g_player);
			 me.money = amount.total;
			 if (type==0&&me.top!=g_player.data.weektop){
			  g_player.data.weektop=me.top;
			  g_player.flushPageview();
			 }
			
			var items = []
			items.push(me);
			for (var i=0;i<tdata.length-1;i++){
				var item = tdata[i];
				if (item.playerid==me.playerid)continue;
				item.top = i+1;
				var mintop = items.length;
				if (item.money>me.money){
				 mintop -= 1;
				}
				if (mintop==item.top){
				 item.top += 1;
				}
				if (item.money<me.money&&item.top<=me.top){
					item.top = me.top+1;
				}
				for (j=0;j<items.length;j++){
				 if (items[j].money==item.money){
				  item.top = items[j].top;
				  break;
				 }
				}
				items.push(item);
			}
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>items.length)
			end = items.length;
		for (var i=start;i<end;i++){
			var item = items[i];
			 var name = item.playername;
			 if (name!=null)
			 {
				 name = item.playername.substring(0,8);
			 }else
				 name = "";
			 var totalmoney = parseInt(item.money/10000);
			if (i==0)			//第一个：自己
			{
			 var topStr = "无";
			 var hasImg = false;
			 if (item.top>0){
				topStr = item.top;
				if (item.top==4001)
				 topStr = ">4000";
				if (item.top<=3)
				 hasImg = true
			 }
			 content += "             <tr>"
			 content += "               <td class='cftoplist_content' style='color:yellow'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+name+"</div></td>"
			 content += "               <td class='cftoplist_c5' style='color:yellow'><div class='cficon_money toplist' onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+totalmoney+"</div></td>"
			 if (hasImg==true)
			  content += "               <td class='cftoplist_c4' style='color:yellow'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'><img class='cftoplist_top' src='static/img/icon_top_"+topStr+".png'></div></td>"
			 else
			  content += "               <td class='cftoplist_c4' style='color:yellow'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+topStr+"</div></td>"
			   
			 content += "               <td class='cftoplist_c3' style='color:yellow'><div onclick='g_toplist.zan("+page+","+item.playerid+")'"
			  if (item.playerid==me.playerid&&!Is_InBrowser)
			   content += "<input type='button' class='cf_top_share'/><span class='cftoplist_c6'>&nbsp;<span id='zan_"+item.playerid+"'>"+item.zan+"</span></span>"
			 else
			   content += "<input type='button' class='cf_top_zan'/><span class='cftoplist_c6'><span id='zan_"+item.playerid+"'>"+item.zan+"</span></span>"
			 content +="</div></td>"
			 
			 content += "              </tr>"			
			}else {
			
			 content += "             <tr>"
			 content += "               <td class='cftoplist_content'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+name+"</div></td>"
			 content += "               <td class='cftoplist_c5'><div class='cficon_money toplist' onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+totalmoney+"</div></td>"
			 if (item.top<=3)
			  content += "               <td class='cftoplist_c4'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'><img class='cftoplist_top' src='static/img/icon_top_"+item.top+".png'></div></td>"
			 else
			  content += "               <td class='cftoplist_c4'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+item.top+"</div></td>"
			  
			 content += "               <td class='cftoplist_c3'><div onclick='g_toplist.zan("+page+","+item.playerid+")'<input type='button' class='cf_top_zan'/><span class='cftoplist_c6' id='zan_"+item.playerid+"'>"+item.zan+"</span></div></td>"
			 content += "              </tr>"
			}
			
			 content += "             <tr>"
			 content += "               <td colspan='4'><img src='static/img/top_line.png'></td>"
			content += "              </tr>"
		}
			content += "          </table>"
                content += "</div>";

		this.currPage = page;
        content += this.buildPaging(page,items.length,"g_toplist.showToplist",type);
        
        //content += "<div class='cftoplist_desc'>每天只能点赞3次，一天只能对一个人点赞</div>"
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
}

Toplist.prototype.zan = function(page,playerId)
{
	if (playerId==g_player.data.playerid){
	  if (Is_InBrowser)
		g_msg.tip("不能给自己点赞");
	  else
		g_share.doShare();
	
	  return;	
	}
	
	var now = new Date();
	var zandata = store.get(this.zandata);
	var key = g_player.data.playerid+"_"+now.getFullYear()+"-"+now.getMonth()+"-"+now.getDate();
	var zans = zandata[key];
	if (zans==null){
		zans = [];
	}else if (zans.length>=ZAN_COUNT){
		g_msg.tip("每天点赞不能多于"+ZAN_COUNT+"次");
		return;
	}else {
		for (var i=0;i<zans.length;i++){
			if (zans[i]==playerId){
				g_msg.tip("您每天只能对一个人点赞1次");
				return;
			}
		}
	}
	
    var zanTag = document.getElementById('zan_'+playerId);
    var zanCount = parseInt(zanTag.innerHTML)+1;
	try  {
		var data = "toplist.playerid="+playerId+"&toplist.zan="+zanCount;
		$.ajax({type:"post",url:"/cf/toplist_zan.do",data:data});
	}   catch  (e)   {
	    document.write(e.name);
	}   
	
    for (var i=0;i<g_toplist.weekdata.length;i++){
        if (g_toplist.weekdata[i].playerid==playerId) {
            g_toplist.weekdata[i].zan = zanCount;
             break;
        }
    }
	
    for (var i=0;i<g_toplist.monthdata.length;i++){
        if (g_toplist.monthdata[i].playerid==playerId) {
            g_toplist.monthdata[i].zan = zanCount;
             break;
        }
    }
        
	zanTag.innerHTML = zanCount;
	
	zans.push(playerId);
	zandata[key] = zans;
	store.set(this.zandata,zandata);	 
	 
}

Toplist.prototype.syncData2 = function(){
	g_msg.showload("g_toplist.syncData2");
	
	try  {
		var data= "playerid="+g_player.data.playerid;
		$.ajax({type:"post",url:"/cf/toplist_get.jsp",data:data,success:function(data){
		var datas = cfeval(data);
		var weekdata = datas[0];
		g_toplist.weekdata = [];
		for (var i=0;i<weekdata.length;i++){
		 var item = {playerid:parseInt(weekdata[i][0]),playername:weekdata[i][1],money:parseInt(weekdata[i][2]),zan:parseInt(weekdata[i][3]),top:0};
		 if (item.playerid==g_player.data.playerid){
		  item.playername = g_player.data.playername;
		  item.top = parseInt(weekdata[i][4]);
		 }
		 g_toplist.weekdata.push(item);
		}
		
		var monthdata = datas[1];
		g_toplist.monthdata = [];
		for (var i=0;i<monthdata.length;i++){
		 var item = {playerid:parseInt(monthdata[i][0]),playername:monthdata[i][1],money:parseInt(monthdata[i][2]),zan:parseInt(monthdata[i][3]),top:0};
		 if (item.playerid==g_player.data.playerid){
		  item.playername = g_player.data.playername;
		  item.top = parseInt(monthdata[i][4]);
		 }
		 g_toplist.monthdata.push(item);
		}
				
		 g_toplist.showToplist(0,0);
 		 g_msg.destroyload();
		}});
	}   catch  (e)   {
	    logerr(e.name);
	}   
}

var g_toplist = new Toplist();


