package cn.hd.cf.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.base.Base;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.InsureManager;
import cn.hd.mgr.LogMgr;
import cn.hd.mgr.SavingManager;
import cn.hd.mgr.StockManager;
import cn.hd.mgr.ToplistManager;

import com.alibaba.fastjson.JSON;

public class SavingAction extends BaseAction {
//	public Saving		saving;
//	
//	public Saving getSaving() {
//		return saving;
//	}
//
//	public void setSaving(Saving saving) {
//		this.saving = saving;
//	}
	
	public boolean playerTopUpdate(int playerid){
		Player player = DataManager.getInstance().findPlayer(playerid);
		if (player!=null){
			float money = ToplistManager.getInstance().calculatePlayerMoney(playerid);
			 ToplistManager.getInstance().updateToplist(playerid,player.getPlayername(),money);			
				//System.out.println("排行榜金钱更新:"+playerid+";money="+money);
			return true;
		}
		
		return false;
	}


	/**
	 * 存款是否到期
	 * @param saving 对象
	 * @return boolean true为已到期，false为未到期
	 * */
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
//       System.out.println("剩余时间: "+diffdd+",周期时间:"+periodMinutes);
       if ((diffdd-periodMinutes)>0.001)		//定期到期
		{
			return true;
		}		
		return false;
	}
	
	/**
	 * 获得当前存款的活期利息
	 * @param saving 对象
	 * @return float 利息值
	 * */
	public float getLiveInter(Saving saving)
	{
		if (saving==null||saving.getUpdatetime()==null) return 0;
		
		Saving savingCfg = SavingManager.getInstance().getSavingCfg(1);
		 Calendar c2 = Calendar.getInstance(); 
        c2.setTime(saving.getUpdatetime());
        Date currDate = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(currDate);
	    
        float diffdd = Base.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
		float periodMinutes = 60*24;//天:分钟
		float diff = (diffdd/periodMinutes);
		float inter = diff * saving.getAmount()*savingCfg.getRate()/100;	
		return inter;
	}
	
	/**
	 * 增加/取出存款
	 * @param saving 对象
	 * @return 增加/取出 存款对象json数据
	 * */
	public String add(long sessionid, Saving saving2)
	{
		//活期不存在存款取款:
		if (saving2.getItemid()==1){
			return msgStr2(RetMsg.MSG_NoSavingData,String.valueOf(sessionid));
		}
		
		Saving newsaving = new Saving();
		newsaving.setItemid(saving2.getItemid());
		newsaving.setPlayerid(saving2.getPlayerid());
		newsaving.setAmount(saving2.getAmount());
		int ret = RetMsg.MSG_OK;
		
	
		List<Saving> list = SavingManager.getInstance().getSavingList(saving2.getPlayerid());
		Saving liveSaving = null;
		int liveIndex = 0;
    	for (int i=0;i<list.size();i++){
    		if (list.get(i).getItemid().intValue()==1){
    			liveSaving = list.get(i);
    			liveIndex = i;
    			break;
    		}
    	}		
    	
    	if (liveSaving==null){
    		return msgStr2(RetMsg.MSG_NoSavingData,String.valueOf(sessionid));
    	}
    	
		float changeAmount = 0 - saving2.getAmount();
		//取出存款:
		if (saving2.getAmount()<0){
			Saving saving3 = null;
			int itemIndex = -1;
			for (int i=0;i<list.size();i++){
				if (list.get(i).getItemid().intValue()==saving2.getItemid().intValue()){
					itemIndex = i;
					saving3 = list.get(i);
					break;
				}
			}			
			if (saving3==null){
				return msgStr2(RetMsg.MSG_SavingNotExist,String.valueOf(sessionid));
			}
			//已到期的存款:
			boolean isout = isSavingTimeout(saving3);
			LogMgr.getInstance().log(saving2.getPlayerid()," saving is out:"+isout);
			float inter  = 0;
			if ((saving2.getStatus()!=null&&saving2.getStatus()==1)||isout)		
			{
				inter = (saving3.getAmount()*saving3.getRate()/100);
			}else {			//按活期得到利息
				inter = getLiveInter(saving3);
			}
			if (Math.abs(inter-0.9)>0){
				BigDecimal b = new BigDecimal(inter);  
				int iInter = (int)b.setScale(0,BigDecimal.ROUND_HALF_UP).floatValue();
				changeAmount += iInter;	
				LogMgr.getInstance().log(saving2.getPlayerid()," get inter:"+iInter);
				newsaving.setProfit((float)iInter);				
			}
			list.remove(itemIndex);
			ret = RetMsg.MSG_OK;
		}else {
			if (liveSaving.getAmount()<saving2.getAmount())
				return msgStr2(RetMsg.MSG_MoneyNotEnough,String.valueOf(sessionid));
			
			boolean found = false;
			for (int i=0;i<list.size();i++){
				if (list.get(i).getItemid().intValue()==saving2.getItemid().intValue()){
					found = true;
					break;
				}
			}			
			if (found){
				return msgStr2(RetMsg.MSG_SavingIsExist,String.valueOf(sessionid));
			}
			Saving savingCfg = SavingManager.getInstance().getSavingCfg(saving2.getItemid());
			saving2.setName(savingCfg.getName());
			Date d = new Date();
			saving2.setCreatetime(d);
			saving2.setUpdatetime(d);
			saving2.setRate(savingCfg.getRate());
			saving2.setQty(1);
			saving2.setType(savingCfg.getType());
			saving2.setPeriod(savingCfg.getPeriod());
			list.add(saving2);
			ret = RetMsg.MSG_OK;
			boolean doneQuest = DataManager.getInstance().doneQuest(saving2.getPlayerid(), 1);
			if (doneQuest){
				changeAmount += 5000;
				LogMgr.getInstance().log(saving2.getPlayerid()," saving quest prize 5000");
			}
		}
		
		//修改活期金额:
		if (ret==RetMsg.MSG_OK){
			liveSaving.setAmount(liveSaving.getAmount()+changeAmount);
			newsaving.setLiveamount(liveSaving.getAmount());
			newsaving.setSessionid(sessionid);
			list.get(liveIndex).setAmount(liveSaving.getAmount());
			String str = JSON.toJSONString(newsaving);
			LogMgr.getInstance().log(saving2.getPlayerid()," add/remove saving itemid="+saving2.getItemid()+",ret:"+ret+",amount:"+saving2.getAmount()+", retdata:"+str);
			SavingManager.getInstance().updateSavings(saving2.getPlayerid(), list);
			return msgStr2(RetMsg.MSG_OK,str);
		}else {
			LogMgr.getInstance().log(saving2.getPlayerid(),",error,saving: "+ret);
			return msgStr2(ret,String.valueOf(sessionid));
		}
	}

	public float findUpdatedInsures(int playerId,Saving liveSaving,Map<Integer,Insure> mdata)
		{
			List<Insure> insures = InsureManager.getInstance().getInsureList(playerId);
//			System.out.println("找到保险个数:"+playerId+" from db:"+insures.size()+",session:"+MybatisSessionFactory.getSession().toString());
			if (insures==null)
				return 0;
			
			float insureamount = 0;
			Date curr = new Date();
		    Calendar cCurr = Calendar.getInstance(); 
		    cCurr.setTime(curr);
		    Calendar c2 = Calendar.getInstance(); 
			
		    List<Insure> notTimeoutInsures = new ArrayList<Insure>();
			try {		
			for (Insure insure:insures){
				if (insure.getUpdatetime()==null) continue;
				float inter = 0;	// 0表明未到期
		        c2.setTime(insure.getUpdatetime());
				float diffdd = Base.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
				Insure incfg = InsureManager.getInstance().getInsureCfg(insure.getItemid());
				float periodMinutes = incfg.getPeriod()*60; //天:分钟
//				System.out.println("(peroid:"+insure.getPeriod()+") diffdd:"+diffdd+",periodMinutes:"+periodMinutes);
	//			periodMinutes = 5;
				//到期:
				if ((diffdd-periodMinutes)>0.001)
				{
					//理财产品,得到收益:
					if (insure.getType()!=null&&insure.getType()==1){
					
						float oriAmount = insure.getAmount();
						inter = incfg.getProfit()*insure.getQty();
						liveSaving.setAmount(liveSaving.getAmount()+oriAmount+inter);
					}else {		//保险到期，移除
						inter = -1;
					}
					LogMgr.getInstance().log(playerId," insure timeout:"+JSON.toJSONString(insure));
//					System.out.println("insure overdate:"+insure.getItemid());
				}else {
					insureamount += insure.getAmount();
					notTimeoutInsures.add(insure);
				}
				
				Insure uinsure = new Insure();
				uinsure.setAmount(insure.getAmount());
				uinsure.setCreatetime(insure.getCreatetime());
				uinsure.setQty(insure.getQty());
				uinsure.setProfit(inter);
				mdata.put(insure.getItemid(), uinsure);
			}
			//有到期，更新未过期的
			if (notTimeoutInsures.size()!=insures.size()){
				InsureManager.getInstance().updateInsures(playerId, notTimeoutInsures);
			}
			}catch (Exception e){
				e.printStackTrace();
			}	
			return insureamount;
		}

}
