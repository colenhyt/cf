// JavaScript Document
Signin = function(){
	this.name = "signin"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
}

Signin.prototype = new Datamgr();

Signin.prototype.init = function(){
	this.feeling = -1;
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_signindata);
	} 	
	this.buildHTML("title_signin.png");
	this.buildPage(0);
}

Signin.prototype.buildPage = function(page)
{
//	var page = new PageUtil(this.tagname,0,'modal-content2');
//	var content = page.buildHeader1('签到','g_signin.doSignin');
//	page.addHeader(content);

	var content = "<div class='cf-signin-prize'>"
	content +=	"<div> 今天获得:"
    content +=      "      <div id='signin_gettoday'></div>"
    content +=       " </div>"
    content +=           "  <div> 明天将获得:"
    content +=            "     <div id='signin_gettomorrow'></div>"
    content +=            " </div></div>"
    content +=            " <div class='cf-signin-feeling'> 请选择你今天的心情:"
    content +=            "     <div id ='signin_feeling'>signin_feeling</div>  "
    content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;

}

Signin.prototype.start = function(startDay){ 
    var signin = this;    
    var vstartDay = startDay + 1;	//
    
    var data = store.get(this.name);
    if (data!=null)
    {
    	signin.data = [];
    	signin.data_to = [];
    	for (var i=0;i<data.length;i++)
    	{
    		var item = data[i];
    		var item2 ={"t":item.type,"v":item.value};
    		if (item.day==vstartDay){
    			signin.data.push(item2);
    		}else if (item.day==vstartDay+1){
    			signin.data_to.push(item2);
    		}
    	} 
    	if (signin.data.length>0)   
        	signin.show();
    }
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
        img.width = 50;
        img.onclick = function(){          
            signin.feeling = this.sid;
            this.style.border = "1px red";
        }
        //div.appendChild(img);    
        tagfeel.appendChild(img);       
    }
    var option = {
    	position : 50,
    	show     : true
	}
    $('#'+this.tagname).modal(option);       
}

Signin.prototype.doSignin = function(){
	if (this.feeling<0)
	{
		g_msg.show('pls choose a feeling',MSG.INFO);
		return;
	}
	
    g_player.prize(this.data);
    
    g_playerlog.updateSignin(this.feeling);
    
    $('#'+this.tagname).modal('hide');     
}

var g_signin = new Signin();
g_signin.init();
