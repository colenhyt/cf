Event = function(){
    this.name = "event";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.tick = 0;
    if (data_eventdata_feq==null||data_eventdata_feq==0)
    	data_eventdata_feq = 30;
    this.duration = data_eventdata_feq * 60;
}

Event.prototype = new Datamgr();

Event.prototype.init = function(){
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_eventdata);
	}  
    this.buildHTML();	   
}

Event.prototype.buildHTML = function(){
        var pagedetail = new PageUtil(this.tagdetailname);
        var psubclass = "";
        if (this.name=="insure"||this.name=="event")
        	psubclass = "small";
        pclass = "cfpagedetail "+psubclass;
        content =     "<div class=\"tab-pane in active\" id='quest2'>";
        content += "<div class='"+pclass+"' id='"+this.pagedetailname+"'>"
        content += "</div></div>"
        pagedetail.addContent(content);
        document.write(pagedetail.toString());  
}

Event.prototype.showDetail = function(id){
	var tdata = store.get(this.name);
   var item = tdata[id];
   if (item==null) return;
        
    this.isTrigger = true;
    
var content =      "        <div><h2 style='margin-top:-5px;margin-bottom:-5px;text-align:center'>"+item.name+"</h2>"
content += "<img src='static/img/pop_line.png'>"
 content += "            <div>"+item.desc+"</div>"
 content +=	"</div>"
content += "           <div style='margin-top:10px'>  "
content += "          <button class='cf_bt' onclick='g_event.acceptEvent("+id+")'>确认</button>"
content += "             </div>"
	
	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;

	$('#'+this.tagdetailname).modal({position:120,show: true});  

}

Event.prototype.acceptEvent = function(id){
	$('#'+this.tagdetailname).modal("hide");  
	this.isTrigger = null;
	//g_player.prize(ee.prize);
}       
 
//随机事件触发
Event.prototype.triggerEvent = function(){
	var tdata = store.get(this.name);
	var index = Math.floor(Math.random()*tdata.length);
	var ee = tdata[index]; 
	log("event trigger:"+ee.prize.id);
	this.showDetail(index);
}

//
Event.prototype.update = function(){
	this.tick++;
	if (this.tick%this.duration==0){
		if (this.isTrigger==null)
			this.triggerEvent();
	}
}
var g_event = new Event();
g_event.init();