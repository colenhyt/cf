
Toplist = function(){
    this.name = "toplist";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
   this.pageheader = this.tagname+"header";
     this.tagtab1 = this.name+"tab1";
    this.tagtab2 = this.name+"tab2";
}

Toplist.prototype = new Datamgr();

Toplist.prototype.init = function(){
	var tdata = store.get(this.tagtab1);
	if (tdata==null)
	{
		store.set(this.tagtab1,data_toplistdata);
	} 
	tdata = store.get(this.tagtab2);
	if (tdata==null)
	{
		store.set(this.tagtab2,data_toplistdata2);
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
		  content += "<div class='panel' ID='insure_d1'><div class='panel-body'>没有排名</div>"
      	  content += "</div>"
	}else {
		  var start =0;
		  var end = tdata.length;
                  
                content += "<div class='cf_top_header'>";
 			 content += "        <table id='toplist1_tab' style='width:100%'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td class='td-c-value'>姓名</td>"
			 content += "               <td class='td-c-value'>资产</td>"
			 content += "               <td class='td-c-value'>"+desc+"</td>"
			 content += "               <td class='td-c-value'>赞</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
                content += "</div>";
		for (var i=start;i<end;i++){
			var item = tdata[i];
		     content += "<div>"
			 content += "        <table id='toplist1_tab' style='width:100%'>"
			 content += "             <tr>"
			 content += "               <td class='td-c-value'>"+item.id+"</td>"
			 content += "               <td class='td-c-value'>"+item.name+"</td>"
			 content += "               <td class='td-c-value'>"+item.score+"</td>"
			 content += "               <td class='td-c-value'><input type='button' class='cf_top_zan' onclick='g_toplist.zan("+item.id+")'>*33</td>"
			content += "              </tr>"
			 content += "             <tr>"
			 content += "               <td colspan='4'><img src='static/img/top_line.png'></td>"
			content += "              </tr>"
			content += "          </table>"
      		content += "</div>"
		}

	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
}

Toplist.prototype.zan = function(playerId)
{
    alert('zan'+playerId);
}

var g_toplist = new Toplist();
g_toplist.init();
