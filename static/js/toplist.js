
//
 Toplist_querySuccess = function(tx,results){
   var len = results.rows.length;
	var tab = document.getElementById('top1_tab');
	for (var i=0;i<len ; i++)
	{
		var item = results.rows.item(i);
	var newTr = tab.insertRow(i+1);
	//添加列
	var newTd0 = newTr.insertCell();
	var newTd1 = newTr.insertCell();
	var newTd2 = newTr.insertCell();
	//设置列内容
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

function Parent(nn,aa){ 
this.name=nn; 
this.age=aa; 
this.lev=function(){ 
return this.name; 
};
};

Parent.prototype.bb = function(){
	alert('bbb');
};
var x =new Parent("ttt",120); 
var x2 =new Parent("yyy",333); 
alert(x2.age);
alert(x.lev()); 
x2.bb();