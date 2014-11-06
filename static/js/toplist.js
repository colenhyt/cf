
function addRow(obj)
{
	//alert('ddd addrow');
	//添加一行
	var tab = document.getElementById(obj);
	for (var i=0;i<10 ; i++)
	{
	var newTr = tab.insertRow(i+1);
	//添加列
	var newTd0 = newTr.insertCell();
	var newTd1 = newTr.insertCell();
	var newTd2 = newTr.insertCell();
	//设置列内容和属性
	newTd0.innerHTML = '9999'; 
	newTd1.innerText= '孙小雨';
	newTd2.innerText= i;
	}
}

function getdata(args) {
    //code
}