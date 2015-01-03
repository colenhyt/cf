

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
	footer += "</div>";
	footer += "</div>";
    footer +="</div>";
    
     this.header = "<div class='modal-header'>";
     
    this.content = "";
    this.head = head;
    this.footer = footer;
   
}

PageUtil.prototype.buildSingleTab = function(titleImg,pName)
{
	var header = "<ul id='"+this.id+"Tab' class='nav nav-tabs'>"
     header += "<div class='cf_title_bg' onclick='g_"+pName+".onClickHead()'><img src='static/img/"+titleImg+"' class='cf_title'></div>"
     if (pName!="signin")
    	header += "<button type='button' class='close' data-dismiss='modal'><img src='static/img/close.png' class='cf_title_close'></button>"	
    header += "</ul>"	
	this.addHeader(header);
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
	var content = this.head+this.header+"</div>"+this.content+this.footer;
	return content;
}

PageUtil.prototype.buildMsg = function(contentId)
{
	var content = "<div class='messager messager-example messager-info'>";
	content +="<div class='messager-content' id='"+contentId+"'></div>";
	content +="<button class='close-messager'>&times;</button></div>"
	
	this.addContent(content);
}
