package cn.hd.cf.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import net.sf.json.JSONArray;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.service.StockService;
import cn.hd.mgr.StockManager;

public class StockAction extends SavingAction {
	private StockManager stockMgr;
	private Stock	stock;
	
	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public StockAction(){
		init();
		stockMgr = StockManager.getInstance();
	}
	
	public String quotes(){
		System.out.println("request quotes");
		List<Quote> lquotes = StockManager.getInstance().getBigQuotes(stock.getId());
		JSONArray jsonObject = JSONArray.fromObject(lquotes);
		write(jsonObject.toString(),"utf-8");		
		System.out.println(stock.getId()+" found stocks quote sizes:"+lquotes.size());
		return null;
	}
	
	public String nextquotetime(){
		float margin = StockManager.getInstance().getMarginSec();
		String tt = String.valueOf(margin);
		write(tt,"utf-8");		
		System.out.println("上次行情过去时间比例:"+margin);
		return null;
	}
	
	public String pagelastquotes(){
		String pp = getHttpRequest().getParameter("stockids");
		JSONArray ppObj = JSONArray.fromObject(pp);
		Map<Integer,Float> mquotes = new HashMap<Integer,Float>();
		for (int i=0;i<ppObj.size();i++){
			String strstockid = (String)ppObj.get(i);
			int stockid = Integer.valueOf(strstockid);
			List<Quote> list = stockMgr.getLastQuotes(stockid);
			if (list.size()>0){
				Quote q = list.get(0);
				mquotes.put(stockid, q.getPrice());
			}
		}
		write(JSON.toJSONString(mquotes),"utf-8");		
		System.out.println("found stocks page quote:"+mquotes.size());
		return null;
	}	
	
	public String add(){
		if (stock.getQty()==0){
			return null;
		}
		int ret = RetMsg.MSG_StockIsClosed;
		if (StockManager.getInstance().isStockOpen()==false){
			writeMsg(ret);
			return null;
		}
		float inAmount = 0 - stock.getAmount();
		//先扣钱:
		ret = super.pushLive(stock.getPlayerid(), inAmount);
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
				super.pushLive(stock.getPlayerid(),  stock.getAmount());
				ret = RetMsg.MSG_SQLExecuteError;
			}
			super.playerTopUpdate(stock.getPlayerid());
		}else {
			System.out.println("没找到存款吗:"+ret);
		}
		writeMsg(ret);
		return null;
	}

	public int addStock(Stock stock2){
		if (stock2.getQty()==0){
			return 0;
		}
		float inAmount = 0 - stock2.getAmount();
		//先扣钱:
		int ret = super.pushLive(stock2.getPlayerid(), inAmount);
		if (ret==0){
			boolean exec = false;	
			if (stock2.getQty()>0){
				stock2.setCreatetime(new Date());
				exec = stockService.add(stock2);	
				System.out.println("购买股票:"+stock2.getItemid()+",qty="+stock2.getQty());
			}else {
				int qq = (0 - stock2.getQty());
				System.out.println("抛售股票:"+stock2.getItemid()+",qty="+qq);
				exec = stockService.removeStock(stock2.getPlayerid(), stock2.getItemid(), qq);
			}
			if (exec==false){
				//钱放回去:
				super.pushLive(stock2.getPlayerid(),  stock2.getAmount());
				ret = RetMsg.MSG_SQLExecuteError;
			}
			super.playerTopUpdate(stock2.getPlayerid());
		}
		return ret;
	}	
}
