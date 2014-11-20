package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.freeteam.base.BaseAction;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.service.PlayerService;

public class LoginAction extends BaseAction {
	private Player player;
	private PlayerService playerService;
	
	public LoginAction(){
		init("playerService");
	}
	
	public String register(){
		if (player.getPlayername().length()<=0)
		{
			return null;
		}
		boolean bExist = playerService.have(player.getAccountid());
		if (bExist)
		{
			return null;
		}
		PlayerWithBLOBs playerBlob = new PlayerWithBLOBs();
		playerBlob.setAccountid(player.getAccountid());
		Date time = new Date(); 
		playerBlob.setCreatetime(time);
		playerBlob.setPlayername(player.getPlayername());
		playerBlob.setPwd("jiaoyanma");
		playerService.add(playerBlob);
		return null;
	}
	
	public String login()
	{
		PlayerWithBLOBs tplayer = playerService.find(player.getPlayerid(),player.getPwd());
		if (tplayer!=null)
		{
			//取需要更新的模块id
		}
		return null;
	}
	
	public List<String> findUpdateDataIds(String oldIds)
	{
		List<String> dataIds = new ArrayList<String>();
		String[] arrOldIds = oldIds.split(",");
		for (int i=0;i<arrOldIds.length;i+=2)
		{
			String id = arrOldIds[i];
			String ver = arrOldIds[i+1];
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
