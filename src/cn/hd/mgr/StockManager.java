package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.service.StockdataService;
import cn.hd.cf.service.ToplistService;

public class StockManager {
	private static Logger logger = Logger.getLogger(StockManager.class); 
	public int STOCK_QUOTE_PERIOD = 5;
	private int STOCK_SAVE_PERIOD = 5;
	private StockdataService stockdataService;
	private long lastQuoteTime;
	private Map<Integer,LinkedList<Quote>> quoteMap;
	private int tick = 0;
	private List<Stockdata> stockData;
    private static StockManager uniqueInstance = null;  
	
    public static StockManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new StockManager();  
        }  
        return uniqueInstance;  
     } 
    
    public StockManager(){
		lastQuoteTime = System.currentTimeMillis();
		stockdataService = new StockdataService();
		stockData = stockdataService.findActive();
		quoteMap = new HashMap<Integer,LinkedList<Quote>>();
	   	for (int i=0;i<stockData.size();i++){
    		Stockdata  stock = stockData.get(i);
    		int fre = stock.getFreq();
    		STOCK_QUOTE_PERIOD = fre*60*1000/EventManager.TICK_PERIOD;
//    		STOCK_QUOTE_PERIOD = 5;
	    		String json = new String(stock.getQuotes());
	    		JSONArray array = JSONArray.fromObject(json);
	    		List<Quote> quotes = JSONArray.toList(array, Quote.class);
	    		if (quotes.size()==0) continue;
	    		LinkedList<Quote> qquotes = new LinkedList<Quote>();
	    		for(int j=0;j<quotes.size();j++){
	    			quotes.get(j).setStockid(stock.getId());
	    			qquotes.offer(quotes.get(j));
	    		}
	    		quoteMap.put(stock.getId(), qquotes);
	    		
	    }		
				
     }
  
    public List<Stockdata> getStockDatas(){
    	return stockData;
    }
    
    public float getMarginSec(){
    	long curr = System.currentTimeMillis();
		float diffdd = stockdataService.findDayMargin(curr,lastQuoteTime,-1);
		return diffdd;
    }
        
    public LinkedList<Quote> getQuotes(int stockid){
    	return quoteMap.get(stockid);
    }
    
    public List<Quote> getLastQuotes(int stockid){
    	List<Quote> quotes = new ArrayList<Quote>();
    	if (stockid>=0){
    		LinkedList<Quote> q = quoteMap.get(stockid);
    		quotes.add(q.peekLast());
  		
    	}else {		//all last quotes
	    	Iterator<Integer> iter = quoteMap.keySet().iterator();
	    	while (iter.hasNext()){
	    		LinkedList<Quote> q = quoteMap.get(iter.next());
	    		quotes.add(q.peekLast());
	    	}
    	}
    	return quotes;
    }
    
    public void update(){
    	tick ++;
 		//stock price update:
		if (tick%STOCK_QUOTE_PERIOD==0){
			lastQuoteTime = System.currentTimeMillis();
			System.out.println("条款时间:"+STOCK_QUOTE_PERIOD);
			for (int i=0;i<stockData.size();i++){
	    		Stockdata  stock = stockData.get(i);
	 			LinkedList<Quote> lquote = quoteMap.get(stock.getId());
	 			if (lquote==null||lquote.size()==0) continue;
	 			Random r = new Random();
	 			
		    		float r2 = (float)r.nextInt(stock.getPer().intValue());
		    		float per = r2/100;
		    		int addOrMinus = r.nextInt(100);
		    		Quote quote = lquote.peekLast();
		    		float ps = quote.getPrice();
		    		//涨:
		    		if (addOrMinus<50)
		    			ps += ps*per;
		    		else
		    			ps -= ps*per;
		    		
		    		lquote.poll();
		    		Quote newq = new Quote();
		    		newq.setPrice(ps);
		    		newq.setStockid(stock.getId());
		    		newq.setLowprice(quote.getLowprice());
		    		newq.setQty(quote.getQty());
		    		newq.setUpprice(quote.getUpprice());
		    		lquote.offer(newq);
		    		System.out.println("股票价格变化: "+stock.getName()+",涨跌幅:"+stock.getPer()+",上一个价格:"+quote.getPrice()+",现价格:"+newq.getPrice());
		    }	
		}
		    	
		if (tick%STOCK_SAVE_PERIOD==0){
			for (int i=0;i<stockData.size();i++){
	    		Stockdata  stock = stockData.get(i);
	 			LinkedList<Quote> lquote = quoteMap.get(stock.getId());
	 			if (lquote==null||lquote.size()==0) continue;
	 			stock.setCreatetime(new Date());
	 			stock.setJsonquotes("");
	 			//stock.setQuotes(stockdataService.beanQueueToJson(lquote, Quote.class).getBytes());
	    		//stockdataService.updateByKey(stock);
			}
		}		
		//数据库保存:

	}
    
    public static void main(String[] args) {
//    	String a = "{'id':3,'name':'万科A','desc':'最大房地产股','price':18.7,'unit':100}";
//    	JSONObject obj = JSONObject.fromObject(a);
    	StockManager stmgr = StockManager.getInstance();
    	stmgr.update();
    	float e = stmgr.getMarginSec();
    	int a = 10;
    	Date dt= new Date();
    	  long time= dt.getTime();
    	ToplistService toplist = new ToplistService();
    	for (int i=0;i<1000;i++){
        	int ab = toplist.findCount(0,-1);    		
    	}
    	Date dt2= new Date();
    	long end = dt2.getTime();
    	System.out.println("cost "+(end-time));

    }
}
