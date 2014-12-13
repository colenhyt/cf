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
		List<Toplist> tt = toplistService.findByType(toplist.getType());
		JSONArray jsonObject = JSONArray.fromObject(tt);
		System.out.println("dayliseeeeeeeeeet scrie: "+jsonObject.toString());
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
