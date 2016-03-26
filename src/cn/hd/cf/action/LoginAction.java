package cn.hd.cf.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import cn.hd.base.Base;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.LogMgr;
import cn.hd.mgr.SavingManager;
import cn.hd.mgr.StockManager;
import cn.hd.mgr.ToplistManager;
import cn.hd.util.DateUtil;
import cn.hd.util.HttpXmlClient;

import com.alibaba.fastjson.JSON;


public class LoginAction extends SavingAction {
	Pattern p = Pattern.compile("[1-5]+");
	
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
//    	LogMgr.getInstance().log(json);
		return 0;
	}
	
	public String getPlayerJsonData(Player playerBlob)
	{
		String insures = DataManager.getInstance().get_insure2(playerBlob.getPlayerid());
		playerBlob.setInsure(insures);		
		
		String savings = DataManager.getInstance().get_saving2(playerBlob.getPlayerid());
		playerBlob.setSaving(savings);
		
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
	
	/**
	 * 获取玩家存款保险数据
	 * @param playerid
	 * @return string json返回数据
	 * */
	public String get_savingAndInsure(int playerId)
	{
		Map<Integer,Saving>	 mdata = new HashMap<Integer,Saving>();
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerId);
		if (savings==null||savings.size()<=0)
			return JSON.toJSONString(mdata);
		
		Saving liveSaving = null;
		int liveIndex = 0;
		float savingamount = 0;
		boolean hasUpdate = false;
		for (int i=0;i<savings.size();i++){
			float inter = 0;
			Saving saving = savings.get(i);
			if (saving.getType()==0)		//活期
			{
				inter = getLiveInter(saving);	//活期利息
				liveSaving = saving;
				liveIndex = i;
				if (inter>=1){
					hasUpdate = true;
					saving.setAmount(saving.getAmount()+inter);	
					saving.setUpdatetime(new Date());
				}else
					inter = 0;		//不算利息
			}else {
				savingamount += saving.getAmount();
				if (isSavingTimeout(saving)){		//定期到期
					hasUpdate = true;
					saving.setStatus((byte)1);
					LogMgr.getInstance().log(playerId,"saving timeout,itemid:"+saving.getItemid());
				}
			}
			saving.setProfit(inter);
			mdata.put(saving.getItemid(), saving);			
		}
		float oriLiveValue = liveSaving.getAmount();
		Map<Integer,Insure>	insures = new HashMap<Integer,Insure>();	
		//保险返回数据
		float insureamount = findUpdatedInsures(playerId,liveSaving,insures);
		if (liveSaving.getAmount()!=oriLiveValue){
			savings.get(liveIndex).setAmount(liveSaving.getAmount());
			mdata.get(1).setAmount(liveSaving.getAmount());
			hasUpdate = true;
		}
		
		if (hasUpdate==true){
			savingamount += liveSaving.getAmount();
			SavingManager.getInstance().updateSavings(playerId,savings);
			float amount = savingamount + insureamount + StockManager.getInstance().getStockAmount(playerId);
			ToplistManager.getInstance().updateToplist(playerId,null,amount);	
		}	
		String data = JSON.toJSONString(mdata)+";"+JSON.toJSONString(insures);
		LogMgr.getInstance().log(playerId," get login data:"+data);
		return data;
	}


	/**
	 * 登录玩家数据返回
	 * @param p 
	 * @return string json返回数据
	 * */
	public String loginPlayer(Player p,String openid,long clientSessionid)
	{
		if (!p.getOpenid().equals(openid))
			return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
		
		DataManager mgr = DataManager.getInstance();
		long canReq = mgr.canLogin(p.getPlayerid(), clientSessionid,false);
		if (canReq<0)
			return super.msgStr((int)canReq);
		
		p.setSessionid(canReq);
			
		LogMgr.getInstance().log(p.getPlayerid()," login,openid:"+p.getOpenid()+",name:"+p.getPlayername()+",session:"+canReq);
		int assignCode = assignDailyQuest(p);
		if (assignCode==1){
			LogMgr.getInstance().log(p.getPlayerid()," assign quest,login:"+p.getQuestStr());
		}
		boolean newSignin = countSignin(p);
		if (newSignin){
			LogMgr.getInstance().log(p.getPlayerid()," reset signin:"+p.getSigninCount());
		}
		if (newSignin||assignCode==1||assignCode==2){
			mgr.updatePlayerQuest(p);
		}
		return serialize(p,0,null); 
	}
	/**
	 * 注册登录入口
	 * @param player对象
	 * @return string json返回数据
	 * */
	public String login(int playerid,String playername,String openid,byte sex,long clientSessionid)
	{
		DataManager mgr = DataManager.getInstance();
		if (playerid>0){
			Player playerBlob = mgr.findPlayer(playerid);
//			LogMgr.getInstance().log("find player:"+playerBlob);
			if (playerBlob!=null) {
				return loginPlayer(playerBlob,openid,clientSessionid);
			}
		}
		
		long s = System.currentTimeMillis();
		Player playerBlob2 = mgr.findPlayer(playername);
		long cost = System.currentTimeMillis()-s;
		if (cost>10)		
			log.warn("action cost :"+cost+" openid:"+openid);
		if (playerBlob2==null)
		{
			return register(playername,openid,sex,clientSessionid);
		}else if (!playerBlob2.getOpenid().equalsIgnoreCase(openid)){
			return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
		}else {		//名字登陆
			return loginPlayer(playerBlob2,openid,clientSessionid);
		}
	}
	
	public String get(PlayerWithBLOBs playerBlob)
	{
		String pdata = getPlayerJsonData(playerBlob);
//		write(pdata,"utf-8");
		log.debug("player("+pdata+") found");
		return pdata;
	}
	
	
	/**
	 * 判断两天差值
	 * @param fDate 前一天，oDate后一天
	 * @return int 天数
	 * */
	public static int daysOfTwo(Date fDate, Date oDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(oDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	  }
	
	/**
	 * 计算签到天数
	 * @param player 对象
	 * @return boolean ture为要签到，false为不需要签到
	 * */
	private boolean countSignin(Player p){
		Date last = p.getLastlogin();
		if (DateUtil.isToday(last)){
			return false;
		}
		if (DateUtil.isToday(p.getSigninCountTime())){
			return false;
		}

		if (last!=null){
			Date now = new Date();
			int dayBet = daysOfTwo(last,now);
			if (dayBet==1&&p.getSigninCount()<=7){
				p.setSigninCount(p.getSigninCount()+1);
			}else{
				p.setSigninCount(1);
			}
		}else
			p.setSigninCount(1);
		
		//设置设值时间:
		p.setSigninCountTime(new Date());
		
		return true;
	}
	
	private String serialize(Player player,int isregister,String savingStr){
//		float margin = StockManager.getInstance().getMarginSec();
		player.setQuotetime((float)0);
		String pp = JSON.toJSONString(player);
		String data = "{player:"+pp+",flag:"+isregister;
		if (isregister==1){
			data += ",saving:"+savingStr;
		}
		data += "}";
	  LogMgr.getInstance().log(player.getPlayerid()," loginback:"+data);
	  return data;
	}
	
	public Vector<Integer> getInts(String queststr){
		Vector<Integer> strs = new Vector<Integer>();
		if (queststr!=null&&queststr.trim().length()>0){
			String[] qs = queststr.trim().split(",");
			for (int i=0;i<qs.length;i++){
				Matcher m = p.matcher(qs[i].trim());		
				if (m.matches()){
					strs.add(Integer.valueOf(qs[i].trim()));
				}
			}
		}
		return strs;
	}
	
	/**
	 * 分配每日任务
	 * @param player对象
	 * @return string json返回数据
	 * */
	public int assignDailyQuest(Player p){
		Date qdoneTime = p.getQuestDoneTime();
		if (DateUtil.isToday(qdoneTime))
			return 0;
		Date assignTime = p.getQuestassigntime();
		if (DateUtil.isToday(assignTime))
			return 0;
		
		Vector<Integer> qss = getInts(p.getQuestStr());
		if (qss.size()>=2){
			p.setQuestassigntime(new Date());		//不需要分配，重置分配时间
			return 2;
		}
		
		int notDoneId = -1;
		if (qss.size()>0)
			notDoneId = qss.get(0);
		
		Vector<Integer> qids = new Vector<Integer>();
		for (int i=1;i<=5;i++){
			if (i==notDoneId) continue;
			qids.add(i);
		}
		
		int indx1 = (int)(Math.random()*qids.size());
		int qid1 = qids.get(indx1);
		qss.add(qid1);
		if (qss.size()<2){ //再随机一个:
			qids.remove(indx1);
			int indx2 = (int)(Math.random()*qids.size());
			int qid2 = qids.get(indx2);
			qss.add(qid2);
		}
		String queststr = "";
		for (int i=0;i<qss.size();i++){
			queststr += qss.get(i);
			if (i==0)
				queststr += ",";
		}
		p.setQuestStr(queststr);
		p.setQuestassigntime(new Date());
		return 1;
	}
	
	/**
	 * 注册入口和数据返回
	 * @param String playername
	 * @return string json返回数据
	 * */
	public String register(String playername,String openid,byte sex,long clientSessionid){
		DataManager mgr = DataManager.getInstance();

		//System.out.println("玩家注册:"+player.getPlayername());
			Player playerBlob = new Player();
			
			//注册奖励:
			Init init = mgr.getInit();
			if (init!=null){
				playerBlob.setExp(init.getExp());
			}
			
	//		String ipAddr = getHttpRequest().getRemoteAddr();
			playerBlob.setOpenid(openid);
			playerBlob.setPlayername(playername);
			playerBlob.setSex(sex);
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			String cstr = formatter.format(new Date());
			playerBlob.setCreateTimeStr(cstr);
			playerBlob.setPlayerid(mgr.assignNextId());
			
			long canReq = mgr.canLogin(playerBlob.getPlayerid(), clientSessionid,true);
			if (canReq<0)
				return super.msgStr((int)canReq);
			
			playerBlob.setSessionid(canReq);
			
			assignDailyQuest(playerBlob);
			LogMgr.getInstance().log(playerBlob.getPlayerid()," assign quest register:"+playerBlob.getQuestStr());
			boolean ret = mgr.addPlayer(playerBlob);
			if (ret==false){
				return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
			}
			Map<Integer, Saving> savings = (new HashMap<Integer,Saving>());
			//活期存款:
			if (init!=null&&init.getMoney()>0){
				Date time = new Date(); 
				Saving savingCfg = SavingManager.getInstance().getSavingCfg(1);
				Saving saving = new Saving();
				saving.setName(savingCfg.getName());
				saving.setPeriod(savingCfg.getPeriod());
				saving.setRate(savingCfg.getRate());
				saving.setItemid(savingCfg.getId());
				saving.setQty(1);
				saving.setUpdatetime(time);
				saving.setCreatetime(time);
				saving.setType(savingCfg.getType());
				saving.setPlayerid(playerBlob.getPlayerid());
				saving.setAmount(Float.valueOf(init.getMoney().intValue()));
				SavingManager.getInstance().addFirstSaving(saving.getPlayerid(), saving);
				savings.put(saving.getItemid(), saving);
				//top = DataManager.getInstance().get_registerTop(saving.getAmount());
				ToplistManager.getInstance().addRegisterToplist(playerBlob.getPlayerid(),playerBlob.getPlayername(),saving.getAmount());	
			}
//			JSONObject obj = JSONObject.fromObject(playerBlob);	
			LogMgr.getInstance().log(playerBlob.getPlayerid()," register success,openid:'"+playerBlob.getOpenid()+"',name:"+playername);
//			write(obj.toString(),"utf-8");
			
			return serialize(playerBlob,1,JSON.toJSONString(savings));
		}
}
