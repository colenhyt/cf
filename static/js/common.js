   
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
        var sql = 'SELECT * FROM '+game_tables[module.name];
        g_db.query(sql,module.loadDataCallback);       
    },
    
    loadServerData:function(){
        //var dataobj = $.ajax({url:"../bbs/record.php",async:false});
        alert('999'+this.name);
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
