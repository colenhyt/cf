package cn.hd.cf.action;

import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.ToplistService;

public class ToplistAction extends BaseAction {
	private Toplist toplist;
	private ToplistService toplistService;

	public ToplistAction(){
		init("toplistService");
	}
	
	public String list(){
		List<Toplist> weeklist = toplistService.findByType(0);
		List<Toplist> monthlist = toplistService.findByType(1);
		JSONArray jsonObject = new JSONArray();
		jsonObject.add(weeklist);
		jsonObject.add(monthlist);
		System.out.println("取得排行榜数据:"+weeklist.size());
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
		Toplist toplist2 = toplistService.findByPlayerId(toplist.getPlayerid(),0);
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
