
function gameInit()
{
	gameInit_db();
}



function gameInit_db(){
 
	function createDB(tx){
		tx.executeSql('CREATE TABLE IF NOT EXISTS t_toplist(id unique,type,top,name,score)');
	}
   
	function initDB_success(){
		//alert('init successful');
	}

	var db = window.openDatabase(g_dbname,g_dbversion,g_dbfile,g_dbsize);
	db.transaction(createDB,errorDB,initDB_success);
}

gameInit()