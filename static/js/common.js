
function stringToBytes (str) {  
  var ch, st, re = [];  
  for (var i = 0; i < str.length; i++ ) {  
    ch = str.charCodeAt(i);  // get char   
    st = [];                 // set up "stack"  
    do {  
      st.push( ch & 0xFF );  // push byte to stack  
      ch = ch >> 8;          // shift value down by 1 byte  
    }    
    while ( ch );  
    // add stack contents to result  
    // done because chars have "wrong" endianness  
    re = re.concat( st.reverse() );  
  }  
  // return an array of bytes  
  return re;  
}
  
function errorDB(err){
   alert("Error processing SQL: "+err.code);
}

log33 = function(text){
	 var div2=document.getElementById('testpos');
	 div2.innerHTML = text;
	 div2.style.color = 'black';
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
        page.buildSingleTab(titleImgName,this.name);
         var content =     "<div class='tab-pane in active' id='quest1'>";
         var pclass = "cfpage ";
         if (this.name=="signin") {
            pclass += "small";
         }else if (this.name=="bank") {
            pclass += "bank"
         }
        content += "<div class='"+pclass+"' id='"+this.pagename+"'>"
        content += "</div></div>"
        page.addContent(content);
        document.write(page.toString());    
        
        var pagedetail = new PageUtil(this.tagdetailname);
        var psubclass = "";
        if (this.name=="insure"||this.name=="event")
        	psubclass = "small";
        pclass = "cfpagedetail "+psubclass;
        content =     "<div class=\"tab-pane in active\" id='quest2'>";
        content += "<div class='"+pclass+"' id='"+this.pagedetailname+"'>"
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
		content +=     "        <div width='90%'>"
		content += "<input type='button' class='cf_bt pagingleft' value='上一页' onclick='"+clickCallback+"("+(currPage-1)+")'/>"
		content += "<span class='cf_span'>"+(currPage+1)+"/"+pageCount+"</span>"
		content += "<input type='button' class='cf_bt pagingright' value='下一页' onclick='"+clickCallback+"("+(currPage+1)+")'/>"
		content += "           </div>  "
		return content;
	},
    
    loadServerData:function(){
        //var dataobj = $.ajax({url:"../bbs/record.php",async:false});
        alert('999'+this.name);
        log('9999999');    
    },
    
    onClickHead:function(){
        if (this.name=='signin')
        	g_signin.doSignin(); 
    },    
    
    update:function(){
    	if (this.count==null)
    		this.count = 0;
		this.count += 1;
	   if (this.count%this.syncDuration==0) {
	        this.syncData();
	    }
    },    
	
	show:function(){
		this.buildPage(0);
          $('#'+this.tagname).modal({position:50,show: true});     
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
