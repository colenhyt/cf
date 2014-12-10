package cn.hd.cf.action;

import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Stock;
import cn.hd.mgr.EventManager;
import cn.hd.mgr.StockManager;

public class StockAction extends BaseAction {
	private StockManager stockMgr;
	
	public StockAction(){
		EventManager.getInstance().start();
		stockMgr = StockManager.getInstance();
	}
	
	public String list(){
		List<Stock> list = stockMgr.getStocks();
		JSONArray jsonObject = JSONArray.fromObject(list);
		System.out.println("found obj:"+jsonObject.toString());
		write(jsonObject.toString(),"utf-8");
		return null;
	}
}
