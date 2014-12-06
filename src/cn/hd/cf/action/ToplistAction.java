package cn.hd.cf.action;

import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.ToplistService;
import cn.hd.event.TimerTest;

public class ToplistAction extends BaseAction {
	private Toplist toplist;
	private ToplistService toplistService;

	public ToplistAction(){
		init("toplistService");
	}
	
	public String daylist(){
		List<Toplist> tt = toplistService.findByType(toplist.getId());
		JSONArray jsonObject = JSONArray.fromObject(tt);
		//System.out.println("dayliseeeeeeeeeet scrie: "+jsonObject.toString());
		java.util.Timer timer = new java.util.Timer(true);  
		TimerTest task = new TimerTest();
		//timer.schedule(task, 3000,3000);   
		
		write(jsonObject.toString(),"utf-8");
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
