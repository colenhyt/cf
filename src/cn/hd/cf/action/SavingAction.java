package cn.hd.cf.action;

import java.util.Date;
import java.util.List;

import cn.hd.base.BaseAction;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.SavingdataService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;
import cn.hd.mgr.PlayerManager;
import cn.hd.mgr.StockManager;

public class SavingAction extends BaseAction {
	public Saving		saving;
	private SavingdataService savingdataService;
	public SavingdataService getSavingdataService() {
		return savingdataService;
	}

	public void setSavingdataService(SavingdataService savingdataService) {
		this.savingdataService = savingdataService;
	}

	public Saving getSaving() {
		return saving;
	}

	public void setSaving(Saving saving) {
		this.saving = saving;
	}

	protected SavingService savingService;
	protected InsureService insureService;
	protected StockService stockService;
	protected ToplistService toplistService;
	
	public ToplistService getToplistService() {
		return toplistService;
	}

	public void setToplistService(ToplistService toplistService) {
		this.toplistService = toplistService;
	}

	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	public InsureService getInsureService() {
		return insureService;
	}

	public void setInsureService(InsureService insureService) {
		this.insureService = insureService;
	}

	public SavingService getSavingService() {
		return savingService;
	}

	public String add()
	{
		System.out.println("取钱:11");
		if (saving.getItemid()==1){
			return null;
		}
		float inAmount = 0 - saving.getAmount();
		System.out.println("取钱:22");
		//先设活期:
		int ret = pushLive(saving.getPlayerid(), inAmount);
		if (ret!=RetMsg.MSG_OK){
			super.writeMsg(ret);
			return null;
		}
		System.out.println("取钱:33");
		boolean exec = false;
		//取钱:
		if (saving.getAmount()<0){
			System.out.println("取钱:"+saving.getPlayerid()+":itemid="+saving.getItemid());
			exec = savingService.remove(saving);	
		}else {
			Saving savingCfg = savingdataService.findSaving(saving.getItemid());
			System.out.println("存钱:"+saving.getPlayerid()+":itemid="+saving.getItemid());
			saving.setName(savingCfg.getName());
			saving.setCreatetime(new Date());
			saving.setUpdatetime(new Date());
			saving.setRate(savingCfg.getRate());
			saving.setQty(1);
			saving.setType(savingCfg.getType());
			saving.setPeriod(savingCfg.getPeriod());
			exec = savingService.add(saving);		
		}
		
		if (exec==false){
			pushLive(saving.getPlayerid(),  saving.getAmount());
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
		}else {
			super.writeMsg(RetMsg.MSG_OK);
		}
		return null;
	}
	
	//更新活期存款金钱:
	public String updatelive()
	{
		boolean update = playerMoneyUpdate(saving);	
		if (update==false){
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
		}else
			super.writeMsg(RetMsg.MSG_OK);
		return null;
	}
	
	public int pushLive(int playerId,float amount)
	{
		Saving saving2 = savingService.findLivingSavingByPlayerId(playerId);
		if (saving2!=null){
			float newAmount = saving2.getAmount()+amount;
			if (newAmount<0)
				return RetMsg.MSG_MoneyNotEnough;
			
			saving2.setAmount(saving2.getAmount()+amount);
			playerMoneyUpdate(saving2);		
			return RetMsg.MSG_OK;
		}else {
			return RetMsg.MSG_NoSavingData;
		}
	}	
	
	protected boolean playerMoneyUpdate(Saving saving){
		saving.setUpdatetime(new Date());	
		boolean u = savingService.updateLive(saving);	
		System.out.println("活期存款更新:"+saving.getPlayerid()+";value="+saving.getAmount());
		 playerTopUpdate(saving.getPlayerid());
		//更新排行榜金钱:
		
		return u;
	}
	
	protected boolean playerTopUpdate(int playerid){
		PlayerWithBLOBs player = PlayerManager.getInstance().findPlayer(playerid);
		if (player!=null){
			float money = calculatePlayerMoney(playerid);
			 toplistService.updateToplist(playerid,player.getPlayername(),money);			
				System.out.println("排行榜金钱更新:"+playerid+";money="+money);
			return true;
		}
		
		return false;
	}
	
	public void setSavingService(SavingService savingService) {
		this.savingService = savingService;
	}

	public float calculatePlayerMoney(int playerId){
		float amount = 0;
		List<Saving> saving = savingService.findByPlayerId(playerId);
		for (int i=0;i<saving.size();i++){
			amount += saving.get(i).getAmount();
		}
		List<Insure> insure = insureService.findByPlayerId(playerId);
		for (int i=0;i<insure.size();i++){
			amount += insure.get(i).getAmount();
		}
		List<Stock> stock = stockService.findByPlayerId(playerId);
		for (int i=0;i<stock.size();i++){
			Stock ps = stock.get(i);
			if (ps==null) continue;
			List<Quote> qq = StockManager.getInstance().getLastQuotes(ps.getItemid());
			if (qq.size()>0)
				amount += qq.get(0).getPrice()*ps.getQty();
		}   	
		
		return amount;
	}

	public SavingAction(){
		init("savingService","savingdataService",
				"insureService","stockService","toplistService");
	}
	
}
