// JavaScript Document

Task = function(){
    this.name = "task";
}

Task.prototype = new Datamgr();

Task.prototype.init = function(duration){
    g_game.register(this);
    this.duration = duration;
    //this.loadData();
}

Task.prototype.acceptTask = function(taskId){
    alert('acceptTask'+taskId);
}

Task.prototype.onAcceptTask = function(){
    alert('onAcceptTask');
}

Task.prototype.loadDataCallback = function(tx,results){
	if (results.rows.length==0){
	   g_game.sys["task"].loadServerData();	   
    }
}

Task.prototype.constructor = Task;

var g_task = new Task();
g_task.init(30);
