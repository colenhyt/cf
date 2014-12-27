package cn.hd.cf.action;

import java.util.Date;
import java.util.List;

import cn.hd.base.BaseAction;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.SavingdataService;
import cn.hd.mgr.PlayerManager;

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

	private SavingService savingService;
	
	public SavingService getSavingService() {
		return savingService;
	}

	public String add()
	{
		if (saving.getItemid()==1){
			return null;
		}
		float inAmount = 0 - saving.getAmount();
		//先设活期:
		int ret = updateLiveSaving(saving.getPlayerid(), inAmount);
		if (ret!=RetMsg.MSG_OK){
			super.writeMsg(ret);
			return null;
		}
		
		boolean exec = false;
		//取钱:
		if (saving.getAmount()<0){
			System.out.println("取钱:"+saving.getPlayerid()+":itemid="+saving.getItemid());
			exec = savingService.remove(saving.getPlayerid(),saving.getItemid());	
		}else {
			Saving savingCfg = savingdataService.findSaving(saving.getItemid());
			System.out.println("存钱:"+saving.getPlayerid()+":itemid="+saving.getItemid());
			saving.setName(savingCfg.getName());
			saving.setCreatetime(new Date());
			saving.setRate(savingCfg.getRate());
			saving.setType(savingCfg.getType());
			saving.setPeriod(savingCfg.getPeriod());
			exec = savingService.add(saving);		
		}
		
		if (exec==false){
			updateLiveSaving(saving.getPlayerid(),  saving.getAmount());
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
		}else {
			super.writeMsg(RetMsg.MSG_OK);
		}
		return null;
	}
	
	public String update()
	{
		boolean update = savingService.update(saving);	
		if (update==false){
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
		}
		return null;
	}
	
	public int updateLiveSaving(int playerId,float amount)
	{
		Saving saving2 = savingService.findLivingSavingByPlayerId(playerId);
		if (saving2!=null){
			float newAmount = saving2.getAmount()+amount;
			if (newAmount<0)
				return RetMsg.MSG_MoneyNotEnough;
			
			saving2.setAmount(saving2.getAmount()+amount);
			savingService.update(saving2);		
			return RetMsg.MSG_OK;
		}else {
			return RetMsg.MSG_NoSavingData;
		}
	}	
	
	public void setSavingService(SavingService savingService) {
		this.savingService = savingService;
	}

	public SavingAction(){
		init("savingService","savingdataService");
	}
	
}
