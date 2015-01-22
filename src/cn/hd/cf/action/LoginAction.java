package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Signindata;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.InitdataService;
import cn.hd.cf.service.InsuredataService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingdataService;
import cn.hd.cf.service.SignindataService;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.EventManager;
import cn.hd.mgr.PlayerManager;
import cn.hd.mgr.StockManager;
import cn.hd.util.MD5;
import cn.hd.util.StringUtil;

import com.alibaba.fastjson.JSON;

public class LoginAction extends SavingAction {
	private PlayerWithBLOBs player;
	private PlayerService playerService;
	
	private SavingdataService savingdataService;
	private InsuredataService insuredataService;
	public InsuredataService getInsuredataService() {
		return insuredataService;
	}

	public void setInsuredataService(InsuredataService insuredataService) {
		this.insuredataService = insuredataService;
	}

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
		init("playerService","signindataService","initdataService"
				,"insuredataService","savingdataService");
		EventManager.getInstance().start();
	}
	
	public String connect(){
		Message msg = new Message();
		msg.setCode(0);
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString(),"utf-8");
		return null;
	}
	
	public String register(){
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
		playerBlob.setAccountid(player.getAccountid());
		playerBlob.setPlayername(player.getPlayername());
		playerBlob.setCreatetime(time);
		String pwd = StringUtil.getRandomString(10);
		playerBlob.setPwd(MD5.MD5(pwd));
		boolean ret = playerService.add(playerBlob);
		if (ret==false){
			super.writeMsg(RetMsg.MSG_SQLExecuteError);
			return null;
		}
		PlayerManager.getInstance().addPlayer(playerBlob);
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
		
		JSONObject obj = JSONObject.fromObject(playerBlob);
		System.out.println("register player: "+obj.toString());
		write(obj.toString(),"utf-8");
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
			System.out.println("取得排行榜金钱:"+fMm);
			playerBlob.setZan(toplist.getZan());
		}
		int top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),0,fMm);
		playerBlob.setWeektop(top+1);
		top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),1,fMm);
		playerBlob.setMonthtop(top+1);
		float margin = StockManager.getInstance().getMarginSec();
		playerBlob.setQuotetime(margin);
		playerBlob.setLastlogin(new Date());
		//List<Integer> dataIds = findUpdateDataIds(player.getVersions());
		//取需要更新的模块id
		JSONObject obj = JSONObject.fromObject(playerBlob);	
		
		return obj.toString();
	}
	
	
	private Map<Integer,Insure> findUpdatedInsures(int playerId)
	{
		Date curr = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(curr);
	    Calendar c2 = Calendar.getInstance(); 
		
		List<Insure> insures = insureService.findByPlayerId(playerId);
		
		Map<Integer,Insure>	mdata = new HashMap<Integer,Insure>();
		
		try {		
		for (int i=0;i<insures.size();i++){
			Insure insure = insures.get(i);
			float inter = 0;	// 0表明未到期
	        c2.setTime(insure.getUpdatetime());
			float diffdd = super.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
			float periodMinutes = insure.getPeriod()*60;
			periodMinutes = 5;
			//到期:
			if ((diffdd-periodMinutes)>0.001)
			{
				//理财产品,得到收益:
				if (insure.getType()!=null&&insure.getType()==1){
				
					Insure incfg = insuredataService.findInsure(insure.getItemid());
					inter = incfg.getProfit()*insure.getQty();
					super.pushLive(playerId, insure.getAmount()+inter);
				}else {		//保险到期，移除
					inter = -1;
				}
				//删除产品
				insureService.delete(insure);
			}
			
			Insure uinsure = new Insure();
			uinsure.setAmount(insure.getAmount());
			uinsure.setCreatetime(insure.getCreatetime());
			uinsure.setQty(insure.getQty());
			uinsure.setProfit(inter);
			mdata.put(insure.getItemid(), uinsure);
		}
		}catch (Exception e){
			e.printStackTrace();
			return mdata;
		}	
		
		return mdata;
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
			if (saving.getType()==0)
				liveSaving = saving;
			float inter = 0;
	        c2.setTime(saving.getUpdatetime());
	        float diffdd = super.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
	        float periodMinutes = saving.getPeriod()*60;
	        periodMinutes = 5;
			//System.out.println("利息到期时间:"+diffdd+","+periodMinutes);
			//if ((diffdd-periodMinutes)>0.001)
			{
				liveUpdate = true;
				//活期
				if (saving.getType()==0)
				{
					long diff = (long)(diffdd/periodMinutes);
					inter = diff * saving.getAmount()*saving.getRate()/100;
					saving.setAmount(saving.getAmount()+inter);
				}else //定期，取出来,跟利息一起放回到活期
				{
					inter = saving.getAmount()*saving.getRate()/100;
					float newsaving = saving.getAmount()+inter;
					if (liveSaving!=null){
						liveSaving.setAmount(liveSaving.getAmount()+newsaving);
						savingService.remove(saving);
						Saving ll = mdata.get(liveSaving.getItemid());
						if (ll!=null)
							ll.setAmount(liveSaving.getAmount());
					}
				}
				System.out.println(saving.getItemid()+"存款到期, 得到利息: "+inter);
			}
			
			Saving usaving = new Saving();
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

	public String login()
	{
		PlayerWithBLOBs playerBlob = playerService.find(player.getPlayerid(),player.getPwd());
		if (playerBlob==null)
		{
			System.out.println("no player found:playerid:"+player.getPwd());
			return null;
		}
	
		String pdata = getPlayerJsonData(playerBlob);
	
		PlayerWithBLOBs p2 = new PlayerWithBLOBs();
		p2.setPlayerid(player.getPlayerid());
		p2.setLastlogin(playerBlob.getLastlogin());
		playerService.updateByKey(p2);
			
		write(pdata,"utf-8");
		
		System.out.println("player("+pdata+") login success");
		return null;
	}
	
	public String get()
	{
		PlayerWithBLOBs playerBlob = playerService.findByPlayerId(player.getPlayerid());
		if (playerBlob==null)
		{
			System.out.println("no player found:playerid:"+player.getPlayerid());
			return null;
		}
		String pdata = getPlayerJsonData(playerBlob);
		write(pdata,"utf-8");
		System.out.println("player("+pdata+") found");
		return null;
	}
	
	public String update()
	{
		String pp = getHttpRequest().getParameter("player");
		JSONObject ppObj = JSONObject.fromObject(pp);
		PlayerWithBLOBs playerBlob = (PlayerWithBLOBs)JSONObject.toBean(ppObj,PlayerWithBLOBs.class);
		if (playerBlob==null)
		{
			System.out.println("no player found:playerid:"+player.getPlayerid());
			return null;
		}
		boolean ret = playerService.updateByKey(playerBlob);
		
		System.out.println("update player("+ppObj.toString()+"):ret: "+ret);
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
	public PlayerService getPlayerService() {
		return playerService;
	}
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}
}
