  
function errorDB(err){
   alert("Error processing SQL: "+err.code);
}

log = function(text){
	var name = "console";
	 var div=document.getElementById(name);
	if (div==null){
	    var div = document.createElement("div");
	    div.id = name;
	    document.body.appendChild(div);	
    }	 
	div.innerHTML = text;
	//alert(text);
}

logerr = function(text){
	var name = "consoleerr";
	 var div=document.getElementById(name);
	if (div==null){
	    var div = document.createElement("div");
	    div.id = name;
	    document.body.appendChild(div);	
    }	
	div.innerHTML = text;
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
	var img = game_page_imgs["pop_big.png"];
	var width = Math.floor(img.width *100 /Scene_Width);
	var height = Math.floor(img.height *100 /Scene_Height);
		var content = "<div id='"+this.pagename+"'>"
	   // content += "<img src='static/img/pop_big.png'' style='width:"+width+"%;height:"+height+"%;padding-left:10px'></div>"
	    content += "<img src='static/img/pop_big.png'' style='padding-left:10px'></div>"
		page.addContent(content);
		document.write(page.toString());	
		
		var pagedetail = new PageUtil(this.tagdetailname);
		content = 	"<div class=\"tab-pane in active\" id='quest2'>";
		content += "<div class=\"cfpage\" id='"+this.pagedetailname+"'>"
	    content += "</div></div>"
		pagedetail.addContent(content);
		document.write(pagedetail.toString());	
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
	
	show:function(){
		this.buildPage(0);
    	$('#'+this.tagname).modal('show');   
	},

    onclick:function(clickX,clickY){
	     this.show();
    },    
    
	findItem:function(id){
		var tdata = store.get(this.name);
		var item;
		for (var i=0;i<tdata.length;i++){
			if (tdata[i].id==id){
				item = tdata[i];
				break;
			}
		}
		return item;
	},	
}

Datamgr.prototype.constructor = Datamgr;
