
Uplevel = function(){
	this.name ="uplevel";
	this.count = 0;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
}

Uplevel.prototype = new Datamgr();

Uplevel.prototype.init = function()
{

	this.buildHTML();
	
	setInterval(function(){
	   g_uplevel.update();
	  },MsgDuration
	);	
}

Uplevel.prototype.buildHTML = function()
{
        var pagedetail = new PageUtil(this.tagname);
        var psubclass = "";
        content =     "<div class=\"tab-pane in active\" id='quest2'>";
		content += "<img class='cfuplevel_img' id='msglevel_g1'>"
        content += "<div class='cfpagedetail uplevel' id='"+this.pagename+"'>"
        content += "</div></div>"
        pagedetail.addContent(content);
        document.write(pagedetail.toString());  
        var tag = document.getElementById('msglevel_g1');
        tag = document.getElementById('cssvalues');
        
       // tag.innerHTML = outputCssStyles("cf.css");
       // alert(getStyleValue("cf.css","."+tag.className,"margin-left"));
       // tag.style.setProperty("margin-left","70px");
       //  alert(tag.style.getPropertyValue("margin-left"));
       // tag.style.setProperty("margin-top","-330px");
}

//title自定义：
Uplevel.prototype.open = function()
{
	this.isLevel = true;
	
	var lv = g_title.getLevel();
	var content =      "        <div class='cfuplevel_block'>"
	content += "<div><img src='static/img/title_up.png'></div>"
	content += "<img src='static/img/pop_line.png'>"
	content += " <div class='cfplayer_head_bg uplevel'>"
    content += "<img src='"+head_imgs[g_player.data.sex].src+"'/>"
    content += " </div>"
	content += "            <div class='cfmsg_text uplevel'>恭喜升级为"+lv+"级<br>获得新称号:"
	content +=  g_title.getData(lv).name+"</div>"
	content += "          <button class='cf_bt' onclick='g_uplevel.closeLevel()'>确认</button>"
	content += "             </div>"
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
		
	$('#'+this.tagname).modal({position:0,show: true});  
}

Uplevel.prototype.closeLevel = function()
{
	$('#'+this.tagname).modal('hide'); 
	this.isLevel = false;
}


Uplevel.prototype.update = function()
{
	if (!this.isLevel) return;
	
	{
		var tag = document.getElementById('msglevel_g1');
		var index = tag.src.indexOf("up_g1.png");
		if (index>0)
			tag.src="static/img/up_g2.png";
		else
			tag.src="static/img/up_g1.png";
	}
}

var g_uplevel = new Uplevel()

