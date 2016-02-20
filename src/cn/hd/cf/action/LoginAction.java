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
	private Player player;
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
    	LogMgr.getInstance().log(json);
		return 0;
	}
	
	public synchronized String getPlayerJsonData(Player playerBlob)
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
	
	//
	public synchronized String get_savingAndInsure(int playerId)
	{
		Map<Integer,Saving>	 mdata = new HashMap<Integer,Saving>();
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerId);
		if (savings==null||savings.size()<=0)
			return JSON.toJSONString(mdata);
		
		Saving liveSaving = null;
		int liveIndex = 0;
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
			}else if (isSavingTimeout(saving))		//定期到期
			{
				hasUpdate = true;
				saving.setStatus((byte)1);
				log.debug("pid:"+playerId+" saving timeout");
			}
			saving.setProfit(inter);
		}
		float oriLiveValue = liveSaving.getAmount();
		Map<Integer,Insure>	insures = new HashMap<Integer,Insure>();	
		//保险返回数据
		float insureamount = findUpdatedInsures(playerId,liveSaving,insures);
		if (liveSaving.getAmount()!=oriLiveValue){
			savings.get(liveIndex).setAmount(liveSaving.getAmount());
			hasUpdate = true;
		}
		
		
		//存款返回数据:
		float savingamount = 0;		
		for (Saving item:savings){
			Saving usaving = new Saving();
			usaving.setItemid(item.getItemid());
			usaving.setAmount(item.getAmount());
			usaving.setCreatetime(item.getCreatetime());
			usaving.setQty(item.getQty());
			usaving.setProfit(item.getProfit());
			savingamount += item.getAmount();
			mdata.put(usaving.getItemid(), usaving);			
		}
		savingamount = Float.valueOf(savingamount).intValue();
		
		//股票返回数据:
		float stockamount = 0;
		Map<Integer,List<Stock>> stocks = new HashMap<Integer,List<Stock>>();		
		List<Stock> ss = StockManager.getInstance().getStockList(playerId);
		if (ss!=null){
		for (int i=0;i<ss.size();i++){
			Stock ps = ss.get(i);
			if (ps==null) continue;
			List<Stock> list = stocks.get(ps.getItemid());
			if (list==null){
				list = new ArrayList<Stock>();
				stocks.put(ps.getItemid(), list);
			}
			List<Quote> qq = StockManager.getInstance().getLastQuotes(ps.getItemid());
			if (qq.size()>0)
				stockamount += qq.get(0).getPrice()*ps.getQty();			
			list.add(ss.get(i));
		}		
		}
		stockamount = Float.valueOf(stockamount).intValue();
		float amount = savingamount + insureamount + stockamount;
		int top = ToplistManager.getInstance().findCountByGreaterMoney(
				playerId, 0, amount);
		top++;
		
		if (hasUpdate==true){
			SavingManager.getInstance().updateSavings(playerId,savings);
			Player player2 = DataManager.getInstance().findPlayer(playerId);	
			if (player2!=null){
				LogMgr.getInstance().log("pid:"+playerId+" update toplist:"+top);
				ToplistManager.getInstance().updateToplist(playerId,player2.getPlayername(),amount);
			}
		}	
		if (top==0){
			Player player2 = DataManager.getInstance().findPlayer(playerId);	
			if (player2!=null){
				LogMgr.getInstance().log("pid:"+playerId+" update toplist:"+top);
				ToplistManager.getInstance().updateToplist(playerId,player2.getPlayername(),amount);
				top = ToplistManager.getInstance().findCountByGreaterMoney(
						playerId, 0, amount);
				top++;
			}
		}
		String data = JSON.toJSONString(mdata)+";"+JSON.toJSONString(insures)+";"+JSON.toJSONString(stocks)+";"+top;
		LogMgr.getInstance().log("pid:"+playerId+" get login data:"+data);
		return data;
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
//			LogMgr.getInstance().log("add usersaving:"+JSON.toJSONString(usaving));
			mdata.put(saving.getItemid(), usaving);
		}
		if (liveUpdate==true){
			SavingManager.getInstance().updateLiveSaving(liveSaving);
			super.playerTopUpdate(playerId);			
		}
