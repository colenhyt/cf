// JavaScript Document
Signin = function(){
}

Signin.prototype = new Datamgr();

Signin.prototype.init = function(){
	this.feeling = -1;
}

Signin.prototype.start = function(startDay){ 
    var signin = this;    
    var vstartDay = startDay + 1;	//
    
    var query = 'SELECT * FROM t_signin where day>='+vstartDay+' and day<='+(vstartDay+1);
    g_db.query(query, function (tx, res) {
       if (res.rows.length) {
            //alert('found ' + res.rows.length + ' record(s)');
        } else {
            alert('table foo is empty');
        }
        	signin.data = [];
        	signin.data_to = [];
        	for (var i=0;i<res.rows.length;i++)
        	{
        		var item = res.rows.item(i);
        		var item2 ={"t":item.type,"v":item.value};
        		if (item.day==vstartDay){
        			signin.data.push(item2);
        		}else if (item.day==vstartDay+1){
        			signin.data_to.push(item2);
        		}
        	}
            signin.show();
    });     
}

Signin.prototype.show = function(){
    var signin = this;    
    var get = document.getElementById("signin_gettoday");
    get.innerHTML = itemStr(this.data,"<br>");
 
    get = document.getElementById("signin_gettomorrow");
    get.innerHTML = itemStr(this.data_to,"<br>");

    var tagfeel = document.getElementById("signin_feeling");
    
	for (var i=0; i<tagfeel.childNodes.length; i++) {
	 	var childNode = tagfeel.childNodes[i];
	 	tagfeel.removeChild(childNode);
	}    
	
    for (var i=0;i<feeling_imgs.length;i++)
    {
        var imgData = feeling_imgs[i];
        var div = document.createElement("div");
        div.id = "divfeeling_"+this.id;
        var img = new Image();
        img.src = imgData.src;
        img.id = "imgfeeling_"+imgData.id;
        img.sid = imgData.id;
        img.onclick = function(){          
            signin.feeling = this.sid;
            this.style.border = "1px red";
        }
        //div.appendChild(img);    
        tagfeel.appendChild(img);       
    }
    
    $('#mysignin').modal('show');       
}

Signin.prototype.doSignin = function(){
	if (this.feeling<0)
	{
		alert('pls choose a feeling');
		return;
	}
	
    g_player.prize(this.data);
    
    g_player.logPlayer([{"logtime":Date.parse(new Date()),"feeling":this.feeling}]);
    
    g_player.updateData({'lastsignin':Date.parse(new Date())}); 
    
    $('#mysignin').modal('hide');     
}

var g_signin = new Signin();
g_signin.init();
