package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.freeteam.base.BaseAction;
import cn.freeteam.util.MD5;
import cn.freeteam.util.StringUtil;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Signindata;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SignindataService;

public class LoginAction extends BaseAction {
	private Player player;
	private PlayerService playerService;
	private SignindataService signindataService;
	
	public SignindataService getSignindataService() {
		return signindataService;
	}

	public void setSignindataService(SignindataService signindataService) {
		this.signindataService = signindataService;
	}

	public LoginAction(){
		init("playerService","signindataService");
	}
	
	public String register(){
		if (player.getPlayername().length()<=0)
		{
			return null;
		}
		boolean bExist = playerService.have(player.getAccountid());
		if (bExist)
		{
			System.out.println("account exist :"+player.getAccountid());
			return null;
		}
		PlayerWithBLOBs playerBlob = new PlayerWithBLOBs();
		playerBlob.setAccountid(player.getAccountid());
		Date time = new Date(); 
		playerBlob.setCreatetime(time);
		playerBlob.setPlayername(player.getPlayername());
		String pwd = StringUtil.getRandomString(10);
		playerBlob.setPwd(MD5.MD5(pwd));
		playerService.add(playerBlob);
		return null;
	}
	
	public String login()
	{
		PlayerWithBLOBs tplayer = playerService.find(player.getPlayerid(),MD5.MD5(player.getPwd()));
		if (tplayer!=null)
		{
			List<Integer> dataIds = findUpdateDataIds(player.getVersions());
			//取需要更新的模块id
		}
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
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public PlayerService getPlayerService() {
		return playerService;
	}
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}
}
