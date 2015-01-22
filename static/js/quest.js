// JavaScript Document

Quest = function(){
    this.name = "quest";
    this.tagname = "my"+this.name;
    this.count = 0;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.currPage = 0;
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
		  content += "<div class='cfpanel' ID='insure_d1'>你当前没有任务"
      content += "</div>"
	}else {
		var start = page* this.pageCount;
		var end = (page+1)* this.pageCount;
		if (end>quest.length)
			end = quest.length;	
		  content += "<div class='cfpanel_body'>"
		for (var i=start;i< end;i++){
			var pitem = quest[i];
			var item;
			for (var j=0;j<tdata.length;j++){
				if (tdata[j].id==pitem.id){
					item = tdata[j];
					break;
				}
			}
			var img = "notdone.png";
			var isDone = item!=null&&pitem.status==QUEST_STATUS.DONE;
			var click = "g_quest.gotoQuest("+item.id+")"
			if (isDone){	
				click = "g_quest.getQuetPrize("+item.id+")"
			}
		  	content += "<div class='cfpanel' ID='"+this.name+"_d"+item.id+"' onclick='"+click+"'>"
		     content += "<div><span class='cfpanel_title'>"+item.name+"</span>"
			if (isDone){	
				content += "<input type='button' class='cf_bt questprize' value='领取奖励'/>"
			}
			content += "</div>"
			 content += "        <table id='"+this.tagname+"tab'>"
			 content += "             <tr>"
	//		 content += "               <td class='td-c-name'>描述</td>"
			 content += "               <td class='cfquest_td'>"+item.descs.substring(0,15)+"</td>"
			if (isDone){	
				content += "<td style='text-align:right'><img class='cfquest_icon' src='static/img/done.png'></td>"
			}else
				content += "<td style='text-align:right'><img class='cfquest_icon' src='static/img/notdone.png'></td>"
			 
//			 content += "               <td class='td-c-name'>奖励</td>"
//			 content += "               <td class='td-c-value'>"+item.prize+"</td>"
			content += "              </tr>"
			content += "          </table>"
      		content += "</div>"
		}
   		content += "</div>"
		
		this.currPage = page;
		
        content += this.buildPaging(page,quest.length);
	}
	
	var tag = document.getElementById(this.pagename);
	tag.innerHTML = content;	
}

Quest.prototype.gotoQuest = function(id){  
 	this.onPanelClick(id);
 	
	var tdata = store.get(this.name);
   var item = this.findItem(id);
   if (item==null) return;
	
	if (item.type==-1||item.type==QUEST_TYPE.BUY_INSURE||item.type==QUEST_TYPE.BUY_FINAN)
	{
		g_insure.show();
	}
	else if (item.type==QUEST_TYPE.BUY_STOCK||item.type==QUEST_TYPE.SELL_STOCK)
	{
		g_stock.show();
	}
	else if (item.type==QUEST_TYPE.SAVING)
	{
		g_bank.show();
	}

	this.hide();
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
	var pquest =  g_player.data.quest?g_player.data.quest:[];
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
	var items = g_player.data.quest
    for (var i=0;i<items.length;i++){
    	var item = items[i];
	if (item.id==quest.id) {
		if (item.status==QUEST_STATUS.DONE){
			//g_msg.tip('该任务已被完成');
			continue;
		}
		items[i].status = QUEST_STATUS.DONE;
		g_player.updateData({"quest":items});
		g_msg.tip("任务<span style='color:red'>"+quest.name+"</span>完成，请领取奖励");
		//g_player.prize(quest.prize);		//奖励手工领取
	    break;
	}
    }
}

Quest.prototype.getQuetPrize = function(id){
    var tdata = store.get(this.name);
   var item = this.findItem(id);
   if (item==null) return;
   
   
   var pz = cfeval(item.prize);
   g_player.prize(pz);
   //移除:
	var quests = g_player.data.quest;
	var index;
	for (var i=0;i<quests.length;i++)
	{
		if (quests[i].id==id){
			index = i;
			break;
		}
	}   
	g_msg.tip("成功领取任务奖励:"+itemStr(pz,","));
	quests.splice(index,1);
	g_player.updateData({quest:quests});
	
	this.buildPage(0);
}

Quest.prototype.onBuyItem = function(tname,item,qty){
	var type;
	if (tname=="insure"){
		if (parseInt(item.type)==0)
		 type = QUEST_TYPE.BUY_INSURE;
		else (parseInt(item.type)==1)
		 type = QUEST_TYPE.BUY_FINAN;
	}else if (tname=="stock"){
		if (qty>0)
			type = QUEST_TYPE.BUY_STOCK;
		else if (qty<0)
			type = QUEST_TYPE.SELL_STOCK;
	}else if (tname=="saving"){
		type = QUEST_TYPE.SAVING;
	}
	
	var items = g_player.data.quest
    for (var i=0;i<items.length;i++){
		if (items[i].status==QUEST_STATUS.ACTIVE) {
	    	var quest = this.findItem(items[i].id);
		    if (quest.type==type||quest.type==-1){
				this.doneQuest(quest);
				break;
		    }
		}
    }
}

Quest.prototype.update = function(){
	this.count ++;
	if (this.count%3!=0)return;
	
	var tag = document.getElementById("tag"+this.name);
	var questids = g_player.getQuests(QUEST_STATUS.ACTIVE);
	if (questids.length>0&&tag){
		var tip = tag.innerHTML;
		var index = tip.indexOf("icon_quest_on.png");
		if (index>0){
			tag.innerHTML = "<img src='static/img/icon_quest.png' class='cfpage_text quest2' onclick='g_quest.onclick()"
		}else
			tag.innerHTML = "<img src='static/img/icon_quest_on.png' class='cfpage_text quest2' onclick='g_quest.onclick()'>"
	}		
}


Quest.prototype.constructor = Quest;

var g_quest = new Quest();
g_quest.init(30);
