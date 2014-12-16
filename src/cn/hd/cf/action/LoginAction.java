package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Signindata;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SignindataService;
import cn.hd.cf.service.ToplistService;
import cn.hd.mgr.EventManager;
import cn.hd.util.MD5;
import cn.hd.util.StringUtil;

public class LoginAction extends BaseAction {
	private PlayerWithBLOBs player;
	private PlayerService playerService;
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
		init("playerService","signindataService","toplistService");
		EventManager.getInstance().start();
	}
	
	public String register(){
//		if (player.getPlayername().length()<=0)
//		{
//			return null;
//		}
		boolean bExist = playerService.have(player.getPlayername());
		if (bExist)
		{
			System.out.println("account exist :name:"+player.getPlayername());
			Message msg = new Message();
			msg.setCode(1);		//重名
			JSONObject obj = JSONObject.fromObject(msg);
			write(obj.toString(),"utf-8");
			return null;
		}
		String ipAddr = getHttpRequest().getRemoteAddr();
		PlayerWithBLOBs playerBlob = new PlayerWithBLOBs();
		playerBlob.setAccountid(player.getAccountid());
		playerBlob.setPlayername(player.getPlayername());
		Date time = new Date(); 
		playerBlob.setCreatetime(time);
		String pwd = StringUtil.getRandomString(10);
		playerBlob.setPwd(MD5.MD5(pwd));
		int ret = playerService.add(playerBlob);
		if (ret==0){
			System.out.println("register playerid "+playerBlob.getPlayerid());
			JSONObject obj = JSONObject.fromObject(playerBlob);
			write(obj.toString(),"utf-8");
		}
		return null;
	}
	
	public String login()
	{
		PlayerWithBLOBs playerBlob = playerService.find(player.getPlayerid(),player.getPwd());
		if (playerBlob==null)
		{
			System.out.println("no player found:playerid:"+player.getPlayerid());
			return null;
		}
		System.out.println("player "+player.getPlayerid()+" login success");
		//List<Integer> dataIds = findUpdateDataIds(player.getVersions());
		//取需要更新的模块id
		JSONObject obj = JSONObject.fromObject(playerBlob);
		write(obj.toString(),"utf-8");
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
		System.out.println("player "+player.getPlayerid()+" found");
		//List<Integer> dataIds = findUpdateDataIds(player.getVersions());
		//取需要更新的模块id
		JSONObject obj = JSONObject.fromObject(playerBlob);
		write(obj.toString(),"utf-8");
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
		int ret = playerService.updateByKey(playerBlob);
		Toplist toplist = toplistService.findByPlayerId(playerBlob.getPlayerid());
		if (toplist==null){
			toplist = toplistService.findByLessMoney(0);
			int topCount = toplistService.findCount();
			if (toplist!=null||topCount<10){
				Toplist newtop = new Toplist();
				newtop.setPlayerid(playerBlob.getPlayerid());
				newtop.setPlayername(playerBlob.getPlayername());
				newtop.setMoney(Float.valueOf(0));
				newtop.setZan(0);
				toplistService.add(newtop);				
			}
//			if (toplist!=null&&topCount>10){
//				toplistService.remove(toplist.getId());
//			}
		}else {
			float topMoney = toplist.getMoney().floatValue();
			if (0>topMoney){
				toplist.setMoney(Float.valueOf(0));
				toplistService.updateByKey(toplist);
			}
		}
		//System.out.println("update player("+playerBlob.getPlayername()+"):ret: "+ret);
		Message msg = new Message();
		msg.setCode(ret);
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString(),"utf-8");
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
