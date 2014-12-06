package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.ToplistMapper;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.model.ToplistExample;
import cn.hd.cf.model.ToplistExample.Criteria;

public class ToplistService extends BaseService {
	private ToplistMapper toplistMapper;
	
	public Toplist findById(int id){
		Toplist ccc = toplistMapper.selectByPrimaryKey(id);
		System.out.println("find list id "+ccc.getId());
		return ccc;
	}
	
	public List<Toplist> findByType(int type){
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();		
		criteria.andTypeEqualTo(Integer.valueOf(type));
		List<Toplist> list = toplistMapper.selectByExample(example);
		Toplist ccc = null;
		if (list!=null && list.size()>0) {
			ccc = list.get(2);
		}		
		System.out.println("find list id "+ccc.getId());
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
