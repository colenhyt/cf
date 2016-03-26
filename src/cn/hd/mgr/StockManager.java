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
import redis.clients.jedis.Pipeline;
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
	private int STOCK_SAVE_PERIOD = 20*30;		//30分钟保存一次
	private Date lastUpdateDate;
	private long lastQuoteTime;
	private final int QUOTE_MAXLENGTH = 200;
	private Map<Integer,LinkedList<Quote>> quoteMap;
	private int tick = 0;
	private List<Stockdata> stockData;
    private static StockManager uniqueInstance = null;  
	private Vector<RedisClient> redisClients;
	private Vector<RedisClient> quoteClients;
	
    public static StockManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new StockManager();  
        }  
        return uniqueInstance;  
     } 
    
    public void init(){
 		lastQuoteTime = System.currentTimeMillis();
		lastUpdateDate = null;
		
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

    	
    	redisClients.get(0).returnResource(jedis);
//    	LogMgr.getInstance().log("load stocks :" + playerids.size());    
    	
		stockData = new ArrayList<Stockdata>();
	    	
		quoteClients = new Vector<RedisClient>();
		
		for (int i=0;i<5;i++){
			RedisClient quclient = new RedisClient(redisCfg3);
			quoteClients.add(quclient);
		}
		
		Jedis j3 = jedisClient3.getJedis();
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
	    		//log.warn("quotes  length:"+array.size());
	    		if (quotes.size()==0) {
	    			System.out.println("该股票无行情:"+stock.getName());
	    			continue;
	    		}
	    		LinkedList<Quote> qquotes = new LinkedList<Quote>();
	    		for(int j=0;j<quotes.size();j++){
	    			Quote qq = new Quote();
	    			qq.setPrice(quotes.get(j).getPrice());
	    			qquotes.offer(qq);
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
        
    
	public Map<Integer,List<Stock>> findMapStocks(int playerId)
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
	
	public int updateStock(Stock stock){
		List<Stock> list =getStockList(stock.getPlayerid());
		boolean updated = false;
		for (Stock item:list){
			if (item.getItemid()==stock.getItemid()){
				item.setAmount(stock.getAmount());
				item.setQty(stock.getQty());
				item.setPrice(stock.getPrice());
				item.setUpdatetime(new Date());
				updated = true;
			}
		}
		if (updated==true){
			DataThread dataThread = dataThreads.get(stock.getPlayerid()%dataThreads.size());
			dataThread.updateStock(stock.getPlayerid(), JSON.toJSONString(list));	
			return RetMsg.MSG_OK;
		}
		return RetMsg.MSG_StockNotExist;
	}
	
    public List<Stock> getStockList(int playerId){
    	List<Stock> list = null;
    	if (list==null){
    		int index = playerId%redisClients.size();
			RedisClient jedisClient = redisClients.get(index);
			Jedis jedis = jedisClient.getJedis();
			String liststr = jedis.hget(MgrBase.DATAKEY_STOCK, String.valueOf(playerId));
			jedisClient.returnResource(jedis);    		
			if (liststr!=null){
				list = JSON.parseArray(liststr, Stock.class);
				//stocksMap.put(playerId, list);
			}
    	}	
    	return list;		
	}
    
	/**
	 * 获取玩家股票总金额
	 * @param int playerid
	 * @return float 总金额
	 * */
	public float getStockAmount(int playerid){
		List<Stock> stocks = getStockList(playerid);
		float stockamount = 0;
		if (stocks!=null){
			for (int i=0;i<stocks.size();i++){
				Stock ps = stocks.get(i);
				if (ps==null) continue;
				float currps = getCurrQuotePs(ps.getItemid());
				stockamount += currps*ps.getQty();
			}   			
		}
		return stockamount;
	}
	
	/**
	 * 取股票行情,每两个取一个
	 * @param int 股票id
	 * @return List<Quote> 行情列表
	 * */
    public synchronized List<Quote> getBigQuotes(int stockid){
    	LinkedList<Quote> details = quoteMap.get(stockid);
    	List<Quote> quotes = new ArrayList<Quote>();
    	for (int i=0;i<details.size();i+=2){
    		if (details.size()>i){
    			quotes.add(details.get(i));
    		}
    	}
    	return quotes;
    }
    
	/**
	 * 取股票最新行情价格
	 * @param int 股票id
	 * @return List<Quote> 行情列表
	 * */
    public boolean addStockTradeAmount(int playerid,float addedAmount){
    	boolean canAdded = false;
    	if (addedAmount<=0||playerid<=0)
    		return canAdded;
    	
       Calendar aCalendar = Calendar.getInstance();
       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
    	float maxAmount = 0;;
		int index = playerid%quoteClients.size();
		Jedis j3 = quoteClients.get(index).getJedis();
		String pkey = String.valueOf(playerid)+"_"+day1;
		String amountstr = j3.hget(MgrBase.DATAKEY_MAX_STOCK_AMOUNT,pkey);
		if (amountstr!=null){
			maxAmount = Float.valueOf(amountstr);
		}
		maxAmount += addedAmount;
		if (maxAmount<=super.maxStockAmount){
			canAdded = true;
			j3.hset(MgrBase.DATAKEY_MAX_STOCK_AMOUNT,pkey,String.valueOf(maxAmount));
		}
		quoteClients.get(index).returnResource(j3);    		
    	return canAdded;
    }
    
	/**
	 * 取股票最新行情
	 * @param int 股票id
	 * @return List<Quote> 行情列表
	 * */
    public synchronized List<Quote> getLastQuotes(int stockid){
    	List<Quote> quotes = new ArrayList<Quote>();
    	if (stockid>=0){
    		LinkedList<Quote> q = quoteMap.get(stockid);
    		if (q!=null&&q.size()>0){
       		 quotes.add(q.peekLast());
    		}
    	}
    	return quotes;
    }
    
	/**
	 * 交易市场是否开市
	 * @return boolean true表示开市, false表示休市
	 * */
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
			Jedis jedis = jedisClient3.getJedis();
			Pipeline p0 = jedis.pipelined();
			for (int i=0;i<stockData.size();i++){
	    		Stockdata  stock = stockData.get(i);
	 			LinkedList<Quote> lquote = quoteMap.get(stock.getId());
	 			if (lquote==null||lquote.size()==0) continue;
	 			Random r = new Random();
	 			
		    		float r2 = (float)r.nextInt(maxStockPer);
		    		float per = r2/100;
		    		int addOrMinus = r.nextInt(100);
		    		Quote quote = lquote.peekLast();
		    		float ps = quote.getPrice();
		    		//涨:
		    		if (addOrMinus<stockRaisePer)
		    			ps += ps*per;
		    		else
		    			ps -= ps*per;
		    		
		    		if (ps<0.1){
		    			ps = (float)0.1;
		    		}

					//假如前一天，取休市价，并清空行情价格
		    		if (isNewDay){
		    			//lquote.clear();
		    			log.debug("清空昨天价格");
		    		}
		    		
		    		//lquote.poll();	//不删除行情数据:
		    		
		    		if (lquote.size()>=300){
		    			lquote.removeFirst();
		    		}
		    		Quote newq = new Quote();
		    		newq.setPrice(ps);
		    		lquote.offer(newq);
	    			p0.hset(MgrBase.DATAKEY_CURRENT_STOCK_PS, String.valueOf(stock.getId()), JSON.toJSONString(ps));
//	    			log.warn("quotes "+lquote.size()+",str"+JSON.toJSONString(lquote));
//		    		System.out.println("股票价格变化: "+stock.getName()+",涨跌幅:"+per+",上一个价格:"+quote.getPrice()+",现价格:"+newq.getPrice());
		    }	
    		p0.sync();
    		jedisClient3.returnResource(jedis);
		}
		    	
		if (tick%STOCK_SAVE_PERIOD==0){
			Jedis jedis = jedisClient3.getJedis();
			Pipeline p = jedis.pipelined();
			for (int i=0;i<stockData.size();i++){
	    		Stockdata  stock = stockData.get(i);
	 			LinkedList<Quote> lquote = quoteMap.get(stock.getId());
	 			if (lquote==null||lquote.size()==0) continue;
	 			//只保留最新120个行情:
	 			LinkedList<Quote> quotes = new LinkedList<Quote>();
	 			int firstIndex = 0;
	 			if (lquote.size()>QUOTE_MAXLENGTH){
	 				firstIndex = lquote.size() - QUOTE_MAXLENGTH;
	 			}
	 			for (int j=firstIndex;j<lquote.size();j++){
		 			quotes.offer(lquote.get(j));
	 			}
	 			String strqu = JSON.toJSONString(quotes);
	 			stock.setQuotes(strqu.getBytes());
//	 			String ss = JSON.toJSONString(stock);
	 			//log.warn("update qutoes :"+quotes.size());
    			p.hset(MgrBase.DATAKEY_DATA_STOCK, String.valueOf(stock.getId()), JSON.toJSONString(stock));
	 			//stockDataThread.updateStockdata(stock);
			}
    		p.sync();
    		jedisClient3.returnResource(jedis);
			//log.warn("resave stock quotes data");
		}		
		//数据库保存:

	}
    
	/**
	 * 增加玩家股票
	 * @param int playerid
	 * @param Stock 对象
	 * @return int 0表示增加成功
	 * */
    public int addStock(int playerId,Stock record){
		List<Stock> list = getStockList(playerId);
		if (list==null){
			list = new ArrayList<Stock>();
			//stocksMap.put(playerId, list);
		}
		list.add(record);
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
		dataThread.updateStock(playerId, JSON.toJSONString(list));
		return RetMsg.MSG_OK;
	}

	/**
	 * 抛售玩家股票
	 * @param int playerid
	 * @param int 股票ID
	 * @param int 抛售数量
	 * @return Vector 股票剩余总额
	 * */
	public Vector<Float> deleteStock(int playerId,int stockId,int qty){
		List<Stock> list = getStockList(playerId);
		Vector<Float> reps = new Vector<Float>();
		if (list==null)
			return reps;
		
		float boughtPs = 0;
		int needRemoveQty = qty;
		boolean updated = false;
		for (int i=0;i<list.size();i++){
			Stock ss = list.get(i);
			if (ss.getItemid().intValue()!=stockId)
				continue;
			
			updated = true;
			boughtPs = ss.getPrice();
			
			if (ss.getQty()<=needRemoveQty){
				needRemoveQty -= ss.getQty();
				list.remove(i);
				i--;
				//LogMgr.getInstance().log(playerId,"delete stock:"+ss.getQty());
			}else {
				ss.setQty(ss.getQty()-needRemoveQty);
				ss.setAmount(ss.getQty()*ss.getPrice());
				//LogMgr.getInstance().log(playerId,"update stock:"+ss.getQty());
				needRemoveQty = 0;
			}
			if (needRemoveQty==0) {
				break;
			}
		}		
		float totalStockAmount = 0;
		if (updated){
			reps.add(boughtPs);
			for (Stock stock:list){
				totalStockAmount += stock.getQty()*stock.getPrice();
			}
			reps.add(totalStockAmount);
			DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
			dataThread.updateStock(playerId, JSON.toJSONString(list));
		}
		return reps;
	}

	/**
	 * 增加玩家股票
	 * @param int playerid
	 * @param int item id
	 * @param int 数量
	 * @param float 价格
	 * @param float 金额
	 * @return String 股票数据
	 * */
	public String add(int playerid,int itemid,int qty,float price,float amount,String sessionstr){
	   	StockAction action = new StockAction();
		Player p = DataManager.getInstance().findPlayer(playerid);
		if (p==null){
			return action.msgStr(RetMsg.MSG_PlayerNotExist);
		}    	
		long clientSessionid = 0;
		if (sessionstr!=null){
			clientSessionid = Long.valueOf(sessionstr);
		}
		long canSubmit = DataManager.getInstance().canSubmit(playerid, clientSessionid);
		if (canSubmit<=0){
			return action.msgStr((int)canSubmit);
		}

		Stock item = new Stock();
		item.setPlayerid(playerid);
		item.setItemid(itemid);
		item.setQty(qty);
		item.setAmount(amount);
		item.setPrice(price);
		//action.setStock(item);
		return action.add(canSubmit,item);
	}

	/**
	 * 取股票最新行情价格
	 * @param int 股票id
	 * @return List<Quote> 行情列表
	 * */
	public synchronized float getCurrQuotePs(int stockid){
		float currPs = 0;;
		if (stockid>=0){
			int index = stockid%quoteClients.size();
			Jedis j3 = quoteClients.get(index).getJedis();
			String psstr = j3.hget(MgrBase.DATAKEY_CURRENT_STOCK_PS,String.valueOf(stockid));
			if (psstr!=null){
				currPs = Float.valueOf(psstr);
			}else {
				LinkedList<Quote> q = quoteMap.get(stockid);
				Quote qs = q.peekLast();
				currPs = qs.getPrice();
				j3.hset(MgrBase.DATAKEY_CURRENT_STOCK_PS,String.valueOf(stockid),String.valueOf(currPs));
			}
			quoteClients.get(index).returnResource(j3);    		
		}
		 long   l1   =   Math.round(currPs*100);   //四舍五入  
		currPs = (float)(l1/100.0);
		return currPs;
	}

	public static void main(String[] args) {
			Random r = new Random();
	 	       Calendar aCalendar = Calendar.getInstance();
		       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
 			
    		float r2 = 0;
    		int addOrMinus = 0;
    		for (int i=0;i<200;i++){
        		addOrMinus = r.nextInt(100);	
        		r2 = (float)r.nextInt(5);
       		System.out.println(r2);
        		System.out.println(addOrMinus);   			
    		}

    }
}
