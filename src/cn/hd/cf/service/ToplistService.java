package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.ToplistMapper;
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
	
	public int findCount(){
		ToplistExample example=new ToplistExample();
		return toplistMapper.countByExample(example);
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
	
	public List<Toplist> findAll(){
		ToplistExample example=new ToplistExample();
		example.setOrderByClause("money desc");
		List<Toplist> list = toplistMapper.selectByExample(example);
		return list;
	}
	
	public ToplistService(){
		initMapper("ToplistMapper");
	}
	
	public ToplistMapper getToplistMapper() {
		return toplistMapper;
	}

	public void setToplistMapper(ToplistMapper toplistMapper) {
		this.toplistMapper = toplistMapper;
	}
}
