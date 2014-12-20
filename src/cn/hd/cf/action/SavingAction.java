package cn.hd.cf.action;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.SavingService;

public class SavingAction extends BaseAction {
	private Saving		saving;
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
		savingService.update(saving);		
		return null;
	}
	
	public void setSavingService(SavingService savingService) {
		this.savingService = savingService;
	}

	public SavingAction(){
		init("savingService");
	}
	
}
