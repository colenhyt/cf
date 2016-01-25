package cn.hd.cf.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerId);
		Map<Integer,Saving>	 mdata = new HashMap<Integer,Saving>();
		if (savings==null)
			return mdata;
		
		Date currDate = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(currDate);
	    Calendar c2 = Calendar.getInstance(); 
		Saving liveSaving = null;
		boolean liveUpdate = false;
		for (int i=0;i<savings.size();i++){
			Saving saving = savings.get(i);
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
			mdata.put(saving.getItemid(), usaving);
		}
		if (liveUpdate==true){
			SavingManager.getInstance().updateLiveSaving(liveSaving);
			super.playerTopUpdate(playerId);			
		}
		
		return mdata;
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
			if (playerBlob!=null)
				return serialize(playerBlob); 
		}
		Player playerBlob = DataManager.getInstance().findPlayer(player.getPlayername());
		if (playerBlob==null)
		{
			return register();
		}else if (playerBlob.getOpenid()==null||!playerBlob.getOpenid().equals(player.getOpenid())){		//昵称已被注册
			return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
		}
		
		//log.warn("openid:'"+player.getOpenid()+"',pid:"+playerBlob.getPlayerid()+" login success,name:"+player.getPlayername());
		
		return serialize(playerBlob);
	}
	
	public String get(PlayerWithBLOBs playerBlob)
	{
		String pdata = getPlayerJsonData(playerBlob);
//		write(pdata,"utf-8");
		log.debug("player("+pdata+") found");
		return pdata;
	}
	
	public String update()
	{	
		String pp = getHttpRequest().getParameter("playerdata");
		JSONObject ppObj = JSONObject.fromObject(pp);
		PlayerWithBLOBs playerBlob = (PlayerWithBLOBs)JSONObject.toBean(ppObj,PlayerWithBLOBs.class);
		if (playerBlob==null)
		{
			log.debug("no player found:playerid:"+player.getPlayerid());
			return null;
		}
		boolean ret = DataManager.getInstance().updatePlayer(playerBlob);
		
		log.debug("update player("+ppObj.toString()+"):ret: "+ret);
		writeMsg(RetMsg.MSG_OK);
		return null;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	private synchronized String serialize(Player player){
//		float margin = StockManager.getInstance().getMarginSec();
//		player.setQuotetime(margin);
		String data = JSON.toJSONString(player);
	//  log.warn("login:"+data);
	  return data;
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
			boolean ret = DataManager.getInstance().addPlayer(playerBlob);
			if (ret==false){
				return super.msgStr(RetMsg.MSG_PlayerNameIsExist);
			}
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
				SavingManager.getInstance().addSaving(saving.getPlayerid(), saving);
//				super.playerTopUpdate(playerBlob.getPlayerid());
//				ToplistManager.getInstance().addToplist(playerBlob.getPlayerid(),playerBlob.getPlayername(),saving.getAmount());	
			}
//			JSONObject obj = JSONObject.fromObject(playerBlob);	
			log.warn("openid:'"+playerBlob.getOpenid()+"', pid:"+playerBlob.getPlayerid()+",register success:,name:"+player.getPlayername());
//			write(obj.toString(),"utf-8");
			return serialize(playerBlob);
		}
}
