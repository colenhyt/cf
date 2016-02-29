package cn.hd.mgr;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import redis.clients.jedis.Jedis;
import cn.hd.cf.action.ToplistAction;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;
	
public class ToplistManager extends MgrBase{
	Map<Integer,Toplist>		toplistMap;
	List<Toplist>				currWeekToplist;
	List<Toplist>				currMonthToplist;
	List<Toplist>				topWeekToplist;
	List<Toplist>				topMonthToplist;
	String						topWeekToplistStr;
	String						topMonthToplistStr;
	ToplistAction				topAction;
	final int					TOPLIST_MAX_COUNT = 4000;
    private static ToplistManager uniqueInstance = null;  
    private long lastLoadTime = 0;
    private RedisClient jedisClient;
	
    public static ToplistManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new ToplistManager();  
        }  
        return uniqueInstance;  
     } 
    
    public void init(){
    	topAction = new ToplistAction();
    	
    	toplistMap = Collections.synchronizedMap(new HashMap<Integer,Toplist>());
		
		jedisClient = new RedisClient(redisCfg2);
		
		dataThreads = new Vector<DataThread>();
		 for (int i=0;i<redisCfg2.getThreadCount();i++){
			 DataThread dataThread = new DataThread(redisCfg2);
			 dataThread.setUpdateDuration(100);
			dataThreads.add(dataThread);
			dataThread.start();
		 }	    	
   	
    	Jedis jedis = jedisClient.getJedis();   	
		if (jedis==null){
			log.error("could not get toplist redis,redis2 may not be run");
			return;
		}
		jedisClient.returnResource(jedis);
    	log.warn("toplist init,reloadtime:"+toplistTime+"s");
    	
		currWeekToplist = new ArrayList<Toplist>();
		currMonthToplist = new ArrayList<Toplist>();
		topWeekToplist = new ArrayList<Toplist>();
		topMonthToplist = new ArrayList<Toplist>();
    	load();
    }
    
    public synchronized void load(){
    	boolean isNeedReloaded = false;
    	long topTime = toplistTime*1000;
    	if (lastLoadTime==0||(System.currentTimeMillis()-lastLoadTime>=topTime)){
    		lastLoadTime = System.currentTimeMillis();
    		isNeedReloaded = true;
    	}
    	
    	if (!isNeedReloaded) return;
    	
    	toplistMap.clear();
    	
    	Jedis jedis = jedisClient.getJedis();   	
		
    	List<String> itemstrs = jedis.hvals(MgrBase.DATAKEY_TOPLIST);
    	jedisClient.returnResource(jedis);
    	for (String str:itemstrs){
    		Toplist item = (Toplist)JSON.parseObject(str, Toplist.class);
    		if (!toplistMap.containsKey(item.getPlayerid()))
    			toplistMap.put(item.getPlayerid(), item);
    	}
    	
    	log.warn("reload all toplist data:" + itemstrs.size());      
    	
    	
    	//reset week and month toplist:
		Date firstWeekDate = getFirstDate(0,new Date());
		Date firstMonthDate = getFirstDate(1,new Date());
		List<Toplist>  allweeklist = new ArrayList<Toplist>();
		List<Toplist>  allmonthlist = new ArrayList<Toplist>();
		Collection<Toplist> toplists = toplistMap.values();
		for (Toplist top:toplists){
			if (top.getUpdatetime().compareTo(firstWeekDate)>0){
				allweeklist.add(top);
			}
			if (top.getUpdatetime().compareTo(firstMonthDate)>0){
				allmonthlist.add(top);
			}
		}
		Collections.sort((List<Toplist>)allweeklist);
		currWeekToplist.clear();
		topWeekToplist.clear();
		for (int i=0;i<TOPLIST_MAX_COUNT;i++){
			if (i>=allweeklist.size())break;
			currWeekToplist.add(allweeklist.get(i));
		}
		for (int i=0;i<currWeekToplist.size();i++){
			if (i>=100) break;
			topWeekToplist.add(currWeekToplist.get(i));
		}
		topWeekToplistStr = buildTopsStr(topWeekToplist);
		
		Collections.sort((List<Toplist>)allmonthlist);    	
		currMonthToplist.clear();
		topMonthToplist.clear();
		for (int i=0;i<TOPLIST_MAX_COUNT;i++){
			if (i>=allmonthlist.size())break;
			currMonthToplist.add(allmonthlist.get(i));
		}
		for (int i=0;i<currMonthToplist.size();i++){
			if (i>=100) break;
			topMonthToplist.add(currMonthToplist.get(i));
		}
		topMonthToplistStr = buildTopsStr(topMonthToplist);
		
    	log.warn("reset week and month toplist,week:" + currWeekToplist.size()+",month:"+currMonthToplist.size());      
    }
    
	public synchronized Toplist findByPlayerId(int playerid){		
		Toplist item = toplistMap.get(playerid);
		if (item==null){
			Jedis jedis = jedisClient.getJedis();
			String itemstr = jedis.hget(MgrBase.DATAKEY_TOPLIST, String.valueOf(playerid));
			jedisClient.returnResource(jedis);			
			if (itemstr!=null){
				item = (Toplist)JSON.parseObject(itemstr, Toplist.class);
				toplistMap.put(playerid, item);
			}
		}		
		return item;
	}
	
	private int getIndex(int type,float fPMoney){
		List<Toplist> list = currWeekToplist;
		if (type==1)
			list = currMonthToplist;
		
		//小于最小:
		if (list.get(list.size()-1).getMoney().floatValue()>fPMoney) 
			return -1;
		
		int top = -1;
		int left,right,middle;
		left = 0;
		right = list.size()-1;
		while (right>=left){
			middle = (left+right)/2;
			if (fPMoney<list.get(middle).getMoney().floatValue()){
				left = middle+1;
			}else
				right = middle -1;
		}
		return top;
	}
	
	/**
	 * 根据财富值取得排名
	 * @param Toplist对象
	 * @param int 类型
	 * @param float 财富值
	 * @return int 排名
	 * */
	public synchronized int findTopCount(Toplist top,int type,float fPMoney){
		int intMoney = Float.valueOf(fPMoney).intValue();
		BigDecimal fMoney = BigDecimal.valueOf(intMoney);
		if (top!=null){
			fMoney = top.getMoney();
			Calendar cl = Calendar.getInstance();
			Date now = new Date();
			cl.setTime(now);
			if (type==0){
				int week = cl.get(Calendar.WEEK_OF_YEAR);
				cl.setTime(top.getUpdatetime());
				int hisweek = cl.get(Calendar.WEEK_OF_YEAR);
				if (week!=hisweek)
					return -1;
			}else {
				int month = cl.get(Calendar.MONTH);
				cl.setTime(top.getUpdatetime());
				int hismonth = cl.get(Calendar.MONTH);
				if (month!=hismonth)
					return -1;
			}
		}
		
		List<Toplist> list = currWeekToplist;
		if (type==1)
			list = currMonthToplist;
		
		int cc = list.size();
		for (int i=0;i<list.size();i++){
			Toplist item = list.get(i);
			float itemValue = item.getMoney().floatValue();
			if (itemValue>fPMoney) continue;
			cc = i;
			break;
		}
		return cc;
	}	
	
	/**
	 * 玩家排名点赞
	 * @param Toplist对象
	 * @return int 0表示点赞成功
	 * */
	public synchronized int updateZan(Toplist toplist){
		Toplist top = findByPlayerId(toplist.getPlayerid());
		if (top!=null){
			top.setZan(toplist.getZan());
			DataThread dataThread = dataThreads.get(toplist.getPlayerid()%dataThreads.size());
			dataThread.updateToplist(top);
			
			boolean update = false;
			for (Toplist item:topWeekToplist){
				if (item.getPlayerid().intValue()==toplist.getPlayerid().intValue()){
					item.setZan(toplist.getZan());
					update = true;
					break;
				}
			}
			if (update)
				topWeekToplistStr = buildTopsStr(topWeekToplist);
			
			update = false;
			for (Toplist item:topMonthToplist){
				if (item.getPlayerid().intValue()==toplist.getPlayerid().intValue()){
					item.setZan(toplist.getZan());
					update = true;
					break;
				}
			}
			if (update)
				topMonthToplistStr = buildTopsStr(topMonthToplist);
			
		}
		
		return 0;
	}
	
	private synchronized String buildTopsStr(List<Toplist> list){
		String itemStr = "";
		for (Toplist item:list){
			itemStr += "["+item.getPlayerid()+",'"+item.getPlayername()+"',"+item.getMoney().intValue()+","+item.getZan().intValue()+"],";
		}
		return itemStr;
	}
	
	/**
	 * 新注册用户增加排名
	 * @param int playerid
	 * @param String 玩家昵称
	 * @param double 财富值
	 * @return boolean true表示增加成功
	 * */
	public synchronized boolean addRegisterToplist(int playerid,String playerName,double money){
			Toplist newtop = new Toplist();
			newtop.setPlayerid(playerid);
			String pName = playerName;
			if (pName==null){
				Player p = DataManager.getInstance().findPlayer(playerid);
				if (p!=null){
					pName = p.getPlayername();
				}
			}
			newtop.setPlayername(pName);
			newtop.setCreatetime(new Date());
			newtop.setUpdatetime(new Date());
			int imoney = Double.valueOf(money).intValue();
			newtop.setMoney(BigDecimal.valueOf(imoney));
			newtop.setZan(0);
			toplistMap.put(newtop.getPlayerid(), newtop);
			DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
			dataThread.updateToplist(newtop);		
		return true;		
	}
	
	/**
	 * 新户增加排名
	 * @param int playerid
	 * @param String 玩家昵称
	 * @param double 财富值
	 * @return boolean true表示增加成功
	 * */
	public synchronized boolean addToplist(int playerid,String playerName,double money){
		Toplist newtop = findByPlayerId(playerid);
		if (newtop==null) {
			return addRegisterToplist(playerid,playerName,money);	
		}
		return true;		
	}

	private Date getFirstDate(int type,Date startDate){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date today = startDate;
		Calendar cl = Calendar. getInstance();
		cl.setTime(today);
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH)+1;
		int day = cl.get(Calendar.DAY_OF_MONTH);
		String strMonthFirstDay = year+"-"+month+"-01";
		String strTodayDay = year+"-"+month+"-"+day;
		Date firstDate = null;
		try {
			firstDate = sdf.parse(strMonthFirstDay);
			today = sdf.parse(strTodayDay);
			cl.setTime(today);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//week
		if (type==0){
			int weekday = cl.get(Calendar.DAY_OF_WEEK);
			if (weekday==Calendar.SUNDAY)
				weekday = 6;
			else
				weekday -= 2;
			cl.add(Calendar.DAY_OF_YEAR, 0-weekday);
			Date weekDate=cl.getTime();
			int after = weekDate.compareTo(firstDate);
			if (after>0)
				firstDate = weekDate;
		}		
		return firstDate;
	}
	
	public synchronized List<Toplist> getTopItems(String startMonth,boolean isFirst){
		List<Toplist> tops = new ArrayList<Toplist>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		if (startMonth!=null&&startMonth.length()>0){
			try {
				startDate = sdf.parse(startMonth);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		Date firstDate = startDate;
		if (!isFirst)
			getFirstDate(1,startDate);
		
		load();
		List<Toplist> list = new ArrayList<Toplist>();
		Collection<Toplist> toplists = toplistMap.values();
		for (Toplist top:toplists){
			if (top.getUpdatetime().compareTo(firstDate)>0){
				list.add(top);
			}
		}
		Collections.sort((List<Toplist>)list);
		for (int i=0;i<list.size();i++){
			tops.add(list.get(i));
		}		
		return tops;
	}
	
	/**
	 * 获取某个玩家排名
	 * @param Toplist对象
	 * @param int 类型
	 * @return int 排名
	 * */
	public int getTopNumber(int playerid,int type){
		Toplist top = findByPlayerId(playerid);
		float fPMoney = 0;
		if (top==null){
			fPMoney = calculatePlayerMoney(playerid);
		}else
			fPMoney = top.getMoney().floatValue();
		
		return findTopCount(top,type,fPMoney);			

	}
	
	public long getTopCount(){
    	Jedis jedis = jedisClient.getJedis();   	
		
    	long count = jedis.hlen(MgrBase.DATAKEY_TOPLIST);
    	jedisClient.returnResource(jedis);		
		return count;
	}
	
	/**
	 * 获取排名数据
	 * @param int 数量
	 * @return String json排名数据
	 * */
	public synchronized String getActivityList(int count){
		
    	Jedis jedis = jedisClient.getJedis();   	
		
    	List<String> itemstrs = jedis.hvals(MgrBase.DATAKEY_TOPLIST);
    	jedisClient.returnResource(jedis);
		List<Toplist> list = new ArrayList<Toplist>();
    	for (String str:itemstrs){
    		Toplist item = (Toplist)JSON.parseObject(str, Toplist.class);
			Player p = DataManager.getInstance().findPlayer(item.getPlayerid());
			if (p!=null)
				item.setOpenid(p.getOpenid());
			list.add(item);
    	}
    	log.warn("get all toplist data:" + itemstrs.size()); 
		Collections.sort((List<Toplist>)list);
 
		List<Toplist> tops = new ArrayList<Toplist>();
		for (int i=0;i<list.size();i++){
			if (i>=count) break;
			tops.add(list.get(i));
		}		
    	
		return JSON.toJSONString(tops);
	}
	public synchronized List<Toplist> getAlltoplist(int count){
		List<Toplist> tops = new ArrayList<Toplist>();
		
		List<Toplist> list = new ArrayList<Toplist>();
		Collection<Toplist> toplists = toplistMap.values();
		for (Toplist top:toplists){
				list.add(top);
		}
		Collections.sort((List<Toplist>)list);
		for (int i=0;i<list.size();i++){
			if (i>=count) break;
			tops.add(list.get(i));
		}
		log.warn("get all toplist,count:"+tops.size());
		return tops;
	}

	/**
	 * 更新排行榜排名
	 * @param int playerid
	 * @param String 玩家昵称
	 * @param float 财富值
	 * @return boolean true表示更新成功
	 * */
	public synchronized boolean updateToplist(int playerid,String playerName,double money){
		Toplist toplist = findByPlayerId(playerid);
		int imoney = Double.valueOf(money).intValue();
		if (toplist==null){
			addToplist(playerid,playerName,imoney);				
			//System.out.println("增加最新排行榜记录: "+newtop.getPlayername()+":"+newtop.getMoney());
		}else {
			double topMoney = toplist.getMoney().doubleValue();
			if (Math.abs(imoney-topMoney)>1){
				toplist.setMoney(BigDecimal.valueOf(imoney));
				toplist.setUpdatetime(new Date());
				DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
				dataThread.updateToplist(toplist);
				toplistMap.put(playerid, toplist);
//				//System.out.println("更新排行榜财富: "+toplist.getPlayername()+":"+topMoney+","+toplist.getMoney());
			}
		}
		return true;		
	}
	
	public synchronized String list(int playerid,int type){
		Toplist toplist = new Toplist();
		toplist.setPlayerid(playerid);
		topAction.setToplist(toplist);	
			return topAction.list();
		}
	
	public synchronized float getCurrentTotalMoney(int playerId,float totalSaving){
		float savingamount = Float.valueOf(totalSaving).intValue();
		
		float insureamount = InsureManager.getInstance().getInsureAmount(playerId);

		float amount = savingamount + insureamount + StockManager.getInstance().getStockAmount(playerId);
		return amount;
	}
	
	public synchronized float calculatePlayerMoney(int playerId){
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerId);
		float savingamount = 0;
		if (savings!=null){
			for (int i=0;i<savings.size();i++){
				savingamount += savings.get(i).getAmount();
			}			
		}
		savingamount = Float.valueOf(savingamount).intValue();
		float insureamount = InsureManager.getInstance().getInsureAmount(playerId);

		float amount = savingamount + insureamount + StockManager.getInstance().getStockAmount(playerId);
		return amount;
	}
	
	public synchronized int findCountByGreaterMoney(int playerid,int type,float fPMoney){
		Toplist top = findByPlayerId(playerid);
		return findTopCount(top,type,fPMoney);
	}

	/**
	 * 取排名数据
	 * @param int 类型，表示当周还是当月
	 * @return List<Toplist> 排名列表
	 * */
	public synchronized String findByType(int type){
		
		if (type==0)
			return topWeekToplistStr;
		
		return topMonthToplistStr;
	}

	public static void main(String[] args){
//		ToplistManager.getInstance().init();
		Date d = new Date(1456047321192L);
		List<Float> list = new ArrayList<Float>();
		list.add((float)100.0);
		list.add((float)90.0);
		list.add((float)78.0);
		list.add((float)65.0);
		list.add((float)56.0);
		list.add((float)55.0);
		list.add((float)34.0);
		list.add((float)30.0);
		int left,right,middle;
		left = 0;
		float fPMoney = (float)57;
		right = list.size()-1;
		int index = 0;
		while (right>=left){
			middle = (left+right)/2;
			float mv = list.get(middle).floatValue();
			if (fPMoney<mv){
				left = middle+1;
			}else if (fPMoney>mv)
				right = middle -1;
			
			float leftV = list.get(left).floatValue();
			float rightV = list.get(right).floatValue();
			if (leftV>=fPMoney&&fPMoney>=rightV){
				if (leftV==fPMoney)
					index = left;
				else
					index = left+1;
				break;
			}
		}
		System.out.println(index);
		right = 3;
//		ToplistManager.getInstance().findByType(0);
//		Toplist record = new Toplist();
//		record.setPlayerid(280);
//		record.setZan(3);
//		record.setMoney(BigDecimal.valueOf(52300));
//		ToplistManager.getInstance().add(record);
	}

}
