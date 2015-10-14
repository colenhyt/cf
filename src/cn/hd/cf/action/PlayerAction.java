package cn.hd.cf.action;

import cn.hd.base.BaseAction;
import cn.hd.cf.service.PlayerService;

public class PlayerAction extends BaseAction {
	
	public PlayerAction(){
	}
	
	public String signin(){
		PlayerService playerService = new PlayerService();
		String pid = getHttpRequest().getParameter("playerid");
		playerService.addSignin(Integer.valueOf(pid));
		return null;
	}
	
	public String donetask(){
		PlayerService playerService = new PlayerService();
		String pid = getHttpRequest().getParameter("playerid");
		playerService.addDoneQuest(Integer.valueOf(pid));
		return null;
	}
}
