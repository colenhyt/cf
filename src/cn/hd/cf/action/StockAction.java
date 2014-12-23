package cn.hd.cf.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseAction;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.StockService;
import cn.hd.mgr.EventManager;
import cn.hd.mgr.StockManager;

public class StockAction extends SavingAction {
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
		if (stock.getQty()==0){
			return null;
		}
		float inAmount = 0 - stock.getAmount();
		//先扣钱:
		int ret = super.updateLiveSaving(stock.getPlayerid(), inAmount);
		if (ret==0){
			boolean exec = false;	
			if (stock.getQty()>0){
				stock.setCreatetime(new Date());
				exec = stockService.add(stock);	
			}else {
				System.out.println("抛售股票");
				int qq = (0 - stock.getQty());
				exec = stockService.removeStock(stock.getPlayerid(), stock.getItemid(), qq);
			}
			if (exec==false){
				//钱放回去:
				super.updateLiveSaving(stock.getPlayerid(),  stock.getAmount());
				ret = RetMsg.MSG_SQLExecuteError;
			}
		}
		writeMsg(ret);
		return null;
	}	
	
	
	public String update()
	{
		stockService.update(stock);		
		return null;
	}	
}
