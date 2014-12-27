function aaa(){
	log("aaa");
}

function aaa2(){
	//alert("aaa2");
}

Main = function(){
}

Main.prototype.init = function(){
	var dataobj = $.ajax({url:"/cf/login_connect.do",async:false});
	var retcode = -1;
	try    {
		if (dataobj!=null&&dataobj.responseText.length>0) {
			var obj = eval ("(" + dataobj.responseText + ")");
			if (obj!=null&&obj.code!=null){
				retcode = obj.code;
			}
		}
	}   catch  (e)   {
	    document.write(e.name  +   " :  "   +  dataobj.responseText);
	}
	if (retcode==-1){
		alert('联网失败，请检查你的网络');
		return;
	}
	
    var canvas = document.createElement("canvas");
    canvas.width = Scene_Width;
    canvas.height = Scene_Height;
    document.body.appendChild(canvas);
    
    g_msg.tip("aaaaa");
    g_msg.tip("fdafda");
	g_game = new Game();
	g_game.init(canvas);
}
 
var g_main = new Main();
g_main.init();
