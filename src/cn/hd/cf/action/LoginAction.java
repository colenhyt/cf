package cn.hd.cf.action;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;
import cn.hd.base.Base;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.SavingManager;
import cn.hd.mgr.StockManager;
import cn.hd.util.HttpXmlClient;

import com.alibaba.fastjson.JSON;

public class LoginAction extends SavingAction {
	private Player player;
	
	public String connect(){
		Message msg = new Message();
		msg.setCode(0);
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString(),"utf-8");
		return null;
	}
	
	public int appServerCheck(String openid){
    	Map<String, String> params = new HashMap<String, String>();  
    	params.put(DataManager.getInstance().openidparam, openid); 
    	String json = HttpXmlClient.post(DataManager.getInstance().openidurl, params);  
    	log.warn(json);
		return 0;
	}
	
	public synchronized String getPlayerJsonData(Player playerBlob)
	{
		Map<Integer,Insure> insures = findUpdatedInsures(playerBlob.getPlayerid());
		playerBlob.setInsure(JSON.toJSONString(insures));		
		Map<Integer,Saving> savings = findUpdatedSavings(playerBlob.getPlayerid());
		playerBlob.setSaving(JSON.toJSONString(savings));
		Map<Integer,List<Stock>> stocks = StockManager.getInstance().findMapStocks(playerBlob.getPlayerid());
		playerBlob.setStock(JSON.toJSONString(stocks));	
//		Toplist toplist = ToplistManager.getInstance().findByPlayerId(playerBlob.getPlayerid());
//		float fMm = 0;
//		if (toplist!=null){
//			fMm = toplist.getMoney().floatValue();
//			playerBlob.setZan(toplist.getZan());
//		}

		//800ms/1k
//		int top = ToplistManager.getInstance().findCountByGreaterMoney(playerBlob.getPlayerid(),0,fMm);
//		playerBlob.setWeektop(top+1);
		
//		//取需要更新的模块id
		JSONObject obj = JSONObject.fromObject(playerBlob);	
		return obj.toString();
	}
	
	//
	public synchronized Map<Integer,Saving> findUpdatedSavings(int playerId)
	{
		List<Integer> savingids = SavingManager.getInstance().getSavingIds(playerId);
		Map<Integer,Saving>	 mdata = new HashMap<Integer,Saving>();
		if (savingids==null||savingids.size()<=0)
			return mdata;
		
		Date currDate = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(currDate);
	    Calendar c2 = Calendar.getInstance(); 
		Saving liveSaving = null;
		boolean liveUpdate = false;
		for (Integer itemid:savingids){
			Saving saving = SavingManager.getInstance().getSaving(playerId, itemid);
			float inter = 0;
	        c2.setTime(saving.getUpdatetime());
	        float diffdd = Base.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
	        float periodMinutes = saving.getPeriod()*60*24;//天:分钟
			if (saving.getType()==0)		//活期
			{
				liveSaving = saving;
				periodMinutes = 60*24;//天:分钟
				long diff = (long)(diffdd/periodMinutes);
				inter = diff * saving.getAmount()*saving.getRate()/100;
				if (inter>1){
					liveUpdate = true;
					saving.setAmount(saving.getAmount()+inter);					
				}else
					inter = 0;		//不算利息
			}else if (isSavingTimeout(saving))		//定期到期
			{
				liveUpdate = true;
				saving.setStatus((byte)1);
				SavingManager.getInstance().updateSaving(playerId, saving);
				log.debug("pid:"+playerId+" saving timeout,get inter:"+saving.getItemid()+",inter: "+inter);
			}
			Saving usaving = new Saving();
			usaving.setItemid(saving.getItemid());
			usaving.setAmount(saving.getAmount());
			usaving.setCreatetime(saving.getCreatetime());
			usaving.setQty(saving.getQty());
			usaving.setProfit(inter);
//			log.warn("add usersaving:"+JSON.toJSONString(usaving));
			mdata.put(saving.getItemid(), usaving);
		}
		if (liveUpdate==true){
			SavingManager.getInstance().updateLiveSaving(liveSaving);
			super.playerTopUpdate(playerId);			
		}
//		log.warn("mdata return :"+JSON.toJSONString(mdata));
		return mdata;
	}

	public synchronized String loginPlayer(Player playerBlob)
	{
		if (playerBlob!=null){
			if (!playerBlob.getOpenid().equals(player.getOpenid()))
				return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
			
			log.warn("pid:"+playerBlob.getPlayerid()+",openid:"+player.getOpenid()+" login success,name:"+player.getPlayername());
			boolean newAssign = assignDailyQuest(playerBlob);
			if (newAssign){
				log.warn("pid:"+playerBlob.getPlayerid()+" login assign quest:"+playerBlob.getQuestStr());
				DataManager.getInstance().updatePlayerQuest(playerBlob);
			}
			return serialize(playerBlob,0,null,0); 
		}else
			return super.msgStr(RetMsg.MSG_WrongPlayerNameOrPwd);	
	}
	public synchronized String login()
	{
//		String loginstr = "";
//		if (player.getOpenid()!=null&&player.getOpenid().length()>0)
//			loginstr += "openid:'"+player.getOpenid()+"'";
//		if (player.getPlayername()!=null&&player.getPlayername().length()>0)
//			loginstr += ",pname:"+player.getPlayername();
//		loginstr += " enter game";
//		//log.info(loginstr);
		if (player.getPlayerid()>0){
			Player playerBlob = DataManager.getInstance().findPlayer(player.getPlayerid());
			return loginPlayer(playerBlob);
		}
		long s = System.currentTimeMillis();
		Player playerBlob = DataManager.getInstance().findPlayer(player.getPlayername());
		long cost = System.currentTimeMillis()-s;
		if (cost>10)		
			log.warn("action cost :"+cost+" openid:"+player.getOpenid());
		if (playerBlob==null)
		{
			return register();
		}else{		//名字登陆
			return loginPlayer(playerBlob);
		}
	}
	
	public String get(PlayerWithBLOBs playerBlob)
	{
		String pdata = getPlayerJsonData(playerBlob);
//		write(pdata,"utf-8");
		log.debug("player("+pdata+") found");
		return pdata;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	private synchronized String serialize(Player player,int isregister,String savingStr,int top){
		float margin = StockManager.getInstance().getMarginSec();
		player.setQuotetime(margin);
		String pp = JSON.toJSONString(player);
		String data = "{player:"+pp+",flag:"+isregister;
		if (isregister==1){
			data += ",saving:"+savingStr+",top:"+top;
		}
		data += "}";
	//  log.warn("login:"+data);
	  return data;
	}
	
	public static void main(String[] args){
		LoginAction l = new LoginAction();
		Player p = new Player();
		p.setQuestStr("2");
//		p.setQuestDoneTime(new Date());
		l.assignDailyQuest(p);
		System.out.println(p.getQuestStr());
	}
	public boolean assignDailyQuest(Player p){
		Date qdoneTime = p.getQuestDoneTime();
		Date now = new Date();
		if (qdoneTime!=null){
			if (qdoneTime.getDay()==now.getDay()&&qdoneTime.getMonth()==now.getMonth()&&qdoneTime.getYear()==now.getYear())
				return false;
		}
		Date assignTime = p.getQuestassigntime();
		if (assignTime!=null){
			if (assignTime.getDay()==now.getDay()&&assignTime.getMonth()==now.getMonth()&&assignTime.getYear()==now.getYear())
				return false;
		}		
		
		int assignC = 2;
		int notDoneId = -1;
		String queststr = p.getQuestStr();
		if (queststr!=null&&queststr.length()>0){
			String[] qs = queststr.split(",");
			assignC = 2 - qs.length;
			if (qs.length>0)
				notDoneId = Integer.valueOf(qs[0]);
		}
		if (assignC<=0){
			return false;
		}
		
		Vector<Integer> qids = new Vector<Integer>();
		for (int i=1;i<=5;i++){
			if (i==notDoneId) continue;
			qids.add(i);
		}
		
		int indx1 = (int)(Math.random()*qids.size());
		int qid1 = qids.get(indx1);
		if (assignC==1){
			queststr += ","+qid1;
		}else
			queststr = String.valueOf(qid1);
		qids.remove(indx1);
		if (assignC>1){
			int indx2 = (int)(Math.random()*qids.size());
			int qid2 = qids.get(indx2);
			queststr += ","+qid2;
		}
		p.setQuestStr(queststr);
		p.setQuestassigntime(new Date());
		return true;
	}
	
	public synchronized String register(){
			//System.out.println("玩家注册:"+player.getPlayername());
			PlayerWithBLOBs playerBlob = new PlayerWithBLOBs();
			
			//注册奖励:
			Init init = DataManager.getInstance().getInit();
			if (init!=null){
				playerBlob.setExp(init.getExp());
			}
			
	//		String ipAddr = getHttpRequest().getRemoteAddr();
			Date time = new Date(); 
			playerBlob.setAccountid(1);
			playerBlob.setOpenid(player.getOpenid());
			playerBlob.setPlayername(player.getPlayername());
			playerBlob.setSex(player.getSex());
			playerBlob.setCreatetime(time);
			playerBlob.setPwd("0");
			playerBlob.setZan(0);
			playerBlob.setPlayerid(DataManager.getInstance().assignNextId());
			assignDailyQuest(playerBlob);
			log.warn("pid:"+playerBlob.getPlayerid()+" register assign quest:"+playerBlob.getQuestStr());
			boolean ret = DataManager.getInstance().addPlayer(playerBlob);
			if (ret==false){
				return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
			}
			Map<Integer, Saving> savings = (new HashMap<Integer,Saving>());
			int top = -1;
			//活期存款:
			if (init!=null&&init.getMoney()>0){
				Saving savingCfg = SavingManager.getInstance().getSavingCfg(1);
				Saving saving = new Saving();
				saving.setName(savingCfg.getName());
				saving.setPeriod(savingCfg.getPeriod());
				saving.setRate(savingCfg.getRate());
				saving.setItemid(savingCfg.getId());
				saving.setQty(1);
				saving.setUpdatetime(time);
				saving.setType(savingCfg.getType());
				saving.setPlayerid(playerBlob.getPlayerid());
				saving.setAmount(Float.valueOf(init.getMoney().intValue()));
				saving.setCreatetime(time);
				SavingManager.getInstance().addFirstSaving(saving.getPlayerid(), saving);
				savings.put(saving.getItemid(), saving);
				top = DataManager.getInstance().get_registerTop(saving.getAmount());
//				super.playerTopUpdate(playerBlob.getPlayerid());
//				ToplistManager.getInstance().addToplist(playerBlob.getPlayerid(),playerBlob.getPlayername(),saving.getAmount());	
			}
//			JSONObject obj = JSONObject.fromObject(playerBlob);	
			log.warn("pid:"+playerBlob.getPlayerid()+",openid:'"+playerBlob.getOpenid()+"' register success:,name:"+player.getPlayername());
//			write(obj.toString(),"utf-8");
			
			return serialize(playerBlob,1,JSON.toJSONString(savings),top);
		}
}
