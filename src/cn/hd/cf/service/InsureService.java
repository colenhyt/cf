package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.hd.base.BaseService;
import cn.hd.base.Bean;
import cn.hd.cf.dao.InsureMapper;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.InsureExample;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.InsureExample.Criteria;

public class InsureService extends BaseService {
	private InsureMapper	insureMapper;
	public static String ITEM_KEY = "insure";
	
	public InsureMapper getInsureMapper() {
		return insureMapper;
	}

	public void setInsureMapper(InsureMapper insureMapper) {
		this.insureMapper = insureMapper;
	}

	public InsureService()
	{
		initMapper("insureMapper");
		//initData();
	}
	
	public List<Insure> findByPlayerId(int playerId)
	{
		List<Insure> insures = new ArrayList<Insure>();
//		String key = playerId+ITEM_KEY;
//		Map<String,String> mapJsons = jedis.hgetAll(key);
//		Collection<String> l = mapJsons.values();
//		for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
//			  String str = (String)iter.next();
//			  System.out.println(str);
//			  insures.add((Insure)Bean.toBean(str,Insure.class));
//		}
//		jedis.close();
		
		System.out.println("哈哈"+insures.size());

		InsureExample example = new InsureExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		insures = insureMapper.selectByExample(example);
		
		return insures;
	}
	
	public boolean add(Insure record)
	{
//		String key = record.getPlayerid()+ITEM_KEY;
//		jedis.hset(key, record.getItemid().toString(), record.toString());
//		jedis.close();
		System.out.println("增加记录:"+record.toString());
		
		try {
		insureMapper.insert(record);
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean delete(Insure record)
	{
//		String key = record.getPlayerid()+ITEM_KEY;
//		jedis.hdel(key, record.getItemid().toString());
//		jedis.close();
		System.out.println("删除记录:"+record.toString());
		
		try {
			insureMapper.deleteByPrimaryKey(record.getId());
			DBCommit();	
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void initData(){
		InsureExample example = new InsureExample();
		List<Insure> insures = insureMapper.selectByExample(example);
		for (int i=0; i<insures.size();i++){
			Insure insure = insures.get(i);
			String key = insure.getPlayerid()+ITEM_KEY;
			 jedis.del(key);
		}
		for (int i=0; i<insures.size();i++){
			Insure record = insures.get(i);
			String key = record.getPlayerid()+ITEM_KEY;
			jedis.hset(key, record.getItemid().toString(),record.toString());
		}		
	}	
}
