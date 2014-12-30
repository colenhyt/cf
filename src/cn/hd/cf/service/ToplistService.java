package cn.hd.cf.service;

import java.math.BigDecimal;
import java.util.Calendar;
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
		criteria.andTypeEqualTo(Integer.valueOf(type));
		//取当月:
		if (type==1){
			Date d1 = new Date();
		      Calendar c2 = Calendar. getInstance();
		      c2.set(d1.getYear(), d1.getMonth(), 1);
		      int firstMonthDay = c2.get(Calendar.DAY_OF_YEAR);
		      Calendar cl = Calendar. getInstance();
		      cl.setTime(d1);
		      int startWeek = firstMonthDay/7;
		      if (firstMonthDay%7!=0)
		    	  startWeek++;
		      int currDay = cl.get(Calendar.DAY_OF_YEAR);
		      int endWeek = currDay/7;
		      if (currDay%7!=0)
		    	  endWeek++;
		      criteria.andWeekGreaterThanOrEqualTo(startWeek);
		      criteria.andWeekLessThanOrEqualTo(endWeek);
				System.out.println(type+"取得记录数:"+startWeek+",end:"+endWeek);
		}
		example.setOrderByClause("money desc");
		List<Toplist> list = toplistMapper.selectByExample(example);
		return list;
	}
	
	public Toplist findByPlayerId(int playerId){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		List<Toplist> list = toplistMapper.selectByExample(example);
		if (list.size()>0)
			return list.get(0);
		
		return null;
	}
	
	public Toplist findByPlayerIdAndTypeAndWeek(int playerId,int type,int week){
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
		Toplist top = findByPlayerId(playerid);
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
	
	private boolean updateOneData(PlayerWithBLOBs playerBlob,double money,int type,int week,int keepCount){
		Toplist toplist = findByPlayerIdAndTypeAndWeek(playerBlob.getPlayerid(),type,week);
		if (toplist==null){
			toplist = findByLessMoney(money,week);
			int topCount = findCount(type,week);
			if (toplist!=null||(keepCount>0&&topCount<keepCount))
			{
				Toplist newtop = new Toplist();
				newtop.setPlayerid(playerBlob.getPlayerid());
				newtop.setPlayername(playerBlob.getPlayername());
				newtop.setCreatetime(new Date());
				newtop.setMoney(BigDecimal.valueOf(money));
				newtop.setType(type);
				newtop.setWeek(week);
				newtop.setZan(0);
				add(newtop);				
				System.out.println("增加排行榜记录: "+newtop.getPlayername()+":"+newtop.getMoney());
			}
			if (keepCount>0&&toplist!=null&&topCount>=keepCount){
				remove(toplist.getId());
			}
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
	
	public boolean updateData(PlayerWithBLOBs playerBlob,double money,int currWeek){
		//当前排行:
		this.updateOneData(playerBlob, money, 0, -1,-1);

		//当周排名:
		this.updateOneData(playerBlob, money, 1, currWeek,10);
	
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
}
