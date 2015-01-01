
function stringToBytes (str) {  
  var ch, st, re = [];  
  for (var i = 0; i < str.length; i++ ) {  
    ch = str.charCodeAt(i);  // get char   
    st = [];                 // set up "stack"  
    do {  
      st.push( ch & 0xFF );  // push byte to stack  
      ch = ch >> 8;          // shift value down by 1 byte  
    }    
    while ( ch );  
    // add stack contents to result  
    // done because chars have "wrong" endianness  
    re = re.concat( st.reverse() );  
  }  
  // return an array of bytes  
  return re;  
}
  
function errorDB(err){
   alert("Error processing SQL: "+err.code);
}

log33 = function(text){
	 var div2=document.getElementById('testpos');
	 div2.innerHTML = text;
	 div2.style.color = 'black';
}

log = function(text){
	var name = "console";
	 var div=document.getElementById(name);
	if (div==null){
	    var div = document.createElement("div");
	    div.id = name;
	    document.body.appendChild(div);	
    }	 
	div.innerHTML = text;
	//alert(text);
}

logerr = function(text){
	var name = "consoleerr";
	 var div=document.getElementById(name);
	if (div==null){
	    var div = document.createElement("div");
	    div.id = name;
	    document.body.appendChild(div);	
    }	
	div.innerHTML = text;
}

ForDight = function(Dight){  
    Dight = Math.round(Dight*Math.pow(10,2))/Math.pow(10,2);  
    return Dight;  
}

cfeval = function(jsonStr){  
	if (jsonStr==null)
		return null;
    return eval ("(" + jsonStr + ")");
}

myajax = function(url,dataParam,async){
	if (async==null)
		async = false;
		
	var rsp = $.ajax({type:"post",url:"/cf/"+url+".do",data:dataParam,async:async});
	try    {
		if (rsp!=null&&rsp.responseText.length>0) {
			return eval ("(" + rsp.responseText + ")");
		}
	}   catch  (e)   {
	    document.write(e.name  +   " :  "   +  rsp.responseText);
	    return null;
	}	
}

obj2ParamStr = function(objName,objProp)
{
	var str = "";
	for (key in objProp){
		if (str.length>0)
			str += "&";
		str += objName+"."+key+"="+objProp[key];
	}
	return str;
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

computeUnit = function(startDate,endDate){
	var mills = Date.parse(endDate) - Date.parse(startDate);
	return parseInt(mills/TimeUnit);
}

randomItems = function(items,existItems,count){
	var ritems = [];
	if (items==null||items.length<=0||count<=0) return ritems;
	
	if (items.length<count)
		count = items.length;
		
	var data = items.concat();
	if (existItems!=null&&existItems.length>0){
		if (existItems.length>=count)
			return existItems;
			
		for (var i=0;i<existItems.length;i++){
			ritems.push(existItems[i]);
			for (var j=0;j<data.length;j++){
				if (data[j]==existItems[i]){
					data.splice(j,1);
					break;
				}
			}
		}	
		count -= existItems.length;
	}
	
	for (var i=0;i<count;i++){
		var index = Math.floor(Math.random()*data.length);
		ritems.push(data[index]);
		data.splice(index,1);
	}
	
	return ritems;
}