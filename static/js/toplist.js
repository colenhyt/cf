
//
 Toplist_querySuccess = function(tx,results){
   var len = results.rows.length;
	var tab = document.getElementById('top1_tab');
	for (var i=0;i<len ; i++)
	{
		var item = results.rows.item(i);
	var newTr = tab.insertRow(i+1);
	//�����
	var newTd0 = newTr.insertCell();
	var newTd1 = newTr.insertCell();
	var newTd2 = newTr.insertCell();
	//����������
	newTd0.innerHTML = item.score; 
	newTd1.innerText= item.name;
	newTd2.innerText= item.top;
	}
   
}

//load server data and save to loal db
Toplist_loadServerData = function(param) {

}

//fill local db data to page
Toplist_fillLocalData = function(param) {

	function queryData(tx){
		tx.executeSql('SELECT * FROM '+g_table_top,[],Toplist_querySuccess,errorDB);
	}

	var db = window.openDatabase(g_dbname,g_dbversion,g_dbfile,g_dbsize);
	db.transaction(queryData,errorDB);
}
