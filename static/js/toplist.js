
function addRow(obj)
{
	//alert('ddd addrow');
	//���һ��
	var tab = document.getElementById(obj);
	for (var i=0;i<10 ; i++)
	{
	var newTr = tab.insertRow(i+1);
	//�����
	var newTd0 = newTr.insertCell();
	var newTd1 = newTr.insertCell();
	var newTd2 = newTr.insertCell();
	//���������ݺ�����
	newTd0.innerHTML = '9999'; 
	newTd1.innerText= '��С��';
	newTd2.innerText= i;
	}
}

function getdata(args) {
    //code
}