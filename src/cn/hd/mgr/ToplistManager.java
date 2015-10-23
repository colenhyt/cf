package cn.hd.mgr;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.ToplistService;
	
public class ToplistManager extends MgrBase{
	List<Toplist>		toplists;
    private static ToplistManager uniqueInstance = null;  
	
    public static ToplistManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new ToplistManager();  
        }  
        return uniqueInstance;  
     } 
    
    public void init(){
    	ToplistService service= new ToplistService();
    	toplists = service.findAll();
    	dataThread = new DataThread();
    	dataThread.start();    	
    	
    }
	public synchronized Toplist findByPlayerId(int playerId){
		for (int i=0;i<toplists.size();i++){
			Toplist top = toplists.get(i);
			if (top.getPlayerid().intValue()==playerId){
				return top;
			}
		}
		
		return null;
	}
	
	public synchronized List<Toplist> findCurrMonthToplists(){
		Date currMontDate = getFirstDate(1);
		List<Toplist> monthlist = new ArrayList<Toplist>();
		for (int i=0;i<toplists.size();i++){
			Toplist top = toplists.get(i);
			if (top.getUpdatetime().compareTo(currMontDate)>0){
				monthlist.add(top);
			}
		}
		Collections.sort(monthlist);
		return monthlist;
	}
	
	public int findCountByGreaterMoney(int playerid,int type,float fPMoney){
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
		for (int i=0;i<toplists.size();i++){
			Toplist top2 = toplists.get(i);
			int later = top2.getUpdatetime().compareTo(fristDate);
			if (top2.getMoney().floatValue()>=fMoney.floatValue()&&later>=0){
				cc++;
			}
		}
		return cc;
	}	
	
	public synchronized int add(Toplist record){	
		toplists.add(record);
		dataThread.pushToplist(record);
		return 0;
	}
	
	public synchronized int updateByKey(Toplist record){
		Toplist top = findByPlayerId(record.getPlayerid());
		if (top!=null){
			top.setMoney(record.getMoney());
			top.setUpdatetime(new Date());
			dataThread.updateToplist(top);
		}		
		return 0;
	}
	
	public synchronized int updateZan(Toplist toplist){
		System.out.println("update zan: "+(toplist==null));
		Toplist top = findByPlayerId(toplist.getPlayerid());
		if (top!=null){
			top.setZan(toplist.getZan());
			ToplistService service = new ToplistService();
			service.updateZan(top);
		}
		
		return 0;
	}
	
	public synchronized boolean updateToplist(int playerid,String playerName,double money){
		Toplist toplist = findByPlayerId(playerid);
		if (toplist==null){
			Toplist newtop = new Toplist();
			newtop.setPlayerid(playerid);
			newtop.setPlayername(playerName);
			newtop.setCreatetime(new Date());
			newtop.setUpdatetime(new Date());
			newtop.setMoney(BigDecimal.valueOf(money));
			newtop.setZan(0);
			add(newtop);				
			//System.out.println("增加最新排行榜记录: "+newtop.getPlayername()+":"+newtop.getMoney());
		}else {
			double topMoney = toplist.getMoney().doubleValue();
			if (Math.abs(money-topMoney)>0.01){
				toplist.setMoney(BigDecimal.valueOf(money));
				toplist.setUpdatetime(new Date());
				updateByKey(toplist);
				//System.out.println("更新排行榜财富: "+toplist.getPlayername()+":"+topMoney+","+toplist.getMoney());
			}
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
		for (int i=0;i<toplists.size();i++){
			Toplist top = toplists.get(i);
			if (top.getUpdatetime().compareTo(firstDate)>0){
				list.add(top);
			}
		}
		Collections.sort(list);		
		for (int i=0;i<list.size();i++){
			if (i>=20) break;
			tops.add(list.get(i));
		}
		//System.out.println("取排行榜(type:"+type+"):开始时间:"+firstDate.toString()+",记录数:"+tops.size());
		return tops;
	}

	public static void main(String[] args){
		Toplist record = new Toplist();
		record.setPlayerid(280);
		record.setZan(3);
	}

}