// JavaScript Document
Signin = function(){
	this.name = "signin"
	this.tagname = "my"+this.name
    this.pagename = this.tagname+"page";
	this.feeling = -1;
}

Signin.prototype = new Datamgr();

Signin.prototype.init = function(){
	store.remove(this.name);
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_signindata);
	} 	
	this.buildHTML();
	this.buildPage(0);
}

Signin.prototype.buildPage = function(page)
{
	var content = "<div class='cfpage_content'>"
	content +=	" 今天签到将获得:"
    content +=      "      <span id='signin_gettoday' class='cf_font2'> </span><br>"
    content +=           "  明天签到将获得:"
    content +=            "  <span id='signin_gettomorrow' class='cf_font2'> </span>"
    content +=            "     <div class='cf_font3'>注:连续签到工资将会有提升</div>"
    content +=            " <div class='cf_signin_feeling'> 请选择你今天的心情:"
    content +=            "     <div id ='signin_feeling' class='cf_signin_feeling2'>心情选择</div>  "
     content +=       " </div>"
    content +=            " </div>"

	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;

}

Signin.prototype.start = function(startDay){
	//return;
	 
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
    get.innerHTML = "<br>"+itemStr(this.data,",");
 
    get = document.getElementById("signin_gettomorrow");
    get.innerHTML = "<br>"+itemStr(this.data_to,",");

    var tagfeel = document.getElementById("signin_feeling");
    
	for (var i=0; i<tagfeel.childNodes.length; i++) {
	 	var childNode = tagfeel.childNodes[i];
	 	tagfeel.removeChild(childNode);
	}    
	
	var context = "";
 	 context += "        <table class='cfsignin_content'>"
	 context += "             <tr>"
	 for (var i=0;i<4;i++){
	 	var img = feeling_imgs[i];
	 	var id = i+1;
        context += "<td class='cfsignin_feelingimgs' id='signin_feeling"+id+"'";
         context += " onclick='g_signin.clickFeeling("+id+")'>";
        context += "<img src='"+img.src+"'><br><br>";
	 	img = feeling_imgs[i+4];
          context += "<img src='"+img.src+"'>";
 		context += "</td>"
 	  }
 	context += "</tr>"
 	context += "</table>     "
    
    tagfeel.innerHTML = context;
    
    $('#'+this.tagname).modal({position:Page_Top,show:true});       
}

Signin.prototype.clickFeeling = function(feelingId){
	for (var i=0;i<4;i++){
		var ftag= document.getElementById("signin_feeling"+(i+1));
		ftag.style.border = "none";
	}
	var ftag = document.getElementById("signin_feeling"+feelingId);
	ftag.style.border = "3px solid green";
    
    g_player.prize(this.data);
    
    g_playerlog.updateSignin(feelingId);
    
    $('#'+this.tagname).modal('hide'); 
    
    g_insure.onEnter();     
}


var g_signin = new Signin();
g_signin.init();
