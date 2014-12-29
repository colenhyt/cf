package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.service.StockService;
import cn.hd.mgr.EventManager;
import cn.hd.mgr.PlayerManager;
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
		stockMgr = StockManager.getInstance();
	}
	
	public String list(){
		List<Stockdata> list = stockMgr.getStockDatas();
		List<Stockdata> list2 = new ArrayList<Stockdata>();
		for (int i=0;i<list.size();i++){
			Stockdata data = list.get(i);
			Stockdata data2 = new Stockdata();
			data2.setDescs(data.getDescs());
			data2.setName(data.getName());
			data2.setId(data.getId());
			data2.setType(data.getType());
			LinkedList<Quote> lquotes = StockManager.getInstance().getQuotes(data.getId());
			JSONArray jsonquotes = JSONArray.fromObject(lquotes);
			data2.setJsonquotes(jsonquotes.toString());
			list2.add(data2);
			System.out.println(data.getName()+"发现行情: "+jsonquotes.toString().length());
		}
		JSONArray jsonObject = JSONArray.fromObject(list2);
		System.out.println("found stocks:"+jsonObject.toString().length());
		write(jsonObject.toString(),"utf-8");
		return null;
	}
	
	public String quotes(){
		LinkedList<Quote> lquotes = StockManager.getInstance().getQuotes(stock.getId());
		JSONArray jsonObject = JSONArray.fromObject(lquotes);
		write(jsonObject.toString(),"utf-8");		
		System.out.println(stock.getId()+" found stocks quote:"+jsonObject.toString().length());
		return null;
	}
	
	public String nextquotetime(){
		long margin = StockManager.getInstance().getMarginSec();
		long m2 = StockManager.getInstance().STOCK_QUOTE_PERIOD;
		float fmargin = (float)margin/(float)m2;
		String tt = String.valueOf(fmargin);
		write(tt,"utf-8");		
		System.out.println(margin+"上次行情过去时间比例:"+fmargin);
		return null;
	}
	
	public String lastquote(){
		int stockid = stock.getId();
		List<Quote> list = stockMgr.getLastQuotes(stockid);
		JSONArray jsonObject = JSONArray.fromObject(list);
		write(jsonObject.toString(),"utf-8");		
		//System.out.println("found stocks quote:"+jsonObject.toString());
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
				System.out.println("购买股票:"+stock.getItemid()+",qty="+stock.getQty());
			}else {
				int qq = (0 - stock.getQty());
				System.out.println("抛售股票:"+stock.getItemid()+",qty="+qq);
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
