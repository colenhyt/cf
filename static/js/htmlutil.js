

PageUtil = function(id,zIndex,contentClass){
	this.id = id;
	var head = ""; 
	head += "<div class='modal fade' id='"+id+"'";
	if (zIndex>0)
		head += " style='z-index:"+zIndex+"'>";
	else
		head += ">"
  	head += "<div class='modal-dialog'>";
  	if (contentClass&&contentClass.length>0)
    	head += "<div class='"+contentClass+"'>";
    else
    	head += "<div class='modal-content'>";
    
    var footer = "";
    footer +="</div>";
	footer += "</div>";
    footer +="</div>";
    
     this.header = "<div class='modal-header'>";
     
    this.content = "";
    this.head = head;
    this.footer = footer;
   
}

PageUtil.prototype.buildHeader1 = function(title,titelCallback)
{
	var content = "<div class='cf-m-header'>"
	content += " <table><tr>"
	content += "  <td width='100'>"
    content += "<input type='button' class='form-control' "
    if (titelCallback.length>0)
    	content += "onclick='"+titelCallback+"()'"
    content += " value='"+title+"'/>"
	content += "</td>"
	content += "   <td align='right'>"
    content += "<button type='button' class='close' data-dismiss='modal'><img src='static/img/close.png'></button>"	
	content += "</td>"
	content += " </tr>"
	content += "</table></div>"	
	return content;
}

PageUtil.prototype.addHeader = function(strHeader)
{
	this.header += strHeader;
}

PageUtil.prototype.addContent = function(strContent)
{
	this.content += strContent;
}

PageUtil.prototype.toString = function()
{
	return this.head+this.header+"</div>"+this.content+this.footer;
}

PageUtil.prototype.buildMsg = function(contentId)
{
	var content = "<div class='messager messager-example messager-info'>";
	content +="<div class='messager-content' id='"+contentId+"'></div>";
	content +="<button class='close-messager'>&times;</button></div>"
	
	this.addContent(content);
}

PageUtil.prototype.buildSingleTab = function(titleImg)
{
	var bg = "close.png";
	var img = game_page_imgs[bg];
	var width = img.width * game_screen.swidth;
     var header = "<button type='button' class='close' data-dismiss='modal'>"
     header += "<img src='static/img/"+bg+"' width='"+width+"'>"
     header += "</button>";

	bg = "title_bg.png";
	img = game_page_imgs[bg];
	width = img.width * game_screen.swidth;
	header += "<ul id='"+this.id+"Tab' class='nav nav-tabs'>"
     header += "<img src='static/img/"+bg+"' width='"+width+"'>"
 	img = game_page_imgs[titleImg];
 	width = img.width * game_screen.swidth;
     header += "<img src='static/img/"+titleImg+"' width='"+width+"' class='cf-m-title'>"
    header += "</ul>"
	this.addHeader(header);
}