// JavaScript Document
Signin = function(){
	this.name = "signin"
	this.backdrop = "static";
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

Signin.prototype.findPrize = function(days){
	 
    var prize = {currPz:[],toPz:[]};
    
    var data = store.get(this.name);
    if (data!=null)
    {
    	for (var i=0;i<data.length;i++)
    	{
    		var item = data[i];
    		var item2 ={"t":item.type,"v":item.value};
    		if (item.day==days){
    			prize.currPz.push(item2);
    		}else if (item.day==days+1){
    			prize.toPz.push(item2);
    		}
    	} 
    }
    
    return prize;
 }

Signin.prototype.show = function(){
	var data = store.get(g_playerlog.name);
	var signindays = findSigninLog(data);
	if (signindays<=0) return;
	
	var prize = this.findPrize(signindays);
	
    var signin = this;    
    var get = document.getElementById("signin_gettoday");
    var todayget = "<br>"
    var cash = itemValue(prize.currPz,0);
    var exp = itemValue(prize.currPz,1);
    todayget += "经验:<span class='cfsignin_mmdesc'>,金钱:</span><span class='cfsignin_prize exp' id='cfsignin_prize_exp'>"+exp+"</span>"
    todayget += "<span class='cfsignin_prize' id='cfsignin_prize'>"+cash+"</span>"
    get.innerHTML = todayget;
 
    get = document.getElementById("signin_gettomorrow");
    get.innerHTML = "<br>"+itemStr(prize.toPz,",");

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
        context += "<img class='cffeelingimg' src='"+img.src+"'><br>";
	 	img = feeling_imgs[i+4];
          context += "<img class='cffeelingword'  src='"+img.src+"'>";
 		context += "</td>"
 	  }
 	context += "</tr>"
 	context += "</table>     "
    
    this.currPrize = prize.currPz;
    
    tagfeel.innerHTML = context;
    
    $('#'+this.tagname).modal({backdrop:'static',position:getSizes().PageTop,show:true});       
}

Signin.prototype.clickFeeling = function(feelingId){
	for (var i=0;i<4;i++){
		var ftag= document.getElementById("signin_feeling"+(i+1));
		ftag.style.border = "none";
	}
	var ftag = document.getElementById("signin_feeling"+feelingId);
	ftag.style.border = "3px solid green";
    
    

  var div=$("#cfsignin_prize_exp");
  div.animate({fontSize:'2.5em'},300);
  div.animate({left:'-='+getSizes().SigninMoney[0],top:'-='+getSizes().SigninMoney[2]},1300);
  div.animate({fontSize:'1em'},300,function(){
    var div=$("#cfsignin_prize_exp");
    div.remove();
   });    
    
  var div=$("#cfsignin_prize");
  div.animate({fontSize:'2.5em'},300);
  div.animate({left:'-='+getSizes().SigninMoney[1],top:'-='+getSizes().SigninMoney[2]},1300);
  div.animate({fontSize:'1em'},300,function(){
   var div=$("#cfsignin_prize");
    div.remove();
	   g_player.prize(this.currPrize);
	   this.currPrize = null;
	   $('#'+this.tagname).modal('hide'); 
	    g_playerlog.updateSignin(feelingId);
	   g_game.onEnter(); 
   });
}


var g_signin = new Signin();
g_signin.init();