//		LogMgr.getInstance().log("mdata return :"+JSON.toJSONString(mdata));
		return mdata;
	}

	public synchronized String loginPlayer(Player player)
	{
		if (player!=null){
			if (!player.getOpenid().equals(player.getOpenid()))
				return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
			
			LogMgr.getInstance().log("pid:"+player.getPlayerid()+" login,openid:"+player.getOpenid()+",name:"+player.getPlayername());
			int assignCode = assignDailyQuest(player);
			if (assignCode==1){
				LogMgr.getInstance().log("pid:"+player.getPlayerid()+" assign quest,login:"+player.getQuestStr());
			}
			boolean newSignin = countSignin(player);
			if (newSignin){
				LogMgr.getInstance().log("pid:"+player.getPlayerid()+" reset signin:"+player.getSigninCount());
			}
			if (newSignin||assignCode==1||assignCode==2){
				DataManager.getInstance().updatePlayerQuest(player);
			}
			return serialize(player,0,null); 
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
		Player playerBlob = null;
		if (player.getPlayerid()>0){
			playerBlob = DataManager.getInstance().findPlayer(player.getPlayerid());
//			LogMgr.getInstance().log("find player:"+playerBlob);
			if (playerBlob!=null)
				return loginPlayer(playerBlob);
		}
		
		long s = System.currentTimeMillis();
		playerBlob = DataManager.getInstance().findPlayer(player.getPlayername());
		long cost = System.currentTimeMillis()-s;
		if (cost>10)		
			LogMgr.getInstance().log("action cost :"+cost+" openid:"+player.getOpenid());
		if (playerBlob==null)
		{
			return register();
		}else if (!playerBlob.getOpenid().equalsIgnoreCase(player.getOpenid())){
			return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
		}else {		//名字登陆
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
	
	public static int daysOfTwo(Date fDate, Date oDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(oDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	  }
	
	private synchronized boolean countSignin(Player p){
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
	
	private synchronized String serialize(Player player,int isregister,String savingStr){
		float margin = StockManager.getInstance().getMarginSec();
		player.setQuotetime(margin);
		String pp = JSON.toJSONString(player);
		String data = "{player:"+pp+",flag:"+isregister;
		if (isregister==1){
			data += ",saving:"+savingStr;
		}
		data += "}";
	  LogMgr.getInstance().log("pid:"+player.getPlayerid()+" loginback:"+data);
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
	
	public static void main(String[] args){
		LoginAction l = new LoginAction();
		SavingManager.getInstance().init();
		
	}
	
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
	
	public synchronized String register(){
			//System.out.println("玩家注册:"+player.getPlayername());
			Player playerBlob = new Player();
			
			//注册奖励:
			Init init = DataManager.getInstance().getInit();
			if (init!=null){
				playerBlob.setExp(init.getExp());
			}
			
	//		String ipAddr = getHttpRequest().getRemoteAddr();
			playerBlob.setOpenid(player.getOpenid());
			playerBlob.setPlayername(player.getPlayername());
			playerBlob.setSex(player.getSex());
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			String cstr = formatter.format(new Date());
			playerBlob.setCreateTimeStr(cstr);
			playerBlob.setPlayerid(DataManager.getInstance().assignNextId());
			assignDailyQuest(playerBlob);
			LogMgr.getInstance().log("pid:"+playerBlob.getPlayerid()+" assign quest register:"+playerBlob.getQuestStr());
			boolean ret = DataManager.getInstance().addPlayer(playerBlob);
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
			LogMgr.getInstance().log("pid:"+playerBlob.getPlayerid()+" register success,openid:'"+playerBlob.getOpenid()+"',name:"+player.getPlayername());
//			write(obj.toString(),"utf-8");
			
			return serialize(playerBlob,1,JSON.toJSONString(savings));
		}
}
