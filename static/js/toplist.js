
Toplist = function(){
    this.name = "toplist";
}

Toplist.prototype = new Datamgr();

Toplist.prototype.init = function(){
    g_game.register(this);
}

Toplist.prototype.loadDataCallback = function(tx,results){
	var tab = document.getElementById('toplist1_tab');
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

var g_toplist = new Toplist();
g_toplist.init();
