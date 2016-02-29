package cn.hd.cf.action;

import java.io.IOException;

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
	public synchronized String list(){
		ToplistManager mgr = ToplistManager.getInstance();
		mgr.load();
		int playerid = toplist.getPlayerid();
		int zan = 0;
		float fMoney = 0;
		Toplist top = mgr.findByPlayerId(playerid);
		if (top!=null) {
			zan = top.getZan();
			fMoney = top.getMoney().floatValue();
		}
		String weekTopStr = "[";
		int weektop = mgr.getTopNumber(playerid,0)+1;
		String wTopStr = mgr.findByType(0);
		weekTopStr += wTopStr.substring(1, wTopStr.length()-1);
		weekTopStr += ",["+playerid+",'',"+fMoney+","+zan+","+weektop+"]";
		weekTopStr += "]";
		
		String monthTopStr = "[";
		int monthtop = mgr.getTopNumber(playerid,1)+1;
		String mTopStr = mgr.findByType(1);
		monthTopStr += mTopStr.substring(1, mTopStr.length()-1);
		monthTopStr += ",["+playerid+",'',"+fMoney+","+zan+","+monthtop+"]";
		monthTopStr += "]";
		String str = "["+weekTopStr+","+monthTopStr+"]";
//		log.warn(str);
		return str;
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
