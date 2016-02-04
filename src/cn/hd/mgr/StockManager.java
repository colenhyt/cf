package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import cn.hd.base.Base;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.action.StockAction;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Stockdata;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;

public class StockManager extends MgrBase{
	protected Logger  log = Logger.getLogger(getClass()); 
	public int STOCK_QUOTE_PERIOD = 5;
	private int STOCK_SAVE_PERIOD = 5;
	private Date lastUpdateDate;
	private long lastQuoteTime;
	private Map<Integer,LinkedList<Quote>> quoteMap;
	private int tick = 0;
	private List<Stockdata> stockData;
    private static StockManager uniqueInstance = null;  
	private Vector<RedisClient> redisClients;
	private Map<Integer,String>	stockMap;
	private Map<Integer,List<Stock>>	stocksMap;
	private StockAction action;
	
    public static StockManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new StockManager();  
        }  
        return uniqueInstance;  
     } 
    
    public void init(){
    	action = new StockAction();
		lastQuoteTime = System.currentTimeMillis();
		lastUpdateDate = null;
		stockMap = Collections.synchronizedMap(new HashMap<Integer,String>());
		stocksMap = Collections.synchronizedMap(new HashMap<Integer,List<Stock>>());
		
		redisClients = new Vector<RedisClient>();
		dataThreads = new Vector<DataThread>();
		 for (int i=0;i<redisCfg1.getThreadCount();i++){
			 //read:
			RedisClient client = new RedisClient(redisCfg1);
			redisClients.add(client);
			
			 //write:
			 DataThread dataThread = new DataThread(redisCfg1);
			dataThreads.add(dataThread);
			dataThread.start();
		 }	 
		 
    	Jedis jedis = redisClients.get(0).getJedis();   

    	
//    	Set<String> playerids = jedis.hkeys(super.DATAKEY_STOCK);
//    	for (String strpid:playerids){
//    		String jsonitems = jedis.hget(super.DATAKEY_STOCK, strpid);
//    		List<Stock> list = JSON.parseArray(jsonitems, Stock.class);
//    		stocksMap.put(Integer.valueOf(strpid), list);
//    	}
//    	
    	redisClients.get(0).returnResource(jedis);
//    	log.warn("load stocks :" + playerids.size());    
    	
		stockData = new ArrayList<Stockdata>();
	    	
		Jedis j3 = jedisClient3.getJedis();
		
//		 StockdataService stockdataService = new StockdataService();			
//		 stockData = stockdataService.findActive();
//		for (Stockdata item:stockData){
//			j3.hset(MgrBase.DATAKEY_DATA_STOCK, String.valueOf(item.getId()), JSON.toJSONString(item));
//		}
		
		List<String> stockitems = j3.hvals(MgrBase.DATAKEY_DATA_STOCK);
		jedisClient3.returnResource(j3);
		
		quoteMap = Collections.synchronizedMap(new HashMap<Integer,LinkedList<Quote>>());
	   	for (String str:stockitems){
    		Stockdata  stock = JSON.parseObject(str,Stockdata.class);
    		stockData.add(stock);
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
    	log.warn("stock init :"+quoteMap.size());

				
     }
  
    public float getMarginSec(){
    	long curr = System.currentTimeMillis();
		float diffdd = Base.findDayMargin(curr,lastQuoteTime,-1);
		return diffdd;
    }
        
    
	public synchronized Map<Integer,List<Stock>> findMapStocks(int playerId)
	{
		Map<Integer,List<Stock>> smap = new HashMap<Integer,List<Stock>>();
		
		List<Stock> ss = getStockList(playerId);
		if (ss==null)
			return smap;
		
		for (int i=0;i<ss.size();i++){
			List<Stock> list = smap.get(ss.get(i).getItemid());
			if (list==null){
				list = new ArrayList<Stock>();
				smap.put(ss.get(i).getItemid(), list);
			}
			list.add(ss.get(i));
		}
		
		return smap;
	}
    public synchronized List<Stock> getStockList(int playerId){
    	List<Stock> list = null;
    	if (list==null){
    		int index = playerId%redisClients.size();
			RedisClient jedisClient = redisClients.get(index);
			Jedis jedis = jedisClient.getJedis();
			String liststr = jedis.hget(MgrBase.DATAKEY_STOCK, String.valueOf(playerId));
			jedisClient.returnResource(jedis);    		
			if (liststr!=null){
				list = JSON.parseArray(liststr, Stock.class);
				stocksMap.put(playerId, list);
			}
    	}	
    	return list;		
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
	 			stock.setQuotes(JSON.toJSONString(lquote).getBytes());
	 			//todo: 行情更新会导致CPU占满
//	 			Jedis jedis = jedisClient3.getJedis();   
//	 			jedis.hset(MgrBase.DATAKEY_DATA_STOCK, String.valueOf(stock.getId()), JSON.toJSONString(stock));
//	 			jedisClient3.returnResource(jedis);
			}
		}		
		//数据库保存:

	}
    
    public synchronized int addStock(int playerId,Stock record){
		List<Stock> list = getStockList(playerId);
		if (list==null){
			list = new ArrayList<Stock>();
			stocksMap.put(playerId, list);
		}
		list.add(record);
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
		dataThread.updateStock(playerId, JSON.toJSONString(list));
		return RetMsg.MSG_OK;
	}

	public synchronized int deleteStock(int playerId,int stockId,int qty){
		List<Stock> list = getStockList(playerId);
		if (list==null)
			return RetMsg.MSG_StockNotExist;
		
		int needRemoveQty = qty;
		boolean updated = false;
		for (int i=0;i<list.size();i++){
			Stock ss = list.get(i);
			if (ss.getItemid().intValue()!=stockId)
				continue;
			
			updated = true;
			
			if (ss.getQty()<=needRemoveQty){
				needRemoveQty -= ss.getQty();
				list.remove(i);
				i--;
				log.warn("delete stock:"+ss.getQty());
			}else {
				ss.setQty(ss.getQty()-needRemoveQty);
				ss.setAmount(ss.getQty()*ss.getPrice());
				log.warn("update stock:"+ss.getQty());
				needRemoveQty = 0;
//				dataThread.updateStock(ss);
			}
			if (needRemoveQty==0) {
				break;
			}
		}		
		if (updated){
			DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
			dataThread.updateStock(playerId, JSON.toJSONString(list));
		}
		return RetMsg.MSG_OK;
	}

	public synchronized String add(int playerid,int itemid,int qty,float price,float amount){
		Player p = DataManager.getInstance().findPlayer(playerid);
		if (p==null){
			return action.msgStr(RetMsg.MSG_PlayerNotExist);
		}    	
		Stock item = new Stock();
		item.setPlayerid(playerid);
		item.setItemid(itemid);
		item.setQty(qty);
		item.setAmount(amount);
		item.setPrice(price);
		action.setStock(item);
		return action.add();
	}

	public static void main(String[] args) {
//    	String a = "{'id':3,'name':'万科A','desc':'最大房地产股','price':18.7,'unit':100}";
//    	JSONObject obj = JSONObject.fromObject(a);
    	StockManager stmgr = StockManager.getInstance();
    	stmgr.init();
    	//stmgr.getLastQuotes(8);
    	//stmgr.update();
    	Stock record = new Stock();
    	record.setItemid(1);
    	record.setQty(100);
    	record.setPrice(Float.valueOf(10));
    	stmgr.addStock(2, record);
    	
    	record = new Stock();
    	record.setItemid(1);
    	record.setPrice(Float.valueOf(10));
    	record.setQty(200);
    	stmgr.addStock(2, record);

    	record = new Stock();
    	record.setItemid(1);
    	record.setPrice(Float.valueOf(10));
    	record.setQty(300);
    	stmgr.addStock(2, record);
    	
    	stmgr.deleteStock(2, 1, 800);
    }
}
