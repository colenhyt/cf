package cn.hd.cf.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.ToplistMapper;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.model.ToplistExample;
import cn.hd.cf.model.ToplistExample.Criteria;

public class ToplistService extends BaseService {
	private ToplistMapper toplistMapper;
	
	public Toplist findByPlayerId(int playerId){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andPlayeridEqualTo(playerId);
		List<Toplist> list = toplistMapper.selectByExample(example);
		if (list.size()>0)
			return list.get(0);
		
		return null;
	}
	
	public boolean removeCurrWeekdata(int week){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();	
		criteria.andWeekEqualTo(week);
		toplistMapper.deleteByExample(example);
		DBCommit();
		return true;
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
		
		Date fristDate = getFirstDate(type);
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(0);
		criteria.andMoneyGreaterThan(fMoney);
		criteria.andUpdatetimeGreaterThan(fristDate);
		int cc = toplistMapper.countByExample(example);
		System.out.println("zhaodao :"+fMoney+":"+cc);
		return cc;
	}	
	
	public int add(Toplist record){
		toplistMapper.insert(record);
		DBCommit();
		return 0;
	}
	
	public int remove(int id){
		toplistMapper.deleteByPrimaryKey(Integer.valueOf(id));
		DBCommit();
		return 0;
	}
	
	public int updateByKey(Toplist record){
		toplistMapper.updateByPrimaryKey(record);
		DBCommit();
		return 0;
	}
	
	public int updateZan(Toplist toplist){
		System.out.println("xxxxx "+(toplist==null));
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();	
		criteria.andTypeIsNotNull();
		criteria.andPlayeridEqualTo(toplist.getPlayerid());
		Toplist toplist2 = new Toplist();
		toplist2.setZan(toplist.getZan());
		toplistMapper.updateByExampleSelective(toplist2, example);
		System.out.println("更新赞:"+toplist.getZan());
		DBCommit();
		return 0;
	}
	
	public List<Toplist> findAll(){
		ToplistExample example=new ToplistExample();
		example.setOrderByClause("money desc");
		List<Toplist> list = toplistMapper.selectByExample(example);
		return list;
	}
	
	public ToplistService(){
		initMapper("toplistMapper");
	}
	
	public ToplistMapper getToplistMapper() {
		return toplistMapper;
	}

	public void setToplistMapper(ToplistMapper toplistMapper) {
		this.toplistMapper = toplistMapper;
	}

	public boolean updateToplist(int playerid,String playerName,double money){
		Toplist toplist = findByPlayerId(playerid);
		if (toplist==null){
			Toplist newtop = new Toplist();
			newtop.setPlayerid(playerid);
			newtop.setPlayername(playerName);
			newtop.setCreatetime(new Date());
			newtop.setUpdatetime(new Date());
			newtop.setMoney(BigDecimal.valueOf(money));
			newtop.setType(0);
			newtop.setWeek(-1);
			newtop.setZan(0);
			add(newtop);				
			System.out.println("增加最新排行榜记录: "+newtop.getPlayername()+":"+newtop.getMoney());
		}else {
			double topMoney = toplist.getMoney().doubleValue();
			if (Math.abs(money-topMoney)>0.01){
				toplist.setMoney(BigDecimal.valueOf(money));
				toplist.setUpdatetime(new Date());
				updateByKey(toplist);
				System.out.println("更新排行榜财富: "+toplist.getPlayername()+":"+topMoney+","+toplist.getMoney());
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
			int diff = weekDate.compareTo(firstDate);
			if (diff>0)
				firstDate = weekDate;
		}		
		return firstDate;
	}
	
	public List<Toplist> findByType(int type){
		Date firstDate = getFirstDate(type);
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(0);
		criteria.andUpdatetimeGreaterThan(firstDate);
		 example.setOrderByClause("money desc");
		List<Toplist> list = toplistMapper.selectByExample(example);
		List<Toplist> tops = new ArrayList<Toplist>();
		for (int i=0;i<list.size();i++){
			if (i>=20) break;
			tops.add(list.get(i));
		}
		System.out.println("取排行榜(type:"+type+"):开始时间:"+firstDate.toString()+",记录数:"+tops.size());
		return tops;
	}
}
