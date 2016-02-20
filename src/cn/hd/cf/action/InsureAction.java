package cn.hd.cf.action;

import java.util.Date;

import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Saving;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.InsureManager;
import cn.hd.mgr.LogMgr;
import cn.hd.mgr.SavingManager;

import com.alibaba.fastjson.JSON;

public class InsureAction extends SavingAction {
	private Insure		insure;
	public Insure getInsure() {
		return insure;
	}

	public void setInsure(Insure insure) {
		this.insure = insure;
	}

	public synchronized String add()
	{
		Saving liveSaving = SavingManager.getInstance().getSaving(insure.getPlayerid(), 1);
		if (liveSaving.getAmount()<insure.getAmount())
			return msgStr(RetMsg.MSG_MoneyNotEnough);
		
		float changeAmount = 0 - insure.getAmount();
		
		insure.setCreatetime(new Date());
		insure.setUpdatetime(new Date());
		Insure incfg = InsureManager.getInstance().getInsureCfg(insure.getItemid());
		insure.setPeriod(incfg.getPeriod()*insure.getQty());
		insure.setType(incfg.getType());
		int ret = InsureManager.getInstance().addInsure(insure.getPlayerid(), insure);	
		if (ret==RetMsg.MSG_OK){
			int doType = 2;
			if (incfg.getType()==1)
				doType = 3;
			boolean doneQuest = DataManager.getInstance().doneQuest(insure.getPlayerid(), doType);
			if (doneQuest){
				changeAmount += 5000;
				
				LogMgr.getInstance().log("pid:"+insure.getPlayerid()+" insure quest prize 5000,type:"+doType);
			}
			
			liveSaving.setAmount(liveSaving.getAmount()+changeAmount);
			insure.setLiveamount(liveSaving.getAmount());
			String str = JSON.toJSONString(insure);
			LogMgr.getInstance().log("pid:"+insure.getPlayerid()+" add insure itemid="+insure.getItemid()+",str"+str);
			playerMoneyUpdate(liveSaving);	
			return msgStr2(RetMsg.MSG_OK,str);
		}else {
			LogMgr.getInstance().log("pid:"+insure.getPlayerid()+", warn, insure error: "+ret);
			return msgStr(ret);
		}

	}
	
	public static void main(String[] args)
	{
		InsureAction aa = new InsureAction();
		Insure in = new Insure();
		in.setPlayerid(215);
		aa.setInsure(in);
		System.out.println("print done");
	}
}
