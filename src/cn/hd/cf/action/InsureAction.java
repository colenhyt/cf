package cn.hd.cf.action;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.hd.cf.model.Insure;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.tools.InsuredataService;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.InsureManager;

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
			Insure incfg = InsureManager.getInstance().getInsureCfg(insure.getItemid());
			insure.setPeriod(incfg.getPeriod()*insure.getQty());
			insure.setType(incfg.getType());
			boolean add = InsureManager.getInstance().addInsure(insure.getPlayerid(), insure);	
			if (add==false){
				//钱放回去:
				super.pushLive(insure.getPlayerid(),  insure.getAmount());
				ret = RetMsg.MSG_SQLExecuteError;
			}
			super.playerTopUpdate(insure.getPlayerid());
		}else {
			System.out.println("insure没找到存款吗:"+ret);
		}
		writeMsg(ret);
		return null;
	}
	
	public String get()
	{
		Map<Integer,Insure> insures = findUpdatedInsures(insure.getPlayerid());
		String strInsure = JSON.toJSONString(insures);
		write(strInsure,"utf-8");
		//if (insures.size()<=0)
		return null;
	}
	
	public static void main(String[] args)
	{
		InsureAction aa = new InsureAction();
		Insure in = new Insure();
		in.setPlayerid(215);
		aa.setInsure(in);
		for (int i=0;i<200;i++){
			aa.get();
			
		}
		System.out.println("print done");
	}
}
