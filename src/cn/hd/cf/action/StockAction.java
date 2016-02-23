package cn.hd.cf.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONArray;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.InsureManager;
import cn.hd.mgr.LogMgr;
import cn.hd.mgr.SavingManager;
import cn.hd.mgr.StockManager;
import cn.hd.mgr.ToplistManager;

import com.alibaba.fastjson.JSON;

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
	
	/**
	 * 全部股票行情
	 * @return String 行情json数据
	 * */
	public String quotes(){
		List<Quote> lquotes = StockManager.getInstance().getBigQuotes(stock.getId());
		JSONArray jsonObject = JSONArray.fromObject(lquotes);
		write(jsonObject.toString(),"utf-8");		
		System.out.println(stock.getId()+" found stocks quote sizes:"+lquotes.size());
		return null;
	}
	
	/**
	 * 下次行情跳动时间
	 * @return String 行情跳动时间
	 * */
	public String nextquotetime(){
		float margin = StockManager.getInstance().getMarginSec();
		String tt = String.valueOf(margin);
		write(tt,"utf-8");		
		System.out.println("上次行情过去时间比例:"+margin);
		return null;
	}
	
	/**
	 * 获取特点股票行情
	 * @param String 股票id集合
	 * @return String 行情 json数据
	 * */
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
		String quotestr = JSON.toJSONString(mquotes);
		write(quotestr,"utf-8");		
		log.warn("request some stocks last quote:"+quotestr);
		return null;
	}	

	/**
	 * 买入/抛售股票
	 * @param Stock 股票 对象
	 * @return String 股票 json数据
	 * */
	public synchronized String add(){
		if (stock.getQty()==0){
			return msgStr(RetMsg.MSG_StockQtyIsZero);
		}
		if (stockMgr.isStockOpen()==false){
			return msgStr(RetMsg.MSG_StockIsClosed);
		}
		
		if (stock.getQty()>0){
			return this.buyStock();
		}else {
			return this.sellStock();
		}
	}	

	/**
	 * 抛售股票
	 * @param Stock 股票 对象
	 * @return String 股票 json数据
	 * */
	public synchronized String sellStock(){
		Saving liveSaving = SavingManager.getInstance().getSaving(stock.getPlayerid(), 1);
		float changeAmount = 0 - stock.getAmount();
		
		int ret = RetMsg.MSG_OK;
		int doType = 5;
		int qq = (0 - stock.getQty());
		Vector<Float> retps = stockMgr.deleteStock(stock.getPlayerid(), stock.getItemid(), qq);
		if (retps.size()==2){
			
			boolean doneQuest = DataManager.getInstance().doneQuest(stock.getPlayerid(), doType);
			if (doneQuest){
				changeAmount += 5000;
				LogMgr.getInstance().log(stock.getPlayerid()," stock quest prize 5000,type:"+doType);
			}
			
			liveSaving.setAmount(liveSaving.getAmount()+changeAmount);
			//更新活期存款
			float savingamount = SavingManager.getInstance().updateLiveSaving(stock.getPlayerid(),liveSaving);
			//更新排行榜:
			float amount = savingamount + InsureManager.getInstance().getInsureAmount(stock.getPlayerid()) + retps.get(1);
			ToplistManager.getInstance().updateToplist(stock.getPlayerid(),null,amount);	
			
			//构造返回消息
			stock.setLiveamount(liveSaving.getAmount());
			float profit = changeAmount - retps.get(0)*qq;
			stock.setProfit(profit);
			String str = JSON.toJSONString(stock);
			LogMgr.getInstance().log(stock.getPlayerid()," buy stock,str:"+str);
			return msgStr2(RetMsg.MSG_OK,str);
		}else {
			LogMgr.getInstance().log(stock.getPlayerid()," warn,stock error:"+stock.getPlayerid()+",item:"+stock.getItemid()+",qty:"+stock.getQty()+",ret:"+ret);
			return msgStr(ret);
		}
	}

	/**
	 * 买入股票
	 * @param Stock 股票 对象
	 * @return String 股票 json数据
	 * */
	public synchronized String buyStock(){
		Saving liveSaving = SavingManager.getInstance().getSaving(stock.getPlayerid(), 1);
		
		float changeAmount = 0 - stock.getAmount();
		
		int ret = RetMsg.MSG_OK;
		int doType = 4;
		if (liveSaving.getAmount()<stock.getAmount())
			return msgStr(RetMsg.MSG_MoneyNotEnough);
		stock.setCreatetime(new Date());
		ret = stockMgr.addStock(stock.getPlayerid(), stock);	
		if (ret==RetMsg.MSG_OK){
			
			boolean doneQuest = DataManager.getInstance().doneQuest(stock.getPlayerid(), doType);
			if (doneQuest){
				changeAmount += 5000;
				LogMgr.getInstance().log(stock.getPlayerid()," stock quest prize 5000,type:"+doType);
			}
			
			liveSaving.setAmount(liveSaving.getAmount()+changeAmount);
			stock.setLiveamount(liveSaving.getAmount());
			String str = JSON.toJSONString(stock);
			LogMgr.getInstance().log(stock.getPlayerid()," buy stock,str:"+str);
			SavingManager.getInstance().updateLiveSaving(stock.getPlayerid(),liveSaving);	
			return msgStr2(RetMsg.MSG_OK,str);
		}else {
			LogMgr.getInstance().log(stock.getPlayerid()," warn,stock error:"+stock.getPlayerid()+",item:"+stock.getItemid()+",qty:"+stock.getQty()+",ret:"+ret);
			return msgStr(ret);
		}
	}
}
