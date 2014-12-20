// JavaScript Document

Quest = function(){
    this.name = "quest";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.dailyCount = 2;
    this.pageCount = 3;
}

Quest.prototype = new Datamgr();

Quest.prototype.init = function(duration){
    this.duration = duration;
     store.remove(this.name)
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		store.set(this.name,data_questdata);
	} 
    this.buildHTML();
}

Quest.prototype.buildPage = function(page)
{
	var quest = g_player.data.quest;
	
	var tdata = store.get(this.name);
	var content = 	"";
	if (quest.length<=0){
		  content += "<div class='panel' ID='insure_d1'><div class='panel-body'>no quest item</div>"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>quest.length)
			end = quest.length;	
		  content += "<div style='height:330px'>"
		for (var i=start;i< end;i++){
			var q = quest[i];
			var item;
			for (var j=0;j<tdata.length;j++){
				if (tdata[j].id==q.id){
					item = tdata[j];
					break;
				}
			}
			var img = "notdone.png";
			if (item.status==QUEST_STATUS.DONE)	
				img = "done.png";		
		  	content += "<div class='cfpanel' ID='"+this.pagename+item.id+"' onclick='g_quest.showDetail("+item.id+")'>"
		     content += "<h2 class='cf_h'>"+item.name+"</h2>"
			 content += "        <table id='"+this.tagname+"tab'>"
			 content += "           <thead>"
			 content += "             <tr>"
			 content += "               <td class='td-c-name'>描述</td>"
			 content += "               <td class='td-c-value'>"+item.descs+"</td>"
			 content += "               <td class='td-c-value'><img style='width:78px;height:27px' src='static/img/"+img+"'></td>"
//			 content += "               <td class='td-c-name'>条件</td>"
//			 content += "               <td class='td-c-value'>"+item.need+"</td>"
//			 content += "               <td class='td-c-name'>奖励</td>"
//			 content += "               <td class='td-c-value'>"+item.prize+"</td>"
			content += "              </tr>"
			content += "            </thead>"
			content += "          </table>"
      		content += "</div>"
		}
   		content += "</div>"
		
        content += this.buildPaging(page,quest.length);
	}
	
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;	
}

Quest.prototype.showDetail = function(id){  
	var tdata = store.get(this.name);
   var item = tdata[id-1];
   if (item==null) return;

var content =      "        <div><h2>"+item.name+"</h2>"
 content += "            <div>"+item.descs+"</div>"
// content += "        <table id='toplist1_tab'>"
// content += "           <thead>"
//			 content += "             <tr>"
//			 content += "               <td class='td-c-name'>描述</td>"
//			 content += "               <td class='td-c-value'>"+item.descs+"</td>"
//			 content += "               <td class='td-c-name'>条件</td>"
//			 content += "               <td class='td-c-value'>"+item.need+"</td>"
//			 content += "               <td class='td-c-name'>奖励</td>"
//			 content += "               <td class='td-c-value'>"+item.prize+"</td>"
//			content += "              </tr>"
//content += "            </thead>"
//content += "          </table>     "
content += "          <button class='cf_bt bt_cancel' data-dismiss='modal'>取消</button>      "  
content += "          <button class='cf_bt' onclick='g_quest.acceptQuest("+id+")'>接收</button>"
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

Quest.prototype.onAcceptDaily = function(){
	var tdata = store.get(this.name);
	var qids = [];
	var cc = this.dailyCount;
	if (cc>tdata.length)
		cc = tdata.length;
	var qkeys = [];
	var pquest = g_player.data.quest;
	var jsonCurr = Date.parse(new Date());
	for (var i=0;i<cc;i++)
	{
		var index = Math.floor(Math.random()*tdata.length);
		pquest.push({id:tdata[index].id,accept:jsonCurr,status:QUEST_STATUS.ACTIVE});
		tdata.splice(index,1);
	}
	g_player.updateData({quest:pquest});
    return true;
}

Quest.prototype.doneQuest = function(quest){
	var items = g_player.data.quest;
    for (var i=0;i<items.length;i++){
    	var item = items[i];
	if (item.id==quest.id) {
		if (item.status==QUEST_STATUS.DONE){
			alert('该任务已被完成');
			break;
		}
		items[i].status = QUEST_STATUS.DONE;
		g_player.updateData({"quest":items});
		g_player.prize(quest.prize);
	    break;
	}
    }
}

Quest.prototype.onDoneQuest = function(){
    alert('onDoneQuest');
}


Quest.prototype.onBuyInsure = function(item){
	var items = g_player.data.quest;
    for (var i=0;i<items.length;i++){
		if (items[i].status==QUEST_STATUS.ACTIVE) {
	    	var quest = this.findItem(items[i].id);
		    if (quest.type==QUEST_TYPE.BUY_INSURE){
			this.doneQuest(quest);
			break;
		    }
		}
    }
}

Quest.prototype.onBuyStock = function(item){
	var items = g_player.data.quest;
    for (var i=0;i<items.length;i++){
		if (items[i].status==QUEST_STATUS.ACTIVE) {
	    	var quest = this.findItem(items[i].id);
		    if (quest.type==QUEST_TYPE.BUY_STOCK){
			this.doneQuest(quest);
			break;
		    }
		}
    }
}

Quest.prototype.constructor = Quest;

var g_quest = new Quest();
g_quest.init(30);
