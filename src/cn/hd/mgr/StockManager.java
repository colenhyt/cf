package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import cn.hd.base.Base;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.tools.StockdataService;

public class StockManager {
	protected Logger  log = Logger.getLogger(getClass()); 
	private static Logger logger = Logger.getLogger(StockManager.class); 
	public int STOCK_QUOTE_PERIOD = 5;
	private int STOCK_SAVE_PERIOD = 5;
	private Date lastUpdateDate;
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
		lastUpdateDate = null;
		StockdataService stockdataService = new StockdataService();
		stockData = stockdataService.findActive();
		quoteMap = new HashMap<Integer,LinkedList<Quote>>();
	   	for (int i=0;i<stockData.size();i++){
    		Stockdata  stock = stockData.get(i);
    		int fre = stock.getFreq();
    		STOCK_QUOTE_PERIOD = fre*60*1000/EventManager.TICK_PERIOD;
	    		String json = new String(stock.getQuotes());
	    		JSONArray array = JSONArray.fromObject(json);
	    		List<Quote> quotes = JSONArray.toList(array, Quote.class);
	    		if (quotes.size()==0) {
	    			System.out.println("该股票无行情:"+stock.getName());
	    			continue;
	    		}
	    		LinkedList<Quote> qquotes = new LinkedList<Quote>();
	    		for(int j=0;j<quotes.size();j++){
	    			quotes.get(j).setStockid(stock.getId());
	    			qquotes.offer(quotes.get(j));
	    		}
	    		quoteMap.put(stock.getId(), qquotes);
	    		
	    }		
				
     }
  
    public float getMarginSec(){
    	long curr = System.currentTimeMillis();
		float diffdd = Base.findDayMargin(curr,lastQuoteTime,-1);
		return diffdd;
    }
        
    public List<Quote> getBigQuotes(int stockid){
    	LinkedList<Quote> details = quoteMap.get(stockid);
    	List<Quote> quotes = new ArrayList<Quote>();
    	for (int i=0;i<details.size();i+=2){
    		if (details.size()>i){
    			quotes.add(details.get(i));
    		}
    	}
    	return quotes;
    }
    
    public List<Quote> getLastQuotes(int stockid){
    	List<Quote> quotes = new ArrayList<Quote>();
    	if (stockid>=0){
    		LinkedList<Quote> q = quoteMap.get(stockid);
    		if (q!=null&&q.size()>0){
       		 quotes.add(q.peekLast());
    		}
    	}
    	return quotes;
    }
    
    public boolean isStockOpen(){
		Date now = new Date();
		Calendar cl = Calendar.getInstance();
		cl.setTime(now);
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		if (hour>=9&&hour<21)
			return true;
		return false;
    }
    
    public void update(){
    	tick ++;
 		//stock price update:
		if (tick%STOCK_QUOTE_PERIOD==0){
			boolean isOpen = isStockOpen();
			//System.out.println("stock update"+isOpen);
			if (isOpen!=true) return;
			
    		Date now = new Date();
    		boolean isNewDay = false;
			if (lastUpdateDate!=null){
				Calendar cl = Calendar.getInstance();
				cl.setTime(now);
				int day = cl.get(Calendar.DAY_OF_MONTH);
				cl.setTime(lastUpdateDate);
				int hisday = cl.get(Calendar.DAY_OF_MONTH);
				isNewDay = hisday!=day; 
			}
    		lastUpdateDate = new Date();
			lastQuoteTime = System.currentTimeMillis();
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

					//假如前一天，取休市价，并清空行情价格
		    		if (isNewDay){
		    			//lquote.clear();
		    			log.debug("清空昨天价格");
		    		}
		    		
		    		//lquote.poll();	//不删除行情数据:
		    		
		    		Quote newq = new Quote();
		    		newq.setPrice(ps);
		    		newq.setStockid(stock.getId());
		    		newq.setLowprice(quote.getLowprice());
		    		newq.setQty(quote.getQty());
		    		newq.setUpprice(quote.getUpprice());
		    		lquote.offer(newq);
		    		//System.out.println("行情价格个数"+lquote.size());
		    		//System.out.println("股票价格变化: "+stock.getName()+",涨跌幅:"+stock.getPer()+",上一个价格:"+quote.getPrice()+",现价格:"+newq.getPrice());
		    }	
		}
		    	
		if (tick%STOCK_SAVE_PERIOD==0){
			for (int i=0;i<stockData.size();i++){
	    		Stockdata  stock = stockData.get(i);
	 			LinkedList<Quote> lquote = quoteMap.get(stock.getId());
	 			if (lquote==null||lquote.size()==0) continue;
	 			stock.setCreatetime(new Date());
	 			stock.setJsonquotes("");
	 			StockdataService stockdataService = new StockdataService();
//	 			stock.setQuotes(stockdataService.beanQueueToJson(lquote, Quote.class).getBytes());
//	    		stockdataService.updateByKey(stock);
			}
		}		
		//数据库保存:

	}
    
    public static void main(String[] args) {
//    	String a = "{'id':3,'name':'万科A','desc':'最大房地产股','price':18.7,'unit':100}";
//    	JSONObject obj = JSONObject.fromObject(a);
    	StockManager stmgr = StockManager.getInstance();
    	//stmgr.getLastQuotes(8);
    	//stmgr.update();
    	stmgr.isStockOpen();

    }
}
