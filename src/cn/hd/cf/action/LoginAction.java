package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.hd.base.Base;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Signindata;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.ToplistService;
import cn.hd.cf.tools.InitdataService;
import cn.hd.cf.tools.SavingdataService;
import cn.hd.cf.tools.SignindataService;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.StockManager;
import cn.hd.util.MD5;
import cn.hd.util.StringUtil;

import com.alibaba.fastjson.JSON;

public class LoginAction extends SavingAction {
	private PlayerWithBLOBs player;
	
	private SavingdataService savingdataService;


	public SavingdataService getSavingdataService() {
		return savingdataService;
	}

	public void setSavingdataService(SavingdataService savingdataService) {
		this.savingdataService = savingdataService;
	}
	private InitdataService initdataService;
	public InitdataService getInitdataService() {
		return initdataService;
	}

	public void setInitdataService(InitdataService initdataService) {
		this.initdataService = initdataService;
	}

	private SignindataService signindataService;
	
	public SignindataService getSignindataService() {
		return signindataService;
	}

	public void setSignindataService(SignindataService signindataService) {
		this.signindataService = signindataService;
	}

	public LoginAction(){
		init("signindataService","initdataService"
				,"savingdataService");
	}
	
	public String connect(){
		Message msg = new Message();
		msg.setCode(0);
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString(),"utf-8");
		return null;
	}
	
	public String register(){
		//System.out.println("玩家注册:"+player.getPlayername());
		boolean bExist = playerService.have(player.getPlayername());
		if (bExist)
		{
			System.out.println("account exist :name:"+player.getPlayername());
			Message msg = new Message();
			msg.setCode(RetMsg.MSG_PlayerNameIsExist);		//重名
			JSONObject obj = JSONObject.fromObject(msg);
			write(obj.toString(),"utf-8");
			return null;
		}
		PlayerWithBLOBs playerBlob = new PlayerWithBLOBs();
		Date time = new Date(); 
		
		//注册奖励:
		Init init = initdataService.findInit();
		if (init!=null){
			playerBlob.setExp(init.getExp());
		}
		
//		String ipAddr = getHttpRequest().getRemoteAddr();
		playerBlob.setAccountid(1);
		playerBlob.setPlayername(player.getPlayername());
		playerBlob.setSex(player.getSex());
		playerBlob.setCreatetime(time);
		String pwd = StringUtil.getRandomString(10);
		playerBlob.setPwd(MD5.MD5(pwd));
		playerBlob.setPlayerid(DataManager.getInstance().assignNextId());
		boolean ret = playerService.add(playerBlob);
		if (ret==false){
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
			return null;
		}
		//活期存款:
		if (init!=null&&init.getMoney()>0){
			Saving savingCfg = savingdataService.findSaving(1);
			Saving saving = new Saving();
			saving.setName(savingCfg.getName());
			saving.setPeriod(savingCfg.getPeriod());
			saving.setRate(savingCfg.getRate());
			saving.setItemid(savingCfg.getId());
			saving.setQty(1);
			saving.setUpdatetime(time);
			saving.setType(savingCfg.getType());
			saving.setPlayerid(playerBlob.getPlayerid());
			saving.setAmount(Float.valueOf(init.getMoney().intValue()));
			saving.setCreatetime(time);
			savingService.add(saving);
			super.playerTopUpdate(playerBlob.getPlayerid());
		}
		
		String pdata = getPlayerJsonData(playerBlob);
		
		//System.out.println("register player: "+pdata);
		write(pdata,"utf-8");
		return null;
	}
	
	public String getPlayerJsonData(PlayerWithBLOBs playerBlob)
	{
		Map<Integer,Insure> insures = findUpdatedInsures(playerBlob.getPlayerid());
		playerBlob.setInsure(JSON.toJSONString(insures));		
		Map<Integer,Saving> savings = findUpdatedSavings(playerBlob.getPlayerid());
		playerBlob.setSaving(JSON.toJSONString(savings));
		Map<Integer,List<Stock>> stocks = stockService.findMapByPlayerId(playerBlob.getPlayerid());
		playerBlob.setStock(JSON.toJSONString(stocks));	
		Toplist toplist = toplistService.findByPlayerId(playerBlob.getPlayerid());
		float fMm = 0;
		if (toplist!=null){
			fMm = toplist.getMoney().floatValue();
			playerBlob.setZan(toplist.getZan());
		}

		int top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),0,fMm);
		playerBlob.setWeektop(top+1);
		top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),1,fMm);
		playerBlob.setMonthtop(top+1);
		
		float margin = StockManager.getInstance().getMarginSec();
		playerBlob.setQuotetime(margin);
		playerBlob.setLastlogin(new Date());
		//取需要更新的模块id
		JSONObject obj = JSONObject.fromObject(playerBlob);	
		return obj.toString();
	}
	
	public static void initToplist(PlayerWithBLOBs playerBlob,ToplistService toplistService)
	{
		Toplist toplist = toplistService.findByPlayerId(playerBlob.getPlayerid());
		float fMm = 0;
		if (toplist!=null){
			fMm = toplist.getMoney().floatValue();
			playerBlob.setZan(toplist.getZan());
		}
//		int top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),0,fMm);
//		playerBlob.setWeektop(top+1);
//		top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),1,fMm);
//		playerBlob.setMonthtop(top+1);	
	}
	
	//
	private Map<Integer,Saving> findUpdatedSavings(int playerId)
	{
		Date currDate = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(currDate);
	    Calendar c2 = Calendar.getInstance(); 
		List<Saving> savings = savingService.findByPlayerId(playerId);
		Map<Integer,Saving>	 mdata = new HashMap<Integer,Saving>();
		Saving liveSaving = null;
		boolean liveUpdate = false;
		for (int i=0;i<savings.size();i++){
			Saving saving = savings.get(i);
			float inter = 0;
	        c2.setTime(saving.getUpdatetime());
	        float diffdd = Base.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
	        float periodMinutes = saving.getPeriod()*60*24;//天:分钟
			if (saving.getType()==0)		//活期
			{
				liveSaving = saving;
				periodMinutes = 60*24;//天:分钟
				long diff = (long)(diffdd/periodMinutes);
				inter = diff * saving.getAmount()*saving.getRate()/100;
				if (inter>1){
					liveUpdate = true;
					saving.setAmount(saving.getAmount()+inter);					
				}else
					inter = 0;		//不算利息
			}else if (isSavingTimeout(saving))		//定期，看到期没有
			{
				liveUpdate = true;
				saving.setStatus((byte)1);
				savingService.update(saving);
				log.debug(saving.getItemid()+"存款到期, 得到利息: "+inter);
			}
			Saving usaving = new Saving();
			usaving.setId(saving.getId());
			usaving.setItemid(saving.getItemid());
			usaving.setAmount(saving.getAmount());
			usaving.setCreatetime(saving.getCreatetime());
			usaving.setQty(saving.getQty());
			usaving.setProfit(inter);
			mdata.put(saving.getItemid(), usaving);
		}
		if (liveUpdate==true){
			liveSaving.setUpdatetime(currDate);
			savingService.updateLive(liveSaving);
			super.playerTopUpdate(playerId);			
		}
		
		return mdata;
	}

	public String loginSimple(int playerid)
	{
		
		PlayerWithBLOBs playerBlob = playerService.find(playerid);
		if (playerBlob==null)
		{
			System.out.println("no player found:playerid:"+playerid);
			return null;
		}
		//System.out.println("player(");
		JSONObject obj = JSONObject.fromObject(playerBlob);	
		String pdata = obj.toString();
	
//		PlayerWithBLOBs p2 = new PlayerWithBLOBs();
//		p2.setPlayerid(playerid);
//		p2.setLastlogin(playerBlob.getLastlogin());
//		playerService.updateByKey(p2);
			
//		playerService.DBConnClose();
		//write(pdata,"utf-8");
		
		//System.out.println("player("+playerBlob.getPlayername()+") login success");
		return pdata;
	}
	
	public String login()
	{
		
		PlayerWithBLOBs playerBlob = playerService.find(player.getPlayerid());
		if (playerBlob==null)
		{
			System.out.println("no player found:playerid:"+player.getPlayerid());
			return null;
		}
		//System.out.println("player(");
		String pdata = getPlayerJsonData(playerBlob);
	
		PlayerWithBLOBs p2 = new PlayerWithBLOBs();
		p2.setPlayerid(player.getPlayerid());
		p2.setLastlogin(playerBlob.getLastlogin());
		playerService.updateByKey(p2);
			
//		playerService.DBConnClose();
		write(pdata,"utf-8");
		
		//System.out.println("player("+playerBlob.getPlayername()+") login success");
		return null;
	}
	
	public String get()
	{
		PlayerWithBLOBs playerBlob = playerService.findByPlayerId(player.getPlayerid());
		if (playerBlob==null)
		{
			log.debug("no player found:playerid:"+player.getPlayerid());
			return null;
		}
		String pdata = getPlayerJsonData(playerBlob);
		write(pdata,"utf-8");
		log.debug("player("+pdata+") found");
		return null;
	}
	
	public String update()
	{
		String pp = getHttpRequest().getParameter("player");
		JSONObject ppObj = JSONObject.fromObject(pp);
		PlayerWithBLOBs playerBlob = (PlayerWithBLOBs)JSONObject.toBean(ppObj,PlayerWithBLOBs.class);
		if (playerBlob==null)
		{
			log.debug("no player found:playerid:"+player.getPlayerid());
			return null;
		}
		boolean ret = playerService.updateByKey(playerBlob);
		
		log.debug("update player("+ppObj.toString()+"):ret: "+ret);
		writeMsg(RetMsg.MSG_OK);
		return null;
	}
	
	public List<Integer> findUpdateDataIds(String oldIds)
	{
		List<Integer> dataIds = new ArrayList<Integer>();
		String[] arrOldIds = oldIds.split(",");
		for (int i=0;i<arrOldIds.length;i+=2)
		{
			String id = arrOldIds[i];
			String ver = arrOldIds[i+1];
			int iId = Integer.valueOf(id).intValue();
			int iVer = Integer.valueOf(ver).intValue();
			Signindata data1 = signindataService.findActive();
			if (iId==MODAL_SIGNIN&&data1!=null&&iVer!=data1.getVersion().intValue())
			{
				dataIds.add(Integer.valueOf(id));
			}
		}
		return dataIds;
	}
	public PlayerWithBLOBs getPlayer() {
		return player;
	}
	public void setPlayer(PlayerWithBLOBs player) {
		this.player = player;
	}
}
