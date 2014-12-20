package cn.hd.cf.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.Stock;
import cn.hd.cf.service.StockService;
import cn.hd.mgr.EventManager;
import cn.hd.mgr.StockManager;

public class StockAction extends BaseAction {
	private StockManager stockMgr;
	private StockService stockService;
	private Stock	stock;
	
	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}
	
	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public StockAction(){
		init("stockService");
		EventManager.getInstance().start();
		stockMgr = StockManager.getInstance();
	}
	
	public String list(){
		List<Stock> list = stockMgr.getStocks();
		JSONArray jsonObject = JSONArray.fromObject(list);
		System.out.println("found stocks:"+jsonObject.toString());
		write(jsonObject.toString(),"utf-8");
		return null;
	}
	
	public String quote(){
		List<Stock> list = stockMgr.getStocks();
		JSONArray jsonObject = JSONArray.fromObject(list);
		//System.out.println("found stocks quote:"+jsonObject.toString());
		write(jsonObject.toString(),"utf-8");		
		return null;
	}
	
	
	public String add(){
		int ret = stockService.add(stock);	
		writeMsg(ret);
		return null;
	}	
}
