package cn.hd.cf.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.base.Base;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.InsureManager;
import cn.hd.mgr.SavingManager;
import cn.hd.mgr.StockManager;
import cn.hd.mgr.ToplistManager;

import com.alibaba.fastjson.JSON;

public class SavingAction extends BaseAction {
	public Saving		saving;
	
	public Saving getSaving() {
		return saving;
	}

	public void setSaving(Saving saving) {
		this.saving = saving;
	}

	//更新活期存款金钱:
	public String updatelive()
	{
		Saving currLive = SavingManager.getInstance().getSaving(saving.getPlayerid(), 1);
		currLive.setAmount(saving.getAmount());
		currLive.setUpdatetime(new Date());	
		boolean update = SavingManager.getInstance().updateSavingAmount(currLive);	
		
		log.warn("pid:"+saving.getPlayerid()+" livesaving update value="+currLive.getAmount()+";itemid="+currLive.getItemid());
		 playerTopUpdate(saving.getPlayerid());
		 
		if (update==false){
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
		}else
			super.writeMsg(RetMsg.MSG_OK);
		return null;
	}
	
	public int pushLive(int playerId,float amount)
	{
		Saving saving2 = SavingManager.getInstance().getSaving(playerId, 1);
		if (saving2!=null){
			float newAmount = saving2.getAmount()+amount;
			if (newAmount<0)
				return RetMsg.MSG_MoneyNotEnough;
			
			saving2.setAmount(saving2.getAmount()+amount);
			playerMoneyUpdate(saving2);		
			return RetMsg.MSG_OK;
		}else {
			log.warn("pid:"+playerId+",error,could not found livsaving for playerid:");
			return RetMsg.MSG_NoSavingData;
		}
	}	
	
	public boolean playerMoneyUpdate(Saving saving){
		log.info("pid:"+saving.getPlayerid()+" liveupdate value="+saving.getAmount());
		boolean u = SavingManager.getInstance().updateLiveSaving(saving);	
		 playerTopUpdate(saving.getPlayerid());
		//更新排行榜金钱:
		return u;
	}
	
	public synchronized boolean playerTopUpdate(int playerid){
		Player player = DataManager.getInstance().findPlayer(playerid);
		if (player!=null){
			float money = ToplistManager.getInstance().calculatePlayerMoney(playerid);
			 ToplistManager.getInstance().updateToplist(playerid,player.getPlayername(),money);			
				//System.out.println("排行榜金钱更新:"+playerid+";money="+money);
			return true;
		}
		
		return false;
	}


	//存款是否到期:
	public boolean isSavingTimeout(Saving saving)
	{
		//活期不存在存款取款:
		if (saving.getItemid()==1){
			return false;
		}		
		
		Date currDate = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(currDate);
	    Calendar c2 = Calendar.getInstance(); 
        c2.setTime(saving.getUpdatetime());
        float diffdd = Base.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
       float periodMinutes = saving.getPeriod()*60*24;//天:分钟
//       System.out.println("剩余时间: "+diffdd+",周期时间:"+periodMinutes);
       if ((diffdd-periodMinutes)>0.001)		//定期到期
		{
			return true;
		}		
		return false;
	}
	
	protected float getLiveInter(Saving saving)
	{
		Saving savingCfg = SavingManager.getInstance().getSavingCfg(1);
		 Calendar c2 = Calendar.getInstance(); 
        c2.setTime(saving.getUpdatetime());
        Date currDate = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(currDate);
	    
        float diffdd = Base.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
		float periodMinutes = 60*24;//天:分钟
		float diff = (diffdd/periodMinutes);
		float inter = diff * saving.getAmount()*savingCfg.getRate()/100;	
		return inter;
	}
	
