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
	var quest = store.get(g_quest.questkey());
	
	if (quest==null) quest = [];
	
	var tdata = store.get(this.name);
	var content = 	"";
	if (quest.length<=0){
		  content += "<div class='cfpanel' ID='insure_d1'>您当前没有任务"
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

Quest.prototype.questkey = function(){
if (g_player.data!=null)
 return g_player.data.playername+"_"+g_quest.name;
}

Quest.prototype.onAcceptDaily = function(){
    var datakey = g_quest.questkey();
	var pquest = store.get(datakey);
	if (pquest==null){
	  pquest = [];
	}
	
	if (pquest.length>=2)
	{
	 return;
	}
	var tdata = store.get(this.name);
	var qids = [];
	var cc = this.dailyCount-pquest.length;
	if (cc>tdata.length)
		cc = tdata.length;
	var existId = 0;
	var ttdata = [];
	if (pquest.length>0)
	{
	 exitsId = pquest[0].id;
	 for (var ii=0;ii<tdata.length;ii++){
	  if (tdata[ii].id==exitsId)
	  {
	   continue;
	  }
	   ttdata.push(tdata[ii]);
	 }
	}else {
	 ttdata = tdata;
	}
	var qkeys = [];
	var jsonCurr = Date.parse(new Date());
	for (var i=0;i<cc;i++)
	{
		var index = Math.floor(Math.random()*ttdata.length);
		pquest.push({id:ttdata[index].id,accept:jsonCurr,status:QUEST_STATUS.ACTIVE});
		ttdata.splice(index,1);
	}
	store.set(datakey,pquest);
    return true;
}

Quest.prototype.doneQuest = function(quest){
	var datakey = g_quest.questkey();
    var items = store.get(datakey);
    for (var i=0;i<items.length;i++){
    	var item = items[i];
	if (item.id==quest.id) {
		if (item.status==QUEST_STATUS.DONE){
			//g_msg.tip('该任务已被完成');
			continue;
		}
		items[i].status = QUEST_STATUS.DONE;
		g_msg.tip("任务<span style='color:red'>"+quest.name+"</span>完成，请领取奖励");
		//g_player.prize(quest.prize);		//奖励手工领取
	    break;
	}
    }
    store.set(datakey,items);
    
    //当天任务是否已全部完成:
    var doneCount = 0;
    for (var i=0;i<items.length;i++){
    	var item = items[i];
    	if (item.status==QUEST_STATUS.DONE){
    	 doneCount++;
    	}
    }    
    if (doneCount>=items.length){
     //alert('任务已全部完成:'+doneCount);
		try  {
	   		var dataParam = "playerid="+g_player.data.playerid+"&type=1";
			$.ajax({type:"post",url:"/cf/player_update.jsp",data:dataParam,success:function(dataobj){
			}});
		}   catch  (e)   {
		    document.write(e.name);
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
	var quests = store.get(g_quest.questkey());
	var index;
	for (var i=0;i<quests.length;i++)
	{
		if (quests[i].id==id){
			index = i;
			break;
		}
	}   
	playAudioHandler('money');	
	g_msg.tip("成功领取任务奖励:"+itemStr2(pz,","));
	quests.splice(index,1);
	store.set(g_quest.questkey(),quests);
	
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
	
	var items = store.get(g_quest.questkey());
	if (items==null)
		return;
		
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
//未完成任务；
//快到期保险产品
Quest.prototype.getQuests = function(status) {
    var items = store.get(g_quest.questkey());
	var itemids = [];
	if (!items)
		return itemids;
		
	for (var i=0;i<items.length;i++){
		if (items[i].status==status){
			itemids.push(items[i].itemid);
		}
	}
	return itemids;
}

Quest.prototype.update = function(){
	if (!g_game.enter) {
		return;
	}
	
	this.count ++;
	if (this.count%3!=0)return;
	
	var tag = document.getElementById("tag"+this.name);
	var questids = g_quest.getQuests(QUEST_STATUS.ACTIVE);
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
