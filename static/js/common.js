   
function errorDB(err){
   alert("Error processing SQL: "+err.code);
}

log = function(text){
	 var console=document.getElementById('console');
	console.innerHTML = text;
	//alert(text);
}


Datamgr = function(){   
}

Datamgr.prototype = {        
    loadData:function(){
        var module = this;
	   function queryData(tx){
           tx.executeSql('SELECT * FROM '+game_tables[module.name],[],module.loadDataCallback,errorDB);
	   }
 
	   var db = window.openDatabase(g_dbname,g_dbversion,g_dbfile,g_dbsize); 
	   db.transaction(queryData,errorDB);          
    },
    
    loadServerData:function(){
        //var dataobj = $.ajax({url:"../bbs/record.php",async:false});
        log('9999999');    
    },
    
    update:function(){
        log(this.name);
    },    

    onclick:function(clickX,clickY){
        var options = {
            keyboard : false,
            show     : true
        };
        
        this.loadData();
        $('#my'+this.name).modal(options);       
    },    
}

Datamgr.prototype.constructor = Datamgr;