	public synchronized String add()
	{
		//活期不存在存款取款:
		if (saving.getItemid()==1){
			return msgStr(RetMsg.MSG_NoSavingData);
		}
		
		Saving newsaving = new Saving();
		newsaving.setItemid(saving.getItemid());
		newsaving.setPlayerid(saving.getPlayerid());
		newsaving.setAmount(saving.getAmount());
		int ret = RetMsg.MSG_OK;
		
	
		List<Saving> list = SavingManager.getInstance().getSavingList(saving.getPlayerid());
		Saving liveSaving = null;
		int liveIndex = 0;
    	for (int i=0;i<list.size();i++){
    		if (list.get(i).getItemid().intValue()==1){
    			liveSaving = list.get(i);
    			liveIndex = i;
    			break;
    		}
    	}		
    	
    	if (liveSaving==null){
    		return msgStr(RetMsg.MSG_NoSavingData);
    	}
    	
		float changeAmount = 0 - saving.getAmount();
		//取出存款:
		if (saving.getAmount()<0){
			Saving saving2 = null;
			int itemIndex = -1;
			for (int i=0;i<list.size();i++){
				if (list.get(i).getItemid().intValue()==saving.getItemid().intValue()){
					itemIndex = i;
					saving2 = list.get(i);
					break;
				}
			}			
			if (saving2==null){
				return msgStr(RetMsg.MSG_SavingNotExist);
			}
			//已到期的存款:
			boolean isout = isSavingTimeout(saving2);
			log.warn("pid:"+saving.getPlayerid()+" saving is out:"+isout);
			float inter  = 0;
			if ((saving.getStatus()!=null&&saving.getStatus()==1)||isout)		
			{
				inter = (saving2.getAmount()*saving2.getRate()/100);
			}else {			//按活期得到利息
				inter = getLiveInter(saving2);
			}
			if (Math.abs(inter-0.9)>0){
				BigDecimal b = new BigDecimal(inter);  
				int iInter = (int)b.setScale(0,BigDecimal.ROUND_HALF_UP).floatValue();
				changeAmount += iInter;	
				log.warn("pid:"+saving.getPlayerid()+" get inter:"+iInter);
				newsaving.setProfit((float)iInter);				
			}
			list.remove(itemIndex);
			ret = RetMsg.MSG_OK;
		}else {
			if (liveSaving.getAmount()<saving.getAmount())
				return msgStr(RetMsg.MSG_MoneyNotEnough);
			
			boolean found = false;
			for (int i=0;i<list.size();i++){
				if (list.get(i).getItemid().intValue()==saving.getItemid().intValue()){
					found = true;
					break;
				}
			}			
			if (found){
				return msgStr(RetMsg.MSG_SavingIsExist);
			}
			Saving savingCfg = SavingManager.getInstance().getSavingCfg(saving.getItemid());
			saving.setName(savingCfg.getName());
			Date d = new Date();
			saving.setCreatetime(d);
			saving.setUpdatetime(d);
			saving.setRate(savingCfg.getRate());
			saving.setQty(1);
			saving.setType(savingCfg.getType());
			saving.setPeriod(savingCfg.getPeriod());
			list.add(saving);
			ret = RetMsg.MSG_OK;
			boolean doneQuest = DataManager.getInstance().doneQuest(saving.getPlayerid(), 1);
			if (doneQuest){
				changeAmount += 5000;
				log.warn("pid:"+saving.getPlayerid()+" saving quest prize 5000");
			}
		}
		
		//修改活期金额:
		if (ret==RetMsg.MSG_OK){
			liveSaving.setAmount(liveSaving.getAmount()+changeAmount);
			newsaving.setLiveamount(liveSaving.getAmount());
			list.get(liveIndex).setAmount(liveSaving.getAmount());
			String str = JSON.toJSONString(newsaving);
			log.info("pid:"+saving.getPlayerid()+" add/remove saving itemid="+saving.getItemid()+",ret:"+ret+",amount:"+saving.getAmount()+", retdata:"+str);
			SavingManager.getInstance().updateSavings(saving.getPlayerid(), list);
			playerTopUpdate(saving.getPlayerid());
			return msgStr2(RetMsg.MSG_OK,str);
		}else {
			log.warn("pid:"+saving.getPlayerid()+",error,saving: "+ret);
			return msgStr(ret);
		}
	}

	public synchronized float findUpdatedInsures(int playerId,Saving liveSaving,Map<Integer,Insure> mdata)
		{
			List<Insure> insures = InsureManager.getInstance().getInsureList(playerId);
//			System.out.println("找到保险个数:"+playerId+" from db:"+insures.size()+",session:"+MybatisSessionFactory.getSession().toString());
			if (insures==null)
				return 0;
			
			float insureamount = 0;
			Date curr = new Date();
		    Calendar cCurr = Calendar.getInstance(); 
		    cCurr.setTime(curr);
		    Calendar c2 = Calendar.getInstance(); 
			
		    List<Insure> notTimeoutInsures = new ArrayList<Insure>();
			try {		
			for (Insure insure:insures){
				if (insure.getUpdatetime()==null) continue;
				float inter = 0;	// 0表明未到期
		        c2.setTime(insure.getUpdatetime());
				float diffdd = Base.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
				float periodMinutes = insure.getPeriod()*60; //天:分钟
//				System.out.println("(peroid:"+insure.getPeriod()+") diffdd:"+diffdd+",periodMinutes:"+periodMinutes);
	//			periodMinutes = 5;
				//到期:
				if ((diffdd-periodMinutes)>0.001)
				{
					//理财产品,得到收益:
					if (insure.getType()!=null&&insure.getType()==1){
					
						Insure incfg = InsureManager.getInstance().getInsureCfg(insure.getItemid());
						float oriAmount = insure.getAmount();
						inter = incfg.getProfit()*insure.getQty();
						liveSaving.setAmount(liveSaving.getAmount()+oriAmount+inter);
					}else {		//保险到期，移除
						inter = -1;
					}
					log.warn("pid:"+playerId+" insure timeout:"+JSON.toJSONString(insure));
//					System.out.println("insure overdate:"+insure.getItemid());
				}else {
					insureamount += insure.getAmount();
					notTimeoutInsures.add(insure);
				}
				
				Insure uinsure = new Insure();
				uinsure.setAmount(insure.getAmount());
				uinsure.setCreatetime(insure.getCreatetime());
				uinsure.setQty(insure.getQty());
				uinsure.setProfit(inter);
				mdata.put(insure.getItemid(), uinsure);
				InsureManager.getInstance().updateInsures(playerId, notTimeoutInsures);
			}
			}catch (Exception e){
				e.printStackTrace();
			}	
			insureamount = Float.valueOf(insureamount).intValue();
//			System.out.println("return insure :"+mdata.size());
			return insureamount;
		}

}
