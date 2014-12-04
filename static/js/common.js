   
function errorDB(err){
   alert("Error processing SQL: "+err.code);
}

log = function(text){
	 var console=document.getElementById('console');
	console.innerHTML = text;
	//alert(text);
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
    loadData:function(){
        var module = this;
        var sql = 'SELECT * FROM '+game_tables[module.name];
        g_db.query(sql,module.loadDataCallback);       
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
