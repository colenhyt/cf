package cn.hd.cf.action;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.hd.cf.model.Insure;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.tools.InsuredataService;
import cn.hd.mgr.DataManager;

public class InsureAction extends SavingAction {
	private Insure		insure;
	public Insure getInsure() {
		return insure;
	}

	public void setInsure(Insure insure) {
		this.insure = insure;
	}

	public String add()
	{
		float inAmount = 0 - insure.getAmount();
		//先扣钱:
		int ret = super.pushLive(insure.getPlayerid(), inAmount);
		if (ret==0){
			insure.setCreatetime(new Date());
			insure.setUpdatetime(new Date());
			Insure incfg = insuredataService.findInsure(insure.getItemid());
			insure.setPeriod(incfg.getPeriod()*insure.getQty());
			insure.setType(incfg.getType());
			boolean add = insureService.add(insure);	
			if (add==false){
				//钱放回去:
				super.pushLive(insure.getPlayerid(),  insure.getAmount());
				ret = RetMsg.MSG_SQLExecuteError;
			}
			super.playerTopUpdate(insure.getPlayerid());
		}
		writeMsg(ret);
		return null;
	}
	
	public String get()
	{
		Map<Integer,Insure> insures = findUpdatedInsures(insure.getPlayerid());
		String strInsure = JSON.toJSONString(insures);
		write(strInsure,"utf-8");
		System.out.println("find "+strInsure);
		return null;
	}
	
}
