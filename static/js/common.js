a = [
	{a22:2},
	{a1:15},
	{a22:1},
	{a1:4},
	{a1:6},
]
function test1(a,b){
	if (a.a1==null)
		a.a1 = 1;
	if (b.a1==null)
		b.a1 = 1;
	return (a.a1-b.a1);
}
a.sort(test1);
//for (var i=0;i<a.length;i++)
//alert(a[i].a1);   
function errorDB(err){
   alert("Error processing SQL: "+err.code);
}

log = function(text){
	 var console=document.getElementById('console');
	console.innerHTML = text;
	//alert(text);
}

logerr = function(text){
	 var console=document.getElementById('consoleerr');
	console.innerHTML = text;
}

itemStr = function(items,split){
    var itemDesc = "";
    for (var i=0;i<items.length;i++){
    	var item = items[i];
    	if (item.t==ITEM_TYPE.CASH)
    		itemDesc += item.v+ITEM_NAME.CASH+split;
    	else if (item.t==ITEM_TYPE.EXP)
    		itemDesc += item.v+ITEM_NAME.EXP+split;
    }
    return itemDesc;
}

Datamgr = function(){   
}

Datamgr.prototype = {        

	buildHTML:function(){
		var page = new PageUtil(this.tagname);
		var titleImgName = "title_"+this.name+".png";
		page.buildSingleTab(titleImgName);
		var content = 	"<div class='tab-pane in active' id='quest1'>";
		content += "<div class='cfpage' id='"+this.pagename+"'>"
	    content += "</div></div>"
		page.addContent(content);
		document.write(page.toString());	
		
//		var pagedetail = new PageUtil(this.tagdetailname);
//		content = 	"<div class=\"tab-pane in active\" id='quest2'>";
//		content += "<div class=\"cfpage\" id='"+this.pagedetailname+"'>"
//	    content += "</div></div>"
//		pagedetail.addContent(content);
//		document.write(pagedetail.toString());	
	},


	buildPaging:function(currPage,itemCount)
	{
		var content = ""
		var pageCount = parseInt(itemCount/this.pageCount);
		var tmodel = itemCount%this.pageCount;
		if (tmodel>0)
			pageCount++;

		if (pageCount==1) 
			return content;
			
		var clickCallback = "g_"+this.name+".buildPage";
		content +=     "        <div>"
		content += "<input type='button' class='button_paging' value='上一页' onclick='"+clickCallback+"("+(currPage-1)+")'/>"
		content += ""+(currPage+1)+"/"+pageCount
		content += "<input type='button' class='button_paging' value='下一页' onclick='"+clickCallback+"("+(currPage+1)+")'/>"
		content += "           </div>  "
		return content;
	},
	
	show:function(){
		this.buildPage(0);
    	$('#'+this.tagname).modal('show');   
	},
    
    loadServerData:function(){
        //var dataobj = $.ajax({url:"../bbs/record.php",async:false});
        alert('999'+this.name);
        log('9999999');    
    },
    
    update:function(){
		this.count += 1;
	        log(this.name+this.count);
		if (this.count>=this.syncDuration) {
		    this.syncData();
		    this.count = 0;
		}
    },    

    onclick:function(clickX,clickY){
	     this.show();
    },    
}

Datamgr.prototype.constructor = Datamgr;
