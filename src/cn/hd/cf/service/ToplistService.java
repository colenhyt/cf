package cn.hd.cf.service;

import java.math.BigDecimal;
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
	
	public List<Toplist> findByType(int type){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(1);
		Date now = new Date();
      Calendar cl = Calendar. getInstance();
      cl.setTime(now);
      int currDay = cl.get(Calendar.DAY_OF_YEAR);
      int currWeek = currDay/7;
      if (currDay%7!=0)
    	  currWeek++;
	    //本周:
		if (type==0){
			criteria.andWeekEqualTo(currWeek);
			System.out.println(type+"取当周排行榜:"+currWeek);
		}
		//取当月:
		else if (type==1){
		      Calendar c2 = Calendar. getInstance();
		      c2.set(now.getYear(), now.getMonth(), 1);
		      int firstMonthDay = c2.get(Calendar.DAY_OF_YEAR);
		      int startWeek = firstMonthDay/7;
		      if (firstMonthDay%7!=0)
		    	  startWeek++;
		      criteria.andWeekGreaterThanOrEqualTo(startWeek);
		      criteria.andWeekLessThanOrEqualTo(currWeek);
				System.out.println(type+"取月排行榜:"+startWeek+",end:"+currWeek);
		}
		 example.setOrderByClause("money desc");
		List<Toplist> list = toplistMapper.selectByExample(example);
		return list;
	}
	
	public Toplist findByPlayerId(int playerId,int type){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andPlayeridEqualTo(playerId);
		criteria.andTypeEqualTo(type);
		List<Toplist> list = toplistMapper.selectByExample(example);
		if (list.size()>0)
			return list.get(0);
		
		return null;
	}
	
	public List<Toplist> findCurrWeekToplist(){
		Date now = new Date();
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(0);
		Calendar cal=Calendar.getInstance();
		cal.setTime(now);
		int currWeek = cal.get(Calendar.WEEK_OF_YEAR);
		List<Toplist> list = toplistMapper.selectByExample(example);
		List<Toplist> currList = new ArrayList<Toplist>();
		for (int i=0;i<list.size();i++){
			Toplist item = list.get(i);
			cal.setTime(item.getUpdatetime());
			int updateWeek = cal.get(Calendar.WEEK_OF_YEAR);
			if (updateWeek==currWeek){
				currList.add(item);
			}
		}
		
		 Collections.sort(currList, new Comparator<Toplist>() {
	            public int compare(Toplist arg0, Toplist arg1) {
	            	if (arg0.getMoney().doubleValue()>arg1.getMoney().doubleValue())
	            		return -1;
	            	else
	            		return 1;
	            }
	        });
		return currList;
	}
	
	private Toplist findByPlayerIdAndTypeAndWeek(int playerId,int type,int week){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		if (type>=0)
			criteria.andTypeEqualTo(type);
		if (week>0)
			criteria.andWeekEqualTo(week);
		List<Toplist> list = toplistMapper.selectByExample(example);
		if (list.size()>0)
			return list.get(0);
		return null;
	}	
	
	public Toplist findByLessMoney(double fMoney,int week){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andMoneyLessThan(BigDecimal.valueOf(fMoney));
		if (week>0)
			criteria.andWeekEqualTo(week);
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
	
	public int findCount(int type,int week){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(type);
		if (week>0)
			criteria.andWeekEqualTo(week);
		return toplistMapper.countByExample(example);
	}	
	
	public int findCountByGreaterMoney(int type, int playerid){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(type);
		BigDecimal fMoney = BigDecimal.valueOf(0);
		Toplist top = findByPlayerId(playerid,type);
		if (top!=null)
			fMoney = top.getMoney();
		criteria.andMoneyGreaterThan(fMoney);
		int cc = toplistMapper.countByExample(example);
		System.out.println("zhaodao :"+fMoney+":"+cc);
		return cc;
	}	
	
	public int add(Toplist record){
		toplistMapper.insert(record);
		DBCommit();
		return 0;
	}
	
	private boolean updateWeekData(PlayerWithBLOBs playerBlob,double money,int type,int week,int keepCount){
		Toplist toplist = findByPlayerIdAndTypeAndWeek(playerBlob.getPlayerid(),type,week);
		if (toplist==null){
			Toplist newtop = new Toplist();
			newtop.setPlayerid(playerBlob.getPlayerid());
			newtop.setPlayername(playerBlob.getPlayername());
			newtop.setCreatetime(new Date());
			newtop.setUpdatetime(new Date());
			newtop.setMoney(BigDecimal.valueOf(money));
			newtop.setType(type);
			newtop.setWeek(week);
			newtop.setZan(0);
			add(newtop);				
			System.out.println("增加排行榜记录: "+newtop.getPlayername()+":"+newtop.getMoney());
		}else {
			double topMoney = toplist.getMoney().doubleValue();
			if ((money-topMoney)>0.01){
				toplist.setMoney(BigDecimal.valueOf(money));
				toplist.setUpdatetime(new Date());
				updateByKey(toplist);
				System.out.println("更新排行榜财富: "+toplist.getPlayername()+":"+topMoney+","+toplist.getMoney());
			}
		}
		return true;		
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
		System.out.println("xxxxx222222 "+(toplist==null));
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

	public boolean updateCurrData(PlayerWithBLOBs playerBlob,double money){
		Toplist toplist = findByPlayerIdAndTypeAndWeek(playerBlob.getPlayerid(),0,-1);
		if (toplist==null){
			Toplist newtop = new Toplist();
			newtop.setPlayerid(playerBlob.getPlayerid());
			newtop.setPlayername(playerBlob.getPlayername());
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
			if ((money-topMoney)>0.01){
				toplist.setMoney(BigDecimal.valueOf(money));
				toplist.setUpdatetime(new Date());
				updateByKey(toplist);
				System.out.println("更新排行榜财富: "+toplist.getPlayername()+":"+topMoney+","+toplist.getMoney());
			}
		}
		return true;		
	}
}
