package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.hd.base.BaseService;
import cn.hd.base.Bean;
import cn.hd.cf.dao.SavingMapper;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.SavingExample;
import cn.hd.cf.model.SavingExample.Criteria;

public class SavingService extends BaseService {
	private SavingMapper	savingMapper;
	public static String ITEM_KEY = "saving";
	
	public SavingMapper getSavingMapper() {
		return savingMapper;
	}

	public void setSavingMapper(SavingMapper savingMapper) {
		this.savingMapper = savingMapper;
	}

	public SavingService()
	{
		initMapper("savingMapper");
		initData();
	}
	
	public void initData(){
		SavingExample example = new SavingExample();
		List<Saving> savings = savingMapper.selectByExample(example);
		for (int i=0; i<savings.size();i++){
			Saving saving = savings.get(i);
			String key = saving.getPlayerid()+ITEM_KEY;
			 jedis.del(key);
		}
		for (int i=0; i<savings.size();i++){
			Saving record = savings.get(i);
			String key = record.getPlayerid()+ITEM_KEY;
			 jedis.hset(key, record.getItemid().toString(),record.toString());
		}		
		jedis.close();
	}	
	public List<Saving> findByPlayerId(int playerId)
	{
		List<Saving> savings = new ArrayList<Saving>();
		String key = playerId+ITEM_KEY;
		Map<String,String> mapJsons = jedis.hgetAll(key);
		Collection<String> l = mapJsons.values();
		for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
			  String str = (String)iter.next();
			  System.out.println(str);
			  savings.add((Saving)Bean.toBean(str,Saving.class));
		}
		jedis.close();
		
//		SavingExample example = new SavingExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
//		savings = savingMapper.selectByExample(example);
		
		return savings;
	}
	
	public Saving findLivingSavingByPlayerId(int playerId)
	{
		String key = playerId+ITEM_KEY;
		String strJson = jedis.hget(key, new Integer(1).toString());
		return (Saving)Bean.toBean(strJson, Saving.class);
	}
	
	public boolean add(Saving record)
	{
		String key = record.getPlayerid()+ITEM_KEY;
		jedis.hset(key, record.getItemid().toString(), record.toString());
		jedis.close();
		//System.out.println("增加存款记录:"+record.toString());
		
		try {
			savingMapper.insert(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}
	
	public boolean remove(Saving record)
	{
		String key = record.getPlayerid()+ITEM_KEY;
		jedis.hdel(key, record.getItemid().toString());
		jedis.close();
		//System.out.println("删除saving记录:"+record.toString());
		
		try {
			SavingExample example = new SavingExample();
			Criteria criteria = example.createCriteria();
			criteria.andPlayeridEqualTo(record.getPlayerid());
			criteria.andItemidEqualTo(record.getItemid());
			savingMapper.deleteByExample(example);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}	
	
	public boolean updateLive(Saving record)
	{
		String key = record.getPlayerid()+ITEM_KEY;
		jedis.hset(key, new Integer(1).toString(), record.toString());		
		jedis.close();
		
		try {
			SavingExample example = new SavingExample();
			Criteria criteria = example.createCriteria();
			criteria.andPlayeridEqualTo(record.getPlayerid());
			criteria.andItemidEqualTo(1);			
			savingMapper.updateByExampleSelective(record, example);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}				
		return true;
	}
	
	
}
