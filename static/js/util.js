
/**  
* function for get the style value in special css file  
* @param int css_file_id  
* @param String labname  
* @param String param  
*/  
function getStyleValue(css_file_name,labname,param)  
{  
	var tar;  
	var rss;  
	var style;  
	var value;  
	var tar;
	for (key in document.styleSheets){
		var index = document.styleSheets[key].href.indexOf(css_file_name);
		if (index>0){
			tar = document.styleSheets[key];
			break;
		}
	}
	if (!tar)
		return;
		
	rss = tar.cssRules?tar.cssRules:tar.rules  

	for(i=0;i<rss.length;i++ )  
	{  
	 style = rss[i];  
	 if(style.selectorText&&style.selectorText.toLowerCase() == labname.toLowerCase())  
	 {  
	  value = style.style[param];  
	  break;
	 }  
	}  
	return value;  
} 

function getCSSValue(labname,param)  
{ 
	return getStyleValue("cf.css","."+labname,param);
}

function uncamelize(s, sep) { 
sep = sep || '-'; 
return s.replace(/([a-z])([A-Z])/g, function (match, p1, p2){ 
return p1 + sep + p2.toLowerCase(); 
}); 
} 


function isLegalName(param){
	var ii = param.indexOf('-y');
	var ii2 = param.indexOf('-x');
	var unlegls = ["backgroundRepeat","cssText","backgroundClip"
	,"borderRight","borderLeft","backgroundImage"];
	var found = false;
	for (var i=0;i<unlegls.length;i++){
		var index = param.indexOf(unlegls[i]);
		if (index>=0){
			found = true;
			break;
		}
	}	
	return !found&&!param.match(/^[0-9].*$/)&&ii==-1&&ii2==-1;
}

function isLegalValue(vv){
	var unlegls = ["initial"];
	var found = false;
//	for (var i=0;i<unlegls.length;i++){
//		var index = vv.toLowerCase().indexOf(unlegls[i]);
//		if (index>=0){
//			found = true;
//			break;
//		}
//	}	
	return vv.length>0&&vv!="initial";
}

function outputCssStyles(css_file_name,per) {
	var tar;
	for (key in document.styleSheets){
		var index = document.styleSheets[key].href.indexOf(css_file_name);
		if (index>0){
			tar = document.styleSheets[key];
			break;
		}
	}
	if (!tar)
		return;
		
	rss = tar.cssRules?tar.cssRules:tar.rules  
	
	var content = "";

	for(i=0;i<rss.length;i++ )  
	{  
	 style = rss[i]; 
	 	content += style.selectorText.toLowerCase()+"{"
	 	for (param in style.style){
			var value = style.style[param];
			if (param=='background-repeat')
			{
				var a = 0;
			}
	 		if (isLegalName(param)&&isLegalValue(value)){
	 		 value = value.replace('(http://localhost:8080/cf/',"('../../");
	 		 value = value.replace('.png)',".png')");
			 var index = value.indexOf("px");
			 if (index>0){
			  var pxva = value.substr(0,index);
			  value = parseInt(parseInt(pxva)*per)+"px";
			  //g_msg.tip(pxva);
			 }
	 			//content += param+":"+value+";";
	 			content += uncamelize(param,'-')+":"+value+";";
//		 		if (param=='backgroundOrigin'){
//		 			alert(style.style[param]);
//		 		}
	 		}
	 	} 
	 	content += "}<br><br>";
	}	
	//alert(content);
	return content;	
}

function outputCssFiles(twidth) {
	var tag = document.createElement("DIV");
	var per = (twidth/640);
	tag.innerHTML = outputCssStyles("cf640.css",per);
	tag.style.color = "#000000"
	tag.width = "500px"
    tag.height = "600px";
    document.body.appendChild(tag);	
}

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
if (g_msg)
g_msg.tip(text);
return;
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
	g_msg.open(text);
}

ForDight = function(Dight,point){  
	var pp = 2;
	if (point>pp)
		pp = point;
    Dight = Math.round(Dight*Math.pow(10,pp))/Math.pow(10,pp);  
    return Dight;  
}

cfeval = function(jsonStr){  
	if (jsonStr==null||jsonStr.length<=0)
		return null;
    return eval ("(" + jsonStr + ")");
}

mapIDs = function(map){  
	var ids = []
	for(key in map){
		ids.push(key);
	}
	return ids;
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
    	if (item.v==0) continue;
    	if (item.t==ITEM_TYPE.CASH)
    		itemDesc += item.v+ITEM_NAME.CASH+split;
    	else if (item.t==ITEM_TYPE.EXP)
    		itemDesc += item.v+ITEM_NAME.EXP+split;
    }
    return itemDesc;
}

itemStr2 = function(items,split){
    var itemDesc = "";
    for (var i=0;i<items.length;i++){
    	var item = items[i];
    	if (item.v==0) continue;
    	var vv = item.v;
    	if (vv<0)
    		vv = 0-vv;
    	if (item.t==ITEM_TYPE.CASH)
    		itemDesc += vv+"现金";
    	else if (item.t==ITEM_TYPE.EXP)
    		itemDesc += vv+"经验";
    		
    	if (i!=items.length-1)
    	 itemDesc += split;
    }
    return itemDesc;
}
//天
calculateTimeout = function(pitem,item){
    var ctime = pitem.createtime;
    var now = Date.parse(new Date());
    var diffMinuts = (now - ctime)/(1000*60*60*24);
    var periodTime = item.period*pitem.qty;
    return ForDight(periodTime - diffMinuts);
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

function rotateImg(ctx,px,py,width,height){
// 取得这个图片的数据，图片与当前页面必须同域，否则会出错
	    var img_data = ctx.getImageData(px, py, width, height);
	    var    x, y, p, i, i2, t;
	    var    h = img_data.height;
	    var    w = img_data.width;
	     var   w_2 = w / 2;
	 
	    // 将 img_data 的数据水平翻转
	    for (y = 0; y < h; y ++) {
	        for (x = 0; x < w_2; x ++) {
	            i = (y<<2) * w + (x<<2);
	            i2 = ((y + 1) << 2) * w - ((x + 1) << 2);
	            for (p = 0; p < 4; p ++) {
	                t = img_data.data[i + p];
	                img_data.data[i + p] = img_data.data[i2 + p];
	                img_data.data[i2 + p] = t;
	            }
	        }
	    }
		return img_data;
}