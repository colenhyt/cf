// JavaScript Document

Quest = function(){
    this.name = "quest";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.data = {};
}

Quest.prototype = new Datamgr();

Quest.prototype.init = function(duration){
    g_game.register(this);
    this.duration = duration;
    //this.loadData();
    this.buildHTML_body();
}

Quest.prototype.loadDataCallback = function(tx,results){
	for (var i=0;i<results.rows.length;i++){
		var item = results.rows.item(i);
        g_quest.data[item.id]= item;
    }
    g_quest.buildHTML_data();
}

Quest.prototype.buildHTML_body = function()
{
	var page = new PageUtil(this.tagname);
	page.buildSingleTab("任务列表");
	var content = 	"<div class='tab-pane in active' id='quest1'>";
	content += "<div class='cfpage' id='"+this.pagename+"'>"
    content += "</div></div>"
	page.addContent(content);
	document.write(page.toString());	
	
	var pagedetail = new PageUtil(this.tagdetailname);
	content = 	"<div class=\"tab-pane in active\" id='quest2'>";
	content += "<div class=\"cfpage\" id='"+this.pagedetailname+"'>"
    content += "</div></div>"
	pagedetail.addContent(content);
	document.write(pagedetail.toString());	
	
}

Quest.prototype.buildHTML_data = function()
{
	var content = 	"";
	if (this.data.length<=0){
		  content += "<div class='panel' ID='insure_d1'><div class='panel-body'>no quest item</div>"
      content += "</div>"
	}else {
		for (key in this.data){
			var item = this.data[key];
		  content += "<div class='panel' ID='"+this.pagename+item.id+"' onclick='g_quest.showDetail("+item.id+")'>"
		     content += "<div class='panel-body'><h2>"+item.name+"</h2>"
			 content += "        <table id='"+this.tagname+"tab'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td class='td-c-name'>描述</td>"
			 content += "               <td class='td-c-value'>"+item.desc+"</td>"
			 content += "               <td class='td-c-name'>条件</td>"
			 content += "               <td class='td-c-value'>"+item.need+"</td>"
			 content += "               <td class='td-c-name'>奖励</td>"
			 content += "               <td class='td-c-value'>"+item.prize+"</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
      content += "</div></div>"
		}
	}
     
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;
}

Quest.prototype.showDetail = function(id){  
var item = this.data[id];

var content =      "        <div><h2>"+item.name+"</h2>"
 content += "            <div>"+item.desc+"</div>"
 content += "        <table id='toplist1_tab'>"
 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td class='td-c-name'>描述</td>"
			 content += "               <td class='td-c-value'>"+item.desc+"</td>"
			 content += "               <td class='td-c-name'>条件</td>"
			 content += "               <td class='td-c-value'>"+item.need+"</td>"
			 content += "               <td class='td-c-name'>奖励</td>"
			 content += "               <td class='td-c-value'>"+item.prize+"</td>"
			content += "              </tr>"
content += "            </thead>"
content += "          </table>     "
content += "          <button class='btn btn-primary' data-toggle='modal' onclick='g_quest.acceptQuest("+id+")'>接收</button>"
content += "          <button class='btn btn-primary' data-toggle='modal' data-dismiss='modal'>取消</button>      "  
content += "             </div>"
content += "           </div>  "

	var tag = document.getElementById(this.pagedetailname);
	tag.innerHTML = content;
	$('#'+this.tagdetailname).modal('show');
}

Quest.prototype.acceptQuest = function(id){
//alert(id);
    for (var i=0;i<g_player.quest.length;i++){
	if (g_player.quest[i].id==id) {
	    g_msg.show("quest has been received",MSG.INFO);
	    return;
	}
    }
    g_player.quest.push({id:id});
    var strQuest = JSON.stringify(g_player.quest);
    g_player.updateData({quest:strQuest});
    
}


Quest.prototype.onAcceptQuest = function(){
    alert('onAcceptQuest');
}

Quest.prototype.doneQuest = function(questId){
    for (var i=0;i<g_player.quest.length;i++){
	if (g_player.quest[i].id==questId) {
	    g_msg.show("quest has been received",MSG.INFO);
	    return;
	}
    }
    g_player.quest.push({id:questId});
    var strQuest = JSON.stringify(g_player.quest);
    g_player.updateData({quest:strQuest});
    
}

Quest.prototype.onDoneQuest = function(){
    alert('onDoneQuest');
}

Quest.prototype.constructor = Quest;

var g_quest = new Quest();
g_quest.init(30);
