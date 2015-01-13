package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Signindata;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.InitdataService;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.SavingdataService;
import cn.hd.cf.service.SignindataService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;
import cn.hd.mgr.EventManager;
import cn.hd.mgr.StockManager;
import cn.hd.util.MD5;
import cn.hd.util.StringUtil;

import com.alibaba.fastjson.JSON;

public class LoginAction extends BaseAction {
	private PlayerWithBLOBs player;
	private PlayerService playerService;
	private SavingService savingService;
	private InsureService insureService;
	public InsureService getInsureService() {
		return insureService;
	}

	public void setInsureService(InsureService insureService) {
		this.insureService = insureService;
	}
	private StockService stockService;
	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}
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

	public SavingService getSavingService() {
		return savingService;
	}

	public void setSavingService(SavingService savingService) {
		this.savingService = savingService;
	}
	private SignindataService signindataService;
	private ToplistService	toplistService;
	
	public ToplistService getToplistService() {
		return toplistService;
	}

	public void setToplistService(ToplistService toplistService) {
		this.toplistService = toplistService;
	}

	public SignindataService getSignindataService() {
		return signindataService;
	}

	public void setSignindataService(SignindataService signindataService) {
		this.signindataService = signindataService;
	}

	public LoginAction(){
		init("playerService","signindataService","toplistService","savingService","initdataService"
				,"insureService","stockService","savingdataService");
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
		}
		
		JSONObject obj = JSONObject.fromObject(playerBlob);
		System.out.println("register player: "+obj.toString());
		write(obj.toString(),"utf-8");
		return null;
	}
	
	public String getPlayerJsonData(PlayerWithBLOBs playerBlob)
	{
		Map<Integer,Insure> insures = insureService.findUpdatedInsures(playerBlob.getPlayerid());
		playerBlob.setInsure(JSON.toJSONString(insures));			
		Map<Integer,Saving> savings = savingService.findUpdatedSavings(playerBlob.getPlayerid());
		playerBlob.setSaving(JSON.toJSONString(savings));
		Map<Integer,List<Stock>> stocks = stockService.findMapByPlayerId(playerBlob.getPlayerid());
		playerBlob.setStock(JSON.toJSONString(stocks));	
		Toplist toplist = toplistService.findByPlayerId(playerBlob.getPlayerid(),0);
		if (toplist!=null)
			playerBlob.setZan(toplist.getZan());
		
		Date now = new Date();
	    Calendar cl = Calendar. getInstance();
	    cl.setTime(now);	
	    int currWeek = cl.get(Calendar.WEEK_OF_YEAR);
		int top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),currWeek);
		playerBlob.setWeektop(top+1);
		top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),-1);
		playerBlob.setMonthtop(top+1);
		float margin = StockManager.getInstance().getMarginSec();
		System.out.println("价格跳动:"+margin);
		playerBlob.setQuotetime(margin);
		playerBlob.setLastlogin(new Date());
		//List<Integer> dataIds = findUpdateDataIds(player.getVersions());
		//取需要更新的模块id
		JSONObject obj = JSONObject.fromObject(playerBlob);	
		
		return obj.toString();
	}
	
	
	public String login()
	{
		PlayerWithBLOBs playerBlob = playerService.find(player.getPlayerid(),player.getPwd());
		if (playerBlob==null)
		{
			System.out.println("no player found:playerid:"+player.getPlayerid());
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
