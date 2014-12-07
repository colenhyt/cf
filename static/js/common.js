   
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

	buildHTML:function(titleImgName){
		var page = new PageUtil(this.tagname);
		page.buildSingleTab(titleImgName);
		var content = 	"<div class='tab-pane in active' id='quest1'>";
		content += "<img src='static/img/pop_big.png' style='position:relative;right:20px'>"
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
