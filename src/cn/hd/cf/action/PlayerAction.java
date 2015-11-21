package cn.hd.cf.action;

import cn.hd.base.BaseAction;
import cn.hd.cf.service.PlayerService;
import cn.hd.mgr.DataManager;

public class PlayerAction extends BaseAction {
	
	public String signin(){
		String pid = getHttpRequest().getParameter("playerid");
		DataManager.getInstance().addSignin(Integer.valueOf(pid));
		return null;
	}
	
	public String donetask(){
		String pid = getHttpRequest().getParameter("playerid");
		DataManager.getInstance().addDoneQuest(Integer.valueOf(pid));
		return null;
	}
}
