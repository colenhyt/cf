// JavaScript Document

Quest = function(){
    this.name = "quest";
}

Quest.prototype = new Datamgr();

Quest.prototype.init = function(duration){
    g_game.register(this);
    this.duration = duration;
    //this.loadData();
}

Quest.prototype.acceptQuest = function(questId){
    alert('acceptQuest'+questId);
}

Quest.prototype.onAcceptQuest = function(){
    alert('onAcceptQuest');
}

Quest.prototype.loadDataCallback = function(tx,results){
	if (results.rows.length==0){
	   g_game.sys["quest"].loadServerData();	   
    }
}

Quest.prototype.constructor = Quest;

var g_quest = new Quest();
g_quest.init(30);
