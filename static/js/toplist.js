
Toplist = function(){
    this.name = "toplist";
}

Toplist.prototype.init = function(){

}

Toplist.prototype.querySuccess = function(tx,results){
	var tab = document.getElementById('top1_tab');
	var rowLength = tab.rows.length;
	for (var i=1 ; i< rowLength; i++)
    {
        tab.deleteRow(1);
   }

   var len = results.rows.length;
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
Toplist.prototype.loadServerData = function(param) {
    alert(this.name);
}

//
Toplist.prototype.loadData = function(){

	function queryData(tx){
		tx.executeSql('SELECT * FROM '+g_table_top,[],g_toplist.querySuccess,errorDB);
	}

	var db = window.openDatabase(g_dbname,g_dbversion,g_dbfile,g_dbsize);
	db.transaction(queryData,errorDB);
}

//on close page
Toplist.prototype.onclose = function() {
    
}

Toplist.prototype.onclick = function(clickX,clickY){
        var options = {
            keyboard : false,
            show     : true
        };
        this.loadData();
        $('#myToplist').modal(options);       
}

var g_toplist = new Toplist();
g_toplist.init();
