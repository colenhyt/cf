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

	public synchronized String add(){
		if (stock.getQty()==0){
			return msgStr(RetMsg.MSG_StockQtyIsZero);
		}
		int ret = RetMsg.MSG_StockIsClosed;
		if (stockMgr.isStockOpen()==false){
			return msgStr(ret);
		}
		float inAmount = 0 - stock.getAmount();
		//先扣钱:
		ret = super.pushLive(stock.getPlayerid(), inAmount);
		if (ret==0){
			if (stock.getQty()>0){
				stock.setCreatetime(new Date());
				ret = stockMgr.addStock(stock.getPlayerid(), stock);	
			}else {
				int qq = (0 - stock.getQty());
				ret = stockMgr.deleteStock(stock.getPlayerid(), stock.getItemid(), qq);
			}
			if (ret!=RetMsg.MSG_OK){
				//钱放回去:
				log.warn("pid:"+stock.getPlayerid()+", warn,stock error:"+stock.getPlayerid()+",item:"+stock.getItemid()+",qty:"+stock.getQty());
				super.pushLive(stock.getPlayerid(),  stock.getAmount());
			}
			log.info("pid:"+stock.getPlayerid()+" buy stock:,itemid="+stock.getItemid()+",qty="+stock.getQty()+",ret:"+ret);
			super.playerTopUpdate(stock.getPlayerid());
		}else {
			log.warn("pid:"+stock.getPlayerid()+",error,player saving not found for stock:"+ret);
		}
		return msgStr(ret);
	}	
}
