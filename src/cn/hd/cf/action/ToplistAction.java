package cn.hd.cf.action;

import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Toplist;
import cn.hd.mgr.LogMgr;
import cn.hd.mgr.ToplistManager;

public class ToplistAction extends BaseAction {
	private Toplist toplist;

	/**
	 * 获取排行榜数据
	 * @return String 排行榜json数据
	 * */
	public String list(){
		List<Toplist> weeklist = ToplistManager.getInstance().findByType(0);
		List<Toplist> monthlist = ToplistManager.getInstance().findByType(1);
		
		Toplist weektop = ToplistManager.getInstance().findByPlayerId(toplist.getPlayerid());
		Toplist monthtop = null;
		if (weektop!=null){
			int week = ToplistManager.getInstance().getTopNumber(toplist.getPlayerid(),0);
			weektop.setTop(week+1);
			weeklist.add(weektop);

			int mm = ToplistManager.getInstance().getTopNumber(toplist.getPlayerid(),1);
			monthtop = new Toplist();
			monthtop.setPlayername(weektop.getPlayername());
			monthtop.setPlayerid(weektop.getPlayerid());
			monthtop.setZan(weektop.getZan());
			monthtop.setMoney(weektop.getMoney());
			monthtop.setTop(mm+1);
			
			monthlist.add(monthtop);
		}

		JSONArray jsonObject = new JSONArray();
		jsonObject.add(weeklist);
		jsonObject.add(monthlist);
		//System.out.println("取得排行榜数据:week:"+weeklist.size()+",month:"+monthlist.size());
//		write(jsonObject.toString(),"utf-8");
		return jsonObject.toString();
	}
	
	/**
	 * 月排行榜数据
	 * @return String 排行榜 json数据
	 * */
	public String monthlist(){
		List<Toplist> tt = ToplistManager.getInstance().findByType(1);
		//System.out.println("get list :"+tt.size());
		JSONArray jsonObject = JSONArray.fromObject(tt);
		
		write(jsonObject.toString(),"utf-8");
		return null;
	}
	
	/**
	 * 排行榜玩家点赞
	 * @param Toplist 对象
	 * @return String 点赞json数据返回
	 * */
	public String zan(){
		LogMgr.getInstance().log(toplist.getPlayerid()," zan="+toplist.getZan());
		ToplistManager.getInstance().updateZan(toplist);
		return null;
	}
	
	public Toplist getToplist() {
		return toplist;
	}

	public void setToplist(Toplist toplist) {
		this.toplist = toplist;
	}

}
