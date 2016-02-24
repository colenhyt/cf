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
		ToplistManager mgr = ToplistManager.getInstance();
		mgr.load();
		int zan = 0;
		float fMoney = 0;
		Toplist top = mgr.findByPlayerId(toplist.getPlayerid());
		if (top!=null) {
			zan = top.getZan();
			fMoney = top.getMoney().floatValue();
		}
		int weektop = mgr.getTopNumber(toplist.getPlayerid(),0)+1;
		int monthtop = mgr.getTopNumber(toplist.getPlayerid(),1)+1;
		String str = weektop+";"+mgr.topWeekToplistStr+";"+monthtop+";"+mgr.topMonthToplistStr+";"+zan+";"+fMoney;
		//System.out.println("取得排行榜数据:week:"+weeklist.size()+",month:"+monthlist.size());
//		write(jsonObject.toString(),"utf-8");
		return str;
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
