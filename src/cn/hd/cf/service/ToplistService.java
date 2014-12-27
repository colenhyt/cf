package cn.hd.cf.service;

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
	
	public Toplist findByLessMoney(float fMoney){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andMoneyLessThan(fMoney);
		List<Toplist> list = toplistMapper.selectByExample(example);
		if (list.size()>0)
			return list.get(0);
		
		return null;
	}	
	
	public int findCount(int type){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(type);
		return toplistMapper.countByExample(example);
	}	
	
	public int add(Toplist record){
		toplistMapper.insert(record);
		DBCommit();
		return 0;
	}
	
	public boolean updateData(PlayerWithBLOBs playerBlob,float money){
		int tt = 0;
		Toplist toplist = findByPlayerId(playerBlob.getPlayerid());
		if (toplist==null){
			toplist = findByLessMoney(money);
			int topCount = findCount(tt);
			if (toplist!=null||topCount<10)
			{
				Toplist newtop = new Toplist();
				newtop.setPlayerid(playerBlob.getPlayerid());
				newtop.setPlayername(playerBlob.getPlayername());
				newtop.setCreatetime(new Date());
				newtop.setMoney(money);
				newtop.setType(tt);
				newtop.setZan(0);
				add(newtop);				
				System.out.println("增加排行榜记录: "+newtop.getPlayername()+":"+newtop.getMoney());
			}
			if (toplist!=null&&topCount>=10){
				remove(toplist.getId());
			}
		}else {
			float topMoney = toplist.getMoney().floatValue();
			if ((money-topMoney)>0.01){
				toplist.setMoney(money);
				toplist.setCreatetime(new Date());
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
