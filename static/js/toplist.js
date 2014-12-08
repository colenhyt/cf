
Toplist = function(){
    this.name = "toplist";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
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
		var titleImgName = "title_"+this.name+".png";
		page.buildSingleTab(titleImgName);
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
	if (page==0)
		tdata = store.get(this.tagtab1);
	else if (page==1)
		tdata = store.get(this.tagtab2);
		
	var content = 	"";
	  content += "<div ID='insure_d1'>"
	  content += "<input type='button' class='form-control' onclick='g_toplist.buildPage(0)' value='单日排名'/>"		  
	  content += "<input type='button' class='form-control' onclick='g_toplist.buildPage(1)' value='本月排名'/>"		  
	  content += "</div>"
	  
	  
	if (tdata.length<=0){
		  content += "<div class='panel' ID='insure_d1'><div class='panel-body'>没有排名</div>"
      	  content += "</div>"
	}else {
		  var start =0;
		  var end = tdata.length;
		for (var i=start;i<end;i++){
			var item = tdata[i];
		     content += "<div>"
			 content += "        <table id='toplist1_tab' style='width:100%'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td class='td-c-value'>"+item.id+"</td>"
			 content += "               <td class='td-c-value'>"+item.name+"</td>"
			 content += "               <td class='td-c-value'>"+item.score+"</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
      		content += "</div>"
		}

	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
}


var g_toplist = new Toplist();
g_toplist.init();
