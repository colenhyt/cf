package cn.hd.cf.action;

import java.util.Date;

import cn.hd.base.BaseAction;
import cn.hd.cf.model.Insure;
import cn.hd.cf.service.InsureService;

public class InsureAction extends SavingAction {
	private Insure		insure;
	public Insure getInsure() {
		return insure;
	}

	public void setInsure(Insure insure) {
		this.insure = insure;
	}

	private InsureService insureService;
	
	public InsureService getInsureService() {
		return insureService;
	}

	public String add()
	{
		float inAmount = 0 - insure.getAmount();
		//先扣钱:
		int ret = super.updateLiveSaving(insure.getPlayerid(), inAmount);
		if (ret==0){
			insure.setCreatetime(new Date());
			boolean add = insureService.add(insure);	
			if (add==false){
				//钱放回去:
				super.updateLiveSaving(insure.getPlayerid(),  insure.getAmount());
				ret = RetMsg.MSG_SQLExecuteError;
			}
		}
		writeMsg(ret);
		return null;
	}
	
	public String update()
	{
		insureService.update(insure);		
		return null;
	}
	
	public void setInsureService(InsureService insureService) {
		this.insureService = insureService;
	}

	public InsureAction(){
		init("insureService");
	}
	
}
