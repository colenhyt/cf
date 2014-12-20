package cn.hd.mgr;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.StockdataService;

public class StockManager {
	private int STOCK_QUOTE_PERIOD = 7;
	private int STOCK_SAVE_PERIOD = 3600;
	private StockdataService stockdataService;
	private int tick = 0;
	private List<Stock> stocks;
	private Stockdata stockData;
    private static StockManager uniqueInstance = null;  
	
    public static StockManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new StockManager();  
        }  
        return uniqueInstance;  
     } 
    
    public StockManager(){
		try {
			stockdataService = new StockdataService();
			stockData = stockdataService.findActive();
			String jsonStr = "";
			jsonStr = new String(stockData.getData(),"utf-8");
			STOCK_QUOTE_PERIOD = stockData.getFreq();
			stocks = stockdataService.jsonToBeanList(jsonStr,Stock.class);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
  
    public List<Stock> getStocks(){
    	return stocks;
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
	    		//System.out.println("股票价格变化: "+stock.getName()+",涨跌幅:"+stock.getPer()+",原价格:"+oldPs+",现价格:"+stock.getPrice());
	    	}
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
//    	StockManager stmgr = StockManager.getInstance();
//    	stmgr.update();
    	StockService stockService = new StockService();
    	List<Stock> stocks = stockService.findByPlayerId(1);
    }
}
