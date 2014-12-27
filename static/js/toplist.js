
Toplist = function(){
    this.name = "toplist";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
   this.pageheader = this.tagname+"header";
     this.tagtab1 = this.name+"tab1";
    this.tagtab2 = this.name+"tab2";
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
}

Toplist.prototype.buildPage = function(page)
{
	if (page<0)
		return
		
	var tdata;
        var   header ="";
        var desc = "";
	if (page==0) {
	    tdata = store.get(this.tagtab1);
            desc = "本周排名";
            header += "<button class='cf_title_bg moff' onclick='g_toplist.buildPage(1)'></button>"
            header += "<button class='cf_title_bg won'></button>"
	}
	else if (page==1){
		tdata = store.get(this.tagtab2);
            desc = "本月排名";
           header += "<button class='cf_title_bg woff' onclick='g_toplist.buildPage(0)'></button>"
            header += "<button class='cf_title_bg mon'></button>"
        }        	
	var tagHeader = document.getElementById(this.pageheader);
	tagHeader.innerHTML = header;
		
	var content = 	"";
	  
	if (tdata.length<=0){
		  content += "<div class='cfpanel' ID='insure_d1'>目前还没有排名数据"
      	  content += "</div>"
	}else {
		  var start =0;
		  var end = tdata.length;
                  
                content += "<div class='cf_top'>";
 			 content += "        <table id='toplist1_tab' class='cf_top_header'>"
			 content += "             <tr>"
			 content += "               <td width='180px'>姓名</td>"
			 content += "               <td width='160px'>资产</td>"
			 content += "               <td class='cftoplist_td'>"+desc+"</td>"
			 content += "               <td class='cftoplist_td'>赞</td>"
			content += "              </tr>"
			content += "          </table>"
			 content += "        <table id='toplist1_tab'>"
		for (var i=start;i<end;i++){
			var item = tdata[i];
			 content += "             <tr>"
			 content += "               <td class='cftoplist_td'><div onclick='g_bank.showBank("+item.playerid+")'>"+item.playername.substring(0,8)+"</div></td>"
			 content += "               <td class='cftoplist_td'>"+item.money+"</td>"
			 content += "               <td class='cftoplist_td'>"+(i+1)+"</td>"
			 content += "               <td class='cftoplist_td'><input type='button' class='cf_top_zan' onclick='g_toplist.zan("+page+","+item.playerid+")'>*<span id='zan_"+item.playerid+"'>"+item.zan+"</span></td>"
			content += "              </tr>"
			 content += "             <tr>"
			 content += "               <td colspan='4'><img src='static/img/top_line.png'></td>"
			content += "              </tr>"
		}
			content += "          </table>"
                content += "</div>";

	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
}

Toplist.prototype.zan = function(page,playerId)
{
    var zanTag = document.getElementById('zan_'+playerId);
    var zanCount = parseInt(zanTag.innerHTML)+1;
	try  {
		var data = "toplist.playerid="+playerId+"&toplist.zan="+zanCount;
		$.ajax({type:"post",url:"/cf/toplist_zan.do",data:data});
	}   catch  (e)   {
	    document.write(e.name);
	}   
	
    var dbName = this.tagtab1;
    if (page==1) {
        dbName = this.tagtab2;
    }
    var tdata = store.get(dbName);
    for (var i=0;i<tdata.length;i++){
        if (tdata[i].playerid==playerId) {
            tdata[i].zan = zanCount;
             break;
        }
    }
    store.set(dbName,tdata);
	zanTag.innerHTML = zanCount;
	 
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
g_toplist.init();
