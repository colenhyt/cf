package cn.hd.cf.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.hd.base.Base;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;
import cn.hd.cf.tools.InsuredataService;
import cn.hd.cf.tools.SavingdataService;
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

	protected SavingService savingService;
	protected StockService stockService;
	protected PlayerService playerService;
	
	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	public SavingService getSavingService() {
		return savingService;
	}

	//更新活期存款金钱:
	public String updatelive()
	{
		Saving currLive = SavingManager.getInstance().getSaving(saving.getPlayerid(), 1);
		currLive.setAmount(saving.getAmount());
		currLive.setUpdatetime(new Date());	
		boolean update = SavingManager.getInstance().updateSavingAmount(currLive);	
		
		log.warn("活期存款更新:"+saving.getPlayerid()+";value="+currLive.getAmount()+";itemid="+currLive.getItemid());
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
			System.out.println("could not found livsaving for playerid:"+playerId);
			return RetMsg.MSG_NoSavingData;
		}
	}	
	
	protected boolean playerMoneyUpdate(Saving saving){
		System.out.println("活期存款更新:"+saving.getPlayerid()+";value="+saving.getAmount());
		boolean u = SavingManager.getInstance().updateLiveSaving(saving);	
		 playerTopUpdate(saving.getPlayerid());
		//更新排行榜金钱:
		return u;
	}
	
	public synchronized boolean playerTopUpdate(int playerid){
		PlayerWithBLOBs player = DataManager.getInstance().findPlayer(playerid);
		if (player!=null){
			float money = calculatePlayerMoney(playerid);
			 ToplistManager.getInstance().updateToplist(playerid,player.getPlayername(),money);			
				//System.out.println("排行榜金钱更新:"+playerid+";money="+money);
			return true;
		}
		
		return false;
	}
	
	public void setSavingService(SavingService savingService) {
		this.savingService = savingService;
	}

	public float calculatePlayerMoney(int playerId){
		float amount = 0;
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerId);
		if (savings!=null){
			for (int i=0;i<savings.size();i++){
				amount += savings.get(i).getAmount();
			}			
		}
		List<Insure> insures = InsureManager.getInstance().getInsureList(playerId);
		if (insures!=null){
		for (int i=0;i<insures.size();i++){
			amount += insures.get(i).getAmount();
		}
		}
		List<Stock> stocks = StockManager.getInstance().getStockList(playerId);
		if (stocks!=null){
			for (int i=0;i<stocks.size();i++){
				Stock ps = stocks.get(i);
				if (ps==null) continue;
				List<Quote> qq = StockManager.getInstance().getLastQuotes(ps.getItemid());
				if (qq.size()>0)
					amount += qq.get(0).getPrice()*ps.getQty();
			}   			
		}
		
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
       System.out.println("剩余时间: "+diffdd+",周期时间:"+periodMinutes);
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
			return null;
		}
		
		boolean exec = false;
		Saving newsaving = new Saving();
		newsaving.setItemid(saving.getItemid());
		newsaving.setPlayerid(saving.getPlayerid());
		//取出存款:
		if (saving.getAmount()<0){
			float inAmount = 0 - saving.getAmount();
			Saving saving2 = SavingManager.getInstance().getSaving(saving.getPlayerid(), saving.getItemid());
			if (saving2==null){
				super.writeMsg(RetMsg.MSG_NoSavingData);
				return null;
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
			exec = SavingManager.getInstance().deleteSaving(saving.getPlayerid(),saving);	
		}else {
			
			pushLive(saving.getPlayerid(), 0 - saving.getAmount());
			
			Saving savingCfg = SavingManager.getInstance().getSavingCfg(saving.getItemid());
			System.out.println("存钱:"+saving.getPlayerid()+":itemid="+saving.getItemid());
			saving.setName(savingCfg.getName());
			saving.setCreatetime(new Date());
			saving.setUpdatetime(new Date());
			saving.setRate(savingCfg.getRate());
			saving.setQty(1);
			saving.setType(savingCfg.getType());
			saving.setPeriod(savingCfg.getPeriod());
			exec = SavingManager.getInstance().addSaving(saving.getPlayerid(), saving);
		}
		
		if (exec==false){
			pushLive(saving.getPlayerid(), saving.getAmount() );
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
		}else {
			JSONObject obj = JSONObject.fromObject(newsaving);
			super.writeMsg2(RetMsg.MSG_OK,obj.toString());
		}
		return null;
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

	public SavingAction(){
		init("savingService","playerService",
				"stockService");
	}
	
}
