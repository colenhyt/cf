
Datamgr = function(){   
}

Datamgr.prototype = {        

	buildHTML:function(){
		if (this.name!="saving")
		{
	         var page = new PageUtil(this.tagname);
	        var titleImgName = "title_"+this.name+".png";
	        page.buildSingleTab(titleImgName,this.name);
	         var content =     "<div class='tab-pane in active'>";
	         var pclass = "cfpage ";
	         if (this.name=="bank") {
	            pclass += "bank"
	         }else if (this.name=="playerinfo") {
	            pclass += "player"
	         }
	        content += "<div class='"+pclass+"' id='"+this.pagename+"'>"
	        content += "</div></div>"
	        page.addContent(content);
	        document.write(page.toString());   
		}
         
        var page = this;
		$('#'+this.tagname).on('hide.zui.modal', function()
		{
			if (page.onClose)
		  		page.onClose();
		}) 
	         
 		var tag = document.getElementById(this.tagname+"_dialog");
 		if (tag){
		 tag.style.setProperty("width",getSizes().PageWidth);
		 tag.style.setProperty("height","800px");
 		}
        
        var pagedetail = new PageUtil(this.tagdetailname);
        var psubclass = "";
        if (this.name=="saving")
           psubclass = "small saving"
        else if (this.name=="event")
        	psubclass = "small";
        else
        	psubclass = this.name;
        pclass = "cfpagedetail "+psubclass;
        content =     "<div class=\"tab-pane in active\" id='quest2'>";
        content += "<div class='"+pclass+"' id='"+this.pagedetailname+"'>"
        content += "</div></div>"
        pagedetail.addContent(content);
        document.write(pagedetail.toString());    
		
	},


	buildPaging:function(currPage,itemCount,onClick,type)
	{
		var content = ""
		var pageCount = parseInt(itemCount/this.pageCount);
		var tmodel = itemCount%this.pageCount;
		if (tmodel>0)
			pageCount++;

		if (pageCount==1) 
			return content;
			
		var clickCallback = "g_"+this.name+".buildPage";
		if (onClick)
		 clickCallback = onClick;
		content +=     "        <div width='90%'>"
		if (type==null)
		content += "<input type='button' class='cf_bt pagingleft' value='上一页' onclick='"+clickCallback+"("+(currPage-1)+")'/>";
		else
		content += "<input type='button' class='cf_bt pagingleft' value='上一页' onclick='"+clickCallback+"("+type+","+(currPage-1)+")'/>";
		
		content += "<span class='cf_paging'>"+(currPage+1)+"/"+pageCount+"</span>"
        var nextClick = ""
        if (currPage+1<pageCount) {
 			if (type==null)
                  nextClick = " onclick='"+clickCallback+"("+(currPage+1)+")'";
 			else
                  nextClick = " onclick='"+clickCallback+"("+type+","+(currPage+1)+")'";
        }
		content += "<input type='button' class='cf_bt pagingright' "+nextClick+" value='下一页'/>"
		content += "           </div>  "
		return content;
	},
    
	onClose:function()
	{
	
		playAudioHandler('close1');	
		//alert(this.name+"close");
    	var tag = document.getElementById("tag"+this.name);
    	if (tag&&this.name!=g_playerinfo.name){
    		tag.innerHTML = ""
    	}		
	},
	
    loadServerData:function(){
        //var dataobj = $.ajax({url:"../bbs/record.php",async:false});
        alert('999'+this.name);
        log('9999999');    
    },
    
    onClickHead:function(){
    },    
    
    update:function(){
    	if (this.count==null)
    		this.count = 0;
		this.count += 1;
	   if (this.count%this.syncDuration==0&&this.syncData!=null) {
	        this.syncData();
	    }
    },    
	
	hide:function(pagename){
	playAudioHandler('close1');	
		if (pagename==null)
		$('#'+this.tagname).modal('hide');  
		else
		$('#'+pagename).modal('hide');  
	},

    onclick:function(clickX,clickY){
    	var tag = document.getElementById("tag"+this.name);
    	if (tag&&this.name!=g_playerinfo.name){
    		var name = "g_"+this.name;
    		var className = "cf"+this.name+"_clickon"
    		tag.innerHTML = "<img src='static/img/icon_"+this.name+"_on.png' onclick='"+name+".onclick()' class='"+className+"'>"
    	}
	     this.show();
    },    
	
	show:function(){
		var myDate = new Date();
		var ss = myDate.getSeconds(); 
		var ms = myDate.getMilliseconds();	
		this.buildPage(0);
		playAudioHandler('open1');	
        $('#'+this.tagname).modal({position:getSizes().PageTop,show: true}); 
        var myDate22 = new Date();
		var ss2 = myDate22.getSeconds(); 
		var ms2 = myDate22.getMilliseconds();
		//g_msg.tip("cost: "+((ss2-ss)*1000+(ms2-ms))); 
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
	
	confirmBuy:function(id,qty,price){  
		if (qty==0){
		    g_msg.tip(this.cname+"购买","数量不能为零!");
		    return;
		}
			
	   var item = store.get(this.name)[id];
	   if (!item){
	   		g_msg.tip("找不到"+this.name+"数据");
	   		return;
	   }
	   var ps = 0;
	   if (price&&price>0)
	   	ps = price;
	   else
	    ps = item.price;
	    
	   if (qty>0){
		    var needCash = ps * qty;
		    var cash = g_player.saving[1].amount;
		    if (cash<needCash){
			    g_msg.tip("您的现金不够，购买失败!");
			    return;
		    }		
	    }else {
	    	if (this.name=="stock"){
			   var pitem = g_player.getStockItem(id);
			   if (pitem.qty<(0-qty)){
			   		g_msg.tip("持有数量少于您抛售数量!");	
			   		return;
			   }
	    	}else {
	    		return;
	    	}
	    	
	    }

		this.reqBuy(id,qty,ps);
		return;
	},	

	buy:function(id,qty,ps){
	    var ret = g_player.buyItem(this.name,id,qty,ps);
	    if (ret.ret==true){
	    	var item = ret.item;
	    	//tip:
			if (qty>0)
				g_msg.tip("购买<span style='color:red'>"+item.name+"</span>成功,金额:"+item.amount);
			else
				g_msg.tip("抛售<span style='color:red'>"+item.name+"</span>成功,金额:"+(0-item.amount));
			    	
	    	//刷新detail 页面:
	    	if (this.name=='insure')
	    		this.closeDetail();
	    	else
	    		this.showDetail(id,true);
	    	
	    	//刷新list 页面:
	    	this.buildPage(this.currPage);
	    	//$('#'+this.tagdetailname).modal('hide');
	    }
	},
}

Datamgr.prototype.constructor = Datamgr;
