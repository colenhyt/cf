// JavaScript Document

Quest = function(){
    this.name = "quest";
    this.tagname = "my"+this.name;
}

Quest.prototype = new Datamgr();

Quest.prototype.init = function(duration){
    g_game.register(this);
    this.duration = duration;
    //this.loadData();
    this.buildHTML();
}

Quest.prototype.buildHTML = function()
{
	var page = new PageUtil(this.tagname);
	var content = 	 "<div class=\"cfpage\">"
	  content += "this is myquest content!!!"
	  content += "<button class=\"btn btn-primary\" data-toggle=\"modal\" onclick=\"g_quest.acceptQuest(33)\">接受</button>"
	  content += "</div>"
	page.addContent(content);
	
	document.write(page.toString());
}

Quest.prototype.acceptQuest = function(questId){
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

Quest.prototype.onAcceptQuest = function(){
    alert('onAcceptQuest');
}

Quest.prototype.onDoneQuest = function(){
    alert('onDoneQuest');
}

Quest.prototype.loadDataCallback = function(tx,results){
	if (results.rows.length==0){
	   g_game.sys["quest"].loadServerData();	   
    }
}

Quest.prototype.constructor = Quest;

var g_quest = new Quest();
g_quest.init(30);
