package cn.hd.cf.test;

import java.util.Date;

import cn.freeteam.util.MD5;
import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.service.PlayerService;

public class TestService {
	
	public static void testAction()
	{
		LoginAction action = new LoginAction();		
		PlayerWithBLOBs player = new PlayerWithBLOBs();
		player.setAccountid(1);
		player.setPlayername("test1");
		player.setPwd(MD5.MD5("aaa"));
		Date time = new Date(); 
		player.setCreatetime(time);		
		action.setPlayer(player);
		//action.register();
		PlayerService s = new PlayerService();
		s.add(player);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestService.testAction();
	}

}
