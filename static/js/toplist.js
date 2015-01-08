
Toplist = function(){
    this.name = "toplist";
     this.pageCount = 5;
    this.currPage = 0;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
   this.pageheader = this.tagname+"header";
     this.tagtab1 = this.name+"tab1";
    this.tagtab2 = this.name+"tab2";
    this.zandata = this.name+"zan";    
    this.syncDuration = 5;
}

Toplist.prototype = new Datamgr();

Toplist.prototype.init = function(){
store.remove(this.tagtab1);
store.remove(this.tagtab2);
	var tdata = store.get(this.tagtab1);
	if (tdata==null)
	{
		store.set(this.tagtab1,[]);
	} 
	tdata = store.get(this.tagtab2);
	if (tdata==null)
	{
		store.set(this.tagtab2,[]);
	} 
	var pzan = store.get(this.zandata);
	if (pzan==null){
		store.set(this.zandata,{});
	}	
	this.syncData2();
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
}

Toplist.prototype.buildPage = function(page)
{
	this.currPage = page;
	this.showToplist(0,0);
}

Toplist.prototype.showToplist = function(type,page)
{
	if (page<0)
		return
		
	var tdata;
        var   header ="";
        var desc = "";
	if (type==0) {
	    tdata = store.get(this.tagtab1);
            desc = "本周排名";
            header += "<button class='cf_title_bg moff' onclick='g_toplist.showToplist(1,0)'></button>"
            header += "<button class='cf_title_bg won'></button>"
	}
	else if (type==1){
		tdata = store.get(this.tagtab2);
            desc = "本月排名";
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
 			 content += "        <table id='toplist1_tab' class='cf_top_header'>"
			 content += "             <tr>"
			 content += "               <td class='cftoplist_content'>姓名</td>"
			 content += "               <td class='cftoplist_content'>资产</td>"
			 content += "               <td class='cftoplist_td'>"+desc+"</td>"
			 content += "               <td class='cftoplist_c2'>赞</td>"
			content += "              </tr>"
			content += "          </table>"
			 content += "        <table id='toplist1_tab'>"
  
			 var me = g_player.data;
			 me.top = me.weektop;
			 me.money = g_player.getTotalMoney();
			 if (type==1) 
			  me.top = me.monthtop;
			
			var items = []
			items.push(me);
			for (var i=0;i<tdata.length;i++){
				var item = tdata[i];
				if (item.playerid==me.playerid)continue;
				item.top = i+1;
				items.push(item);
			}
						 
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>items.length)
			end = items.length;
		for (var i=start;i<end;i++){
			var item = items[i];
			if (i==0)
			{
			 content += "             <tr>"
			 content += "               <td class='cftoplist_content' style='color:yellow'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+item.playername.substring(0,8)+"</div></td>"
			 content += "               <td class='cftoplist_c5' style='color:yellow'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+item.money+"</div></td>"
			 content += "               <td class='cftoplist_c4' style='color:yellow'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+item.top+"</div></td>"
			 content += "               <td class='cftoplist_c3' style='color:yellow'><div class='cf_top_zan_me'><span class='cftoplist_c6 me'>*"+item.zan+"</span></div></td>"
			content += "              </tr>"			
			}else {
			 content += "             <tr>"
			 content += "               <td class='cftoplist_content'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+item.playername.substring(0,8)+"</div></td>"
			 content += "               <td class='cftoplist_c5'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+item.money+"</div></td>"
			 content += "               <td class='cftoplist_c4'><div onclick='g_playerinfo.showOneInfo("+item.playerid+")'>"+item.top+"</div></td>"
			 content += "               <td class='cftoplist_c3'><div onclick='g_toplist.zan("+page+","+item.playerid+")'<input type='button' class='cf_top_zan'/><span class='cftoplist_c6'>*<span id='zan_"+item.playerid+"'>"+item.zan+"</span></span></div></td>"
			content += "              </tr>"
			}
			 content += "             <tr>"
			 content += "               <td colspan='4'><img src='static/img/top_line.png'></td>"
			content += "              </tr>"
		}
			content += "          </table>"
                content += "</div>";

		this.currPage = page;
        content += this.buildPaging(page,tdata.length,"g_toplist.showToplist",type);
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
}

Toplist.prototype.zan = function(page,playerId)
{
	if (playerId==g_player.data.playerid){
		g_msg.tip("不能给自己点赞");
		return;	
	}
	
	var ZAN_COUNT = 3;
	var now = new Date();
	var zandata = store.get(this.zandata);
	var key = now.getFullYear()+"-"+now.getMonth()+"-"+now.getDate();
	var zans = zandata[key];
	if (zans==null){
		zans = [];
	}else if (zans.length>=ZAN_COUNT){
		g_msg.tip("每天点赞不能多于"+ZAN_COUNT+"次");
		return;
	}else {
		for (var i=0;i<zans.length;i++){
			if (zans[i]==playerId){
				g_msg.tip("你每天只能对一个人点赞1次");
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
	
    var tdata1 = store.get(this.tagtab1);
    for (var i=0;i<tdata1.length;i++){
        if (tdata1[i].playerid==playerId) {
            tdata1[i].zan = zanCount;
             break;
        }
    }
    store.set(this.tagtab1,tdata1);
	
    var tdata2 = store.get(this.tagtab2);
    for (var i=0;i<tdata2.length;i++){
        if (tdata2[i].playerid==playerId) {
            tdata2[i].zan = zanCount;
             break;
        }
    }
    store.set(this.tagtab2,tdata2);
        
	zanTag.innerHTML = zanCount;
	
	zans.push(playerId);
	zandata[key] = zans;
	store.set(this.zandata,zandata);	 
	 
}

Toplist.prototype.updateData = function(data){
	if (data.length>0){
		store.set(this.tagtab1,data[0]);
		store.set(this.tagtab2,data[1]);
	}
}

Toplist.prototype.syncCallback=function(dataobj){
	try    {
		var obj = eval ("(" + dataobj + ")");
		g_toplist.updateData(obj);
	}   catch  (e)   {
	    logerr(e.name  +   " :  "   +  dataobj);
	}   
	
}

Toplist.prototype.syncData2 = function(){
	try  {
		$.ajax({type:"post",url:"/cf/toplist_list.do",success:this.syncCallback});
	}   catch  (e)   {
	    logerr(e.name);
	}   
}

var g_toplist = new Toplist();
store.remove(g_toplist.zandata);
g_toplist.init();

