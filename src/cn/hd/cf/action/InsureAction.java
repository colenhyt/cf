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
//	private Insure		insure;
//
//	/**
//	 * 获取insure参数
//	 * @return insure
//	 * */
//	public Insure getInsure() {
//		return insure;
//	}
//
//	/**
//	 * 设置insure参数
//	 * @param insure 对象
//	 * @return 无
//	 * */
//	public void setInsure(Insure insure) {
//		this.insure = insure;
//	}

	/**
	 * 保险产品购买
	 * @param insure 对象
	 * @return 购买inusre json数据
	 * */
	public synchronized String add(long sessionid,Insure insure)
	{
		Saving liveSaving = SavingManager.getInstance().getSaving(insure.getPlayerid(), 1);
		if (liveSaving.getAmount()<insure.getAmount())
			return msgStr2(RetMsg.MSG_MoneyNotEnough,String.valueOf(sessionid));
		
		float changeAmount = 0 - insure.getAmount();
		
		Date d1 = new Date(System.currentTimeMillis());
		insure.setCreatetime(d1);
		insure.setUpdatetime(d1);
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
				
				LogMgr.getInstance().log(insure.getPlayerid()," insure quest prize 5000,type:"+doType);
			}
			
			liveSaving.setAmount(liveSaving.getAmount()+changeAmount);
			insure.setLiveamount(liveSaving.getAmount());
			insure.setSessionid(sessionid);
			String str = JSON.toJSONString(insure);
			LogMgr.getInstance().log(insure.getPlayerid()," add insure itemid="+insure.getItemid()+",str"+str);
			SavingManager.getInstance().updateLiveSaving(insure.getPlayerid(),liveSaving);	
			return msgStr2(RetMsg.MSG_OK,str);
		}else {
			LogMgr.getInstance().log(insure.getPlayerid(),", warn, insure error: "+ret);
			return msgStr2(ret,String.valueOf(sessionid));
		}

	}
}
