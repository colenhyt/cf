package cn.hd.cf.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

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
	
	protected boolean playerMoneyUpdate(Saving saving){
		log.info("pid:"+saving.getPlayerid()+" liveupdate value="+saving.getAmount());
		boolean u = SavingManager.getInstance().updateLiveSaving(saving);	
		 playerTopUpdate(saving.getPlayerid());
		//更新排行榜金钱:
		return u;
	}
	
	public synchronized boolean playerTopUpdate(int playerid){
		Player player = DataManager.getInstance().findPlayer(playerid);
		if (player!=null){
			float money = calculatePlayerMoney(playerid);
			 ToplistManager.getInstance().updateToplist(playerid,player.getPlayername(),money);			
				//System.out.println("排行榜金钱更新:"+playerid+";money="+money);
			return true;
		}
		
		return false;
	}
	
	public float calculatePlayerMoney(int playerId){
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerId);
		float savingamount = 0;
		if (savings!=null){
			for (int i=0;i<savings.size();i++){
				savingamount += savings.get(i).getAmount();
			}			
		}
		savingamount = Float.valueOf(savingamount).intValue();
		float insureamount = 0;
		List<Insure> insures = InsureManager.getInstance().getInsureList(playerId);
		if (insures!=null){
		for (int i=0;i<insures.size();i++){
			insureamount += insures.get(i).getAmount();
		}
		}
		insureamount = Float.valueOf(insureamount).intValue();
		List<Stock> stocks = StockManager.getInstance().getStockList(playerId);
		float stockamount = 0;
		if (stocks!=null){
			for (int i=0;i<stocks.size();i++){
				Stock ps = stocks.get(i);
				if (ps==null) continue;
				List<Quote> qq = StockManager.getInstance().getLastQuotes(ps.getItemid());
				if (qq.size()>0)
					stockamount += qq.get(0).getPrice()*ps.getQty();
			}   			
		}
		stockamount = Float.valueOf(stockamount).intValue();
		float amount = savingamount + insureamount + stockamount;
		return amount;
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
		System.out.println("aa "+savingCfg.getRate()+";"+diff+";"+saving.getAmount());
		float inter = diff * saving.getAmount()*savingCfg.getRate()/100;	
		return inter;
	}
	
	public String add()
	{
		//活期不存在存款取款:
		if (saving.getItemid()==1){
			return msgStr(RetMsg.MSG_NoSavingData);
		}
		
		Saving newsaving = new Saving();
		newsaving.setItemid(saving.getItemid());
		newsaving.setPlayerid(saving.getPlayerid());
		int ret = RetMsg.MSG_OK;
		//取出存款:
		if (saving.getAmount()<0){
			float inAmount = 0 - saving.getAmount();
			Saving saving2 = SavingManager.getInstance().getSaving(saving.getPlayerid(), saving.getItemid());
			if (saving2==null){
				return msgStr(RetMsg.MSG_NoSavingData);
			}
			//已到期的存款:
			boolean isout = isSavingTimeout(saving2);
			System.out.println("is out:"+isout);
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
				inAmount += iInter;	
				System.out.println("得到利息:"+iInter);
				newsaving.setProfit((float)iInter);				
			}
			pushLive(saving.getPlayerid(), inAmount);		//放回活期;
			ret = SavingManager.getInstance().deleteSaving(saving.getPlayerid(),saving);
		}else {
			
			pushLive(saving.getPlayerid(), 0 - saving.getAmount());
			
			Saving savingCfg = SavingManager.getInstance().getSavingCfg(saving.getItemid());
			saving.setName(savingCfg.getName());
			saving.setCreatetime(new Date());
			saving.setUpdatetime(new Date());
			saving.setRate(savingCfg.getRate());
			saving.setQty(1);
			saving.setType(savingCfg.getType());
			saving.setPeriod(savingCfg.getPeriod());
			ret = SavingManager.getInstance().addSaving(saving.getPlayerid(), saving);
		}
		log.info("pid:"+saving.getPlayerid()+" add saving itemid="+saving.getItemid()+",ret:"+ret+",amount:"+saving.getAmount());
		
		//放回去
		if (ret!=RetMsg.MSG_OK){
			log.warn("pid:"+saving.getPlayerid()+",error,saving: "+saving.getPlayerid());
			pushLive(saving.getPlayerid(), saving.getAmount() );
			return msgStr(ret);
		}else {
			String str = JSON.toJSONString(newsaving);
			return msgStr2(RetMsg.MSG_OK,str);
		}
	}

	public synchronized Map<Integer,Insure> findUpdatedInsures(int playerId)
		{
			List<Insure> insures = InsureManager.getInstance().getInsureList(playerId);
//			System.out.println("找到保险个数:"+playerId+" from db:"+insures.size()+",session:"+MybatisSessionFactory.getSession().toString());
			Map<Integer,Insure>	mdata = new HashMap<Integer,Insure>();
			if (insures==null)
				return mdata;
			
			Date curr = new Date();
		    Calendar cCurr = Calendar.getInstance(); 
		    cCurr.setTime(curr);
		    Calendar c2 = Calendar.getInstance(); 
			
			try {		
			for (int i=0;i<insures.size();i++){
				Insure insure = insures.get(i);
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
						inter = incfg.getProfit()*insure.getQty();
						pushLive(playerId, insure.getAmount()+inter);
					}else {		//保险到期，移除
						inter = -1;
					}
//					System.out.println("insure overdate:"+insure.getItemid());
					//删除产品
					InsureManager.getInstance().deleteInsure(insure.getPlayerid(), insure);
				}
				
				Insure uinsure = new Insure();
				uinsure.setAmount(insure.getAmount());
				uinsure.setCreatetime(insure.getCreatetime());
				uinsure.setQty(insure.getQty());
				uinsure.setProfit(inter);
				mdata.put(insure.getItemid(), uinsure);
//				System.out.println("put insure :"+insure.getItemid());
			}
			}catch (Exception e){
				e.printStackTrace();
				return mdata;
			}	
//			System.out.println("return insure :"+mdata.size());
			return mdata;
		}

}
