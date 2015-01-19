package cn.hd.cf.action;

import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.ToplistService;

public class ToplistAction extends BaseAction {
	private Toplist toplist;
	private ToplistService toplistService;
	private PlayerService playerService;
	
	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public ToplistAction(){
		init("toplistService","playerService");
	}
	
	public String list(){
		List<Toplist> weeklist = toplistService.findByType(0);
		List<Toplist> monthlist = toplistService.findByType(1);
		
		System.out.println("get toplist playerid:"+toplist.getPlayerid());
		Toplist weektop = toplistService.findByPlayerId(toplist.getPlayerid());
		Toplist monthtop = null;
		if (weektop!=null){
			float fMm = weektop.getMoney().floatValue();
			int week = toplistService.findCountByGreaterMoney(toplist.getPlayerid(),0,fMm);
			int mm = toplistService.findCountByGreaterMoney(toplist.getPlayerid(),1,fMm);
			weektop.setTop(week+1);
			monthtop = new Toplist();
			monthtop.setPlayername(weektop.getPlayername());
			monthtop.setPlayerid(weektop.getPlayerid());
			monthtop.setZan(weektop.getZan());
			monthtop.setMoney(weektop.getMoney());
			monthtop.setTop(mm+1);
			
			weeklist.add(weektop);
			monthlist.add(monthtop);
		}

		JSONArray jsonObject = new JSONArray();
		jsonObject.add(weeklist);
		jsonObject.add(monthlist);
		System.out.println("取得排行榜数据:week:"+weeklist.size()+",month:"+monthlist.size());
		write(jsonObject.toString(),"utf-8");
		return null;
	}
	
	public String monthlist(){
		List<Toplist> tt = toplistService.findByType(1);
		//System.out.println("get list :"+tt.size());
		JSONArray jsonObject = JSONArray.fromObject(tt);
		
		write(jsonObject.toString(),"utf-8");
		return null;
	}
	
	public String zan(){
		Toplist toplist2 = toplistService.findByPlayerId(toplist.getPlayerid());
		System.out.println("toplist zan: playerid="+toplist.getPlayerid()+",zan="+toplist.getZan());
		if (toplist2!=null){
			toplist2.setZan(toplist.getZan());
			toplistService.updateZan(toplist2);
		}
		return null;
	}
	
	public Toplist getToplist() {
		return toplist;
	}

	public void setToplist(Toplist toplist) {
		this.toplist = toplist;
	}

	public ToplistService getToplistService() {
		return toplistService;
	}

	public void setToplistService(ToplistService toplistService) {
		this.toplistService = toplistService;
	}

}
