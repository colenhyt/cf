package cn.hd.cf.action;

import java.util.List;

import cn.hd.base.BaseAction;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.SavingService;

public class SavingAction extends BaseAction {
	public Saving		saving;
	private SavingService savingService;
	
	public SavingService getSavingService() {
		return savingService;
	}

	public String add()
	{
		savingService.add(saving);		
		return null;
	}
	
	public String update()
	{
		boolean update = savingService.update(saving);	
		if (update==false){
			super.writeMsg(RetMsg.SQLExecuteError);
		}
		return null;
	}
	
	public int updateLiveSaving(int playerId,float amount)
	{
		Saving saving2 = savingService.findLivingSavingByPlayerId(playerId);
		if (saving2!=null){
			float newAmount = saving2.getAmount()+amount;
			if (newAmount<0)
				return RetMsg.MoneyNotEnough;
			
			saving2.setAmount(saving2.getAmount()+amount);
			savingService.update(saving2);		
			return RetMsg.OK;
		}else {
			return RetMsg.NoSavingData;
		}
	}	
	
	public void setSavingService(SavingService savingService) {
		this.savingService = savingService;
	}

	public SavingAction(){
		init("savingService");
	}
	
}
