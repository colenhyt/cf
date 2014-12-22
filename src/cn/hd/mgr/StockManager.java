package cn.hd.mgr;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Quotedata;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.service.QuotedataService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.StockdataService;

public class StockManager {
	private static Logger logger = Logger.getLogger(StockManager.class); 
	private int STOCK_QUOTE_PERIOD = 7;
	private int STOCK_SAVE_PERIOD = 3600;
	private StockdataService stockdataService;
	private QuotedataService quotedataService;
	private Map<Integer,Quotedata> quoteMap;
	private int tick = 0;
	private List<Stock> stocks;
	private Stockdata stockData;
	private List<Quote>	quotes;
    private static StockManager uniqueInstance = null;  
	
    public static StockManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new StockManager();  
        }  
        return uniqueInstance;  
     } 
    
    public StockManager(){
		stockdataService = new StockdataService();
		stockData = stockdataService.findActive();
		quotedataService = new QuotedataService();
		List<Quotedata> quotedata = quotedataService.findQuotes();
		quotes = new ArrayList<Quote>();
		quoteMap = quotedataService.getQuoteMap();
		
		String jsonStr = null;
		try {
			jsonStr = new String(stockData.getData(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		STOCK_QUOTE_PERIOD = stockData.getFreq();
		stocks = stockdataService.jsonToBeanList(jsonStr,Stock.class);
		
//		//设置行情价格:
//		for (int i=0;i<stocks.size();i++){
//			Stock  stock = stocks.get(i);
//			Quotedata currQuotes = quoteMap.get(stock.getId());
//			String jsonString = new String(currQuotes.getData());
//			JSONArray arrayStrings = JSONArray.fromObject(jsonString);
//			if (arrayStrings.size()>0){
//				String str = arrayStrings.getString(arrayStrings.size()-1);
//				JSONArray arrayValues = JSONArray.fromObject(str);
//				Quote quote = new Quote();
//				quote.setStockid(stock.getId());
//				quote.setDate(arrayValues.getString(0));
//				quote.setOpenprice(Float.valueOf((float)arrayValues.getDouble(1)));
//				quote.setUpprice(Float.valueOf((float)arrayValues.getDouble(2)));
//				quote.setPrice(Float.valueOf((float)arrayValues.getDouble(3)));
//				quote.setLowprice(Float.valueOf((float)arrayValues.getDouble(4)));
//				quote.setQty(arrayValues.getInt(5));
//				stock.setPrice(quote.getPrice());
//				System.out.println("股票 "+stock.getName()+"获取最新行情价格: "+stock.getPrice());
//			}
//		}
     }
  
    public List<Stock> getStocks(){
    	return stocks;
    }
    
    public List<Quote> getQuotes(){
    	return quotes;
    }
    
    public void update(){
    	tick ++;
		//stock price update:
		if (tick%STOCK_QUOTE_PERIOD==0)
		{
			Random r = new Random();
		   	for (int i=0;i<stocks.size();i++){
	    		Stock stock = stocks.get(i);
	    		float r2 = (float)r.nextInt(stock.getPer().intValue());
	    		float per = r2/100;
	    		int addOrMinus = r.nextInt(100);
	    		float ps = stock.getPrice();
	    		float oldPs = ps;
	    		//涨:
	    		if (addOrMinus<60)
	    			ps += ps*per;
	    		else
	    			ps -= ps*per;
	    		
	    		stock.setPrice(ps);
	    		quotedataService.addNewQuote(stock.getId(), ps);
	    		System.out.println("股票价格变化: "+stock.getName()+",涨跌幅:"+stock.getPer()+",原价格:"+oldPs+",现价格:"+stock.getPrice());
	    	}
		   	logger.info("stock quote update!!!");
	    }		
		
		//数据库保存:
		if (tick%STOCK_SAVE_PERIOD==0){
			String jsonString = stockdataService.beanListToJson(stocks,Stock.class);
			stockData.setData(jsonString.getBytes());
			stockData.setCreatetime(new Date());
			stockdataService.updateByKey(stockData);
		}
	}
    
    public static void main(String[] args) {
//    	String a = "{'id':3,'name':'万科A','desc':'最大房地产股','price':18.7,'unit':100}";
//    	JSONObject obj = JSONObject.fromObject(a);
    	StockManager stmgr = StockManager.getInstance();
    	//stmgr.update();
    	StockService stockService = new StockService();
    	List<Stock> stocks = stockService.findByPlayerId(16);

    }
}
