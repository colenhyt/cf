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

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;
import cn.hd.cf.action.ToplistAction;
import cn.hd.cf.model.Toplist;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;
	
public class ToplistManager extends MgrBase{
	Map<Integer,Toplist>		toplistMap;
	ToplistAction				topAction;
    private static ToplistManager uniqueInstance = null;  
	
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
    	log.warn("toplist init");
    }
    
    public synchronized void load(){
    	toplistMap.clear();
    	
    	Jedis jedis = jedisClient.getJedis();   	
		
    	List<String> itemstrs = jedis.hvals(super.DATAKEY_TOPLIST);
    	jedisClient.returnResource(jedis);
    	for (String str:itemstrs){
    		Toplist item = (Toplist)JSON.parseObject(str, Toplist.class);
    		if (!toplistMap.containsKey(item.getPlayerid()))
    			toplistMap.put(item.getPlayerid(), item);
    	}
//    	log.warn("load toplist :" + itemstrs.size());      	
    }
    
	public synchronized Toplist findByPlayerId(int playerid){		
		Toplist item = toplistMap.get(playerid);
		if (item==null){
			Jedis jedis = jedisClient.getJedis();
			String itemstr = jedis.hget(super.DATAKEY_TOPLIST, String.valueOf(playerid));
			jedisClient.returnResource(jedis);			
			if (itemstr!=null){
				item = (Toplist)JSON.parseObject(itemstr, Toplist.class);
				toplistMap.put(playerid, item);
			}
		}		
		return item;
	}
	
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
		
		int cc = 0;
		Date fristDate = getFirstDate(type,new Date());
		Collection<Toplist> toplists = toplistMap.values();
		for (Toplist top2:toplists){
			int later = top2.getUpdatetime().compareTo(fristDate);
			if (top2.getMoney().floatValue()>fMoney.floatValue()&&later>0){
				cc++;
			}
		}
		return cc;
	}	
	
	public synchronized int updateZan(Toplist toplist){
		System.out.println("update zan: "+(toplist==null));
		Toplist top = findByPlayerId(toplist.getPlayerid());
		if (top!=null){
			top.setZan(toplist.getZan());
			DataThread dataThread = dataThreads.get(toplist.getPlayerid()%dataThreads.size());
			dataThread.updateToplist(top);
		}
		
		return 0;
	}
	
	
	public synchronized boolean addRegisterToplist(int playerid,String playerName,double money){
			Toplist newtop = new Toplist();
			newtop.setPlayerid(playerid);
			newtop.setPlayername(playerName);
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
	
	public long getTopCount(){
    	Jedis jedis = jedisClient.getJedis();   	
		
    	long count = jedis.hlen(MgrBase.DATAKEY_TOPLIST);
    	jedisClient.returnResource(jedis);		
		return count;
	}
	
	public synchronized List<Toplist> findByType(int type){
		List<Toplist> tops = new ArrayList<Toplist>();
		
		Date firstDate = getFirstDate(type,new Date());
		
		List<Toplist> list = new ArrayList<Toplist>();
		Collection<Toplist> toplists = toplistMap.values();
		for (Toplist top:toplists){
			if (top.getUpdatetime().compareTo(firstDate)>0){
				list.add(top);
			}
		}
		Collections.sort((List<Toplist>)list);
		for (int i=0;i<list.size();i++){
			if (i>=100) break;
			tops.add(list.get(i));
		}
		//System.out.println("取排行榜(type:"+type+"):开始时间:"+firstDate.toString()+",记录数:"+tops.size());
		return tops;
	}

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
//				//System.out.println("更新排行榜财富: "+toplist.getPlayername()+":"+topMoney+","+toplist.getMoney());
			}
		}
		return true;		
	}
	
	public String list(int playerid,int type){
		Toplist toplist = new Toplist();
		toplist.setPlayerid(playerid);
		topAction.setToplist(toplist);	
			return topAction.list();
		}

	public synchronized int findCountByGreaterMoney(int playerid,int type,float fPMoney){
		Toplist top = findByPlayerId(playerid);
		return findTopCount(top,type,fPMoney);
	}

	public static void main(String[] args){
		ToplistManager.getInstance().init();
		double a = 3.91948171;
		int ia = Double.valueOf(a).intValue();
		long st = System.currentTimeMillis();
		String str = ToplistManager.getInstance().list(0,0);
		System.out.println(str);
		System.out.println("cost time: "+(System.currentTimeMillis()-st)+"ms");
//		ToplistManager.getInstance().findByType(0);
//		Toplist record = new Toplist();
//		record.setPlayerid(280);
//		record.setZan(3);
//		record.setMoney(BigDecimal.valueOf(52300));
//		ToplistManager.getInstance().add(record);
	}

}
