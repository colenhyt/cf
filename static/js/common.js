
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

function ForDight(Dight){  
    Dight = Math.round(Dight*Math.pow(10,2))/Math.pow(10,2);  
    return Dight;  
}

myajax = function(url,dataParam,async){
	if (async==null)
		async = false;
		
	var rsp = $.ajax({type:"post",url:"/cf/"+url+".do",data:dataParam,async:async});
	try    {
		if (rsp!=null&&rsp.responseText.length>0) {
			return eval ("(" + rsp.responseText + ")");
		}
	}   catch  (e)   {
	    document.write(e.name  +   " :  "   +  rsp.responseText);
	    return null;
	}	
}

obj2ParamStr = function(objName,objProp)
{
	var str = "";
	for (key in objProp){
		if (str.length>0)
			str += "&";
		str += objName+"."+key+"="+objProp[key];
	}
	return str;
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
        if (this.name=="insure"||this.name=="event"||this.name=="saving")
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
		content += "<span class='cf_paging'>"+(currPage+1)+"/"+pageCount+"</span>"
                var nextClick = ""
                if (currPage+1<pageCount) {
                  nextClick = " onclick='"+clickCallback+"("+(currPage+1)+")'";
                }
		content += "<input type='button' class='cf_bt pagingright' "+nextClick+" value='下一页'/>"
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
          $('#'+this.tagname).modal({position:Page_Top,show: true});     
	},

    onclick:function(clickX,clickY){
	     this.show();
    },    
    
    onPanelClick:function(id){
     	var tname = this.name+"_d";
		if (this.lastClickId!=null){
			var lastPanel = document.getElementById(tname+this.lastClickId);
			if (lastPanel!=null)
				lastPanel.style.background = "url('static/img/panel_bg.png') no-repeat";
		}
		var dd = document.getElementById(tname+id);
		dd.style.background = "url('static/img/panel_click.png') no-repeat";  
		this.lastClickId = id;  
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
	
	confirmBuy:function(id,qty){  
		if (this.name=="insure"&&qty==0){
			var tag = document.getElementById('finan_count');
			if (tag!=null)
				qty = tag.value;
		}
		if (qty==0){
		    g_msg.open2(this.cname+"购买","数量不能为零!");
		    return;
		}
			
	   var item = this.findItem(id);
	   
	   if (qty>0){
		    var needCash = item.price * qty;
		    var cash = g_player.saving[0].amount;
		    if (cash<needCash){
			    g_msg.open("你的现金不够，购买失败!");
			    return;
		    }		
	    }else {
	    	if (this.name=="stock"){
			   var pitem = g_player.getItemData(this.name,item);
			   if (pitem.qty<(0-qty)){
			   		g_msg.open("持有数量少于你抛售数量!");	
			   		return;
			   }
	    	}else {
	    		return;
	    	}
	    	
	    }

		var strUnit = "份";
		var strDesc = "确定";
		var qtyStr = qty;
		if (this.name=="stock"){
			strUnit = "股";
			if (qty>0){
				strDesc += "加持";
			}else {
				strDesc += "减持";
				qtyStr = 0 - qtyStr;
			}
		}else
			strDesc += "购买";
			
		strDesc += " <span style='color:red'>"+qtyStr+"</span> "+strUnit+""+item.name+"产品?";
		var doPath = "g_"+this.name+".buy";
		g_msg.open2(item.name+" "+this.cname,strDesc,doPath,id,qty);
	},
	
	buy:function(id,qty){
	    var ret = g_player.buyItem(this.name,id,qty);
	    if (ret==true){
	    	//刷新detail 页面:
	    	if (this.name=='insure')
	    		this.closeDetail();
	    	else
	    		this.showDetail(id,true);
	    	
	    	//刷新list 页面:
	    	this.buildPage(0);
	    	//$('#'+this.tagdetailname).modal('hide');
	    }
	},
}

Datamgr.prototype.constructor = Datamgr;
