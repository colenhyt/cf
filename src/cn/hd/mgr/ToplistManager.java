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

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;
import cn.hd.cf.action.ToplistAction;
import cn.hd.cf.model.Toplist;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;
	
public class ToplistManager extends MgrBase{
	Map<Integer,Toplist>		toplistMsp;
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
    	
    	toplistMsp = Collections.synchronizedMap(new HashMap<Integer,Toplist>());
   	
    	Jedis jedis = jedisClient.getJedis();   	
    	List<String> itemstrs = jedis.hvals(super.DATAKEY_TOPLIST);
    	for (String str:itemstrs){
    		Toplist item = (Toplist)JSON.parseObject(str, Toplist.class);
//    		log.warn("get toplist:"+str);
    		toplistMsp.put(item.getPlayerid(), item);
    	}
    	jedisClient.returnResource(jedis);
    	log.warn("load toplist :" + itemstrs.size());    	
    	
    }
	public synchronized Toplist findByPlayerId(int playerId){		
		return toplistMsp.get(playerId);
	}
	
	public synchronized int findCountByGreaterMoney(int playerid,int type,float fPMoney){
		Toplist top = findByPlayerId(playerid);
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
		Date fristDate = getFirstDate(type);
		Collection<Toplist> toplists = toplistMsp.values();
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
			dataThread.updateToplist(top);
		}
		
		return 0;
	}
	
	public synchronized boolean addToplist(int playerid,String playerName,double money){
		Toplist newtop = toplistMsp.get(playerid);
		if (newtop==null) {
			newtop = new Toplist();
			newtop.setPlayerid(playerid);
			newtop.setPlayername(playerName);
			newtop.setCreatetime(new Date());
			newtop.setUpdatetime(new Date());
			newtop.setMoney(BigDecimal.valueOf(money));
			newtop.setZan(0);
			toplistMsp.put(newtop.getPlayerid(), newtop);
			dataThread.updateToplist(newtop);		
		}
		return true;		
	}

	private Date getFirstDate(int type){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
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
	
	public synchronized List<Toplist> findByType(int type){
		List<Toplist> tops = new ArrayList<Toplist>();
		
		Date firstDate = getFirstDate(type);
		
		List<Toplist> list = new ArrayList<Toplist>();
		Collection<Toplist> toplists = toplistMsp.values();
		for (Toplist top:toplists){
			if (top.getUpdatetime().compareTo(firstDate)>0){
				list.add(top);
			}
		}
		Collections.sort((List<Toplist>)list);
		for (int i=0;i<list.size();i++){
			if (i>=20) break;
			tops.add(list.get(i));
		}
		//System.out.println("取排行榜(type:"+type+"):开始时间:"+firstDate.toString()+",记录数:"+tops.size());
		return tops;
	}

	public synchronized boolean updateToplist(int playerid,String playerName,double money){
		Toplist toplist = findByPlayerId(playerid);
		if (toplist==null){
			addToplist(playerid,playerName,money);				
			//System.out.println("增加最新排行榜记录: "+newtop.getPlayername()+":"+newtop.getMoney());
		}else {
			double topMoney = toplist.getMoney().doubleValue();
			if (Math.abs(money-topMoney)>1){
				toplist.setMoney(BigDecimal.valueOf(money));
				toplist.setUpdatetime(new Date());
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

	public static void main(String[] args){
		ToplistManager.getInstance().init();
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
