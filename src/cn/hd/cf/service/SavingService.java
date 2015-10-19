package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import redis.clients.jedis.Jedis;
import cn.hd.base.BaseService;
import cn.hd.cf.dao.SavingMapper;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.SavingExample;
import cn.hd.cf.model.SavingExample.Criteria;
import cn.hd.mgr.SavingManager;

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
	}
	
	public List<Saving> findAll(){
		SavingExample example = new SavingExample();
		return savingMapper.selectByExample(example);
	}
	
	public void initData(Jedis jedis2){
		if (jedis2==null)
			return;
		
		SavingExample example = new SavingExample();
		List<Saving> savings = savingMapper.selectByExample(example);
		for (int i=0; i<savings.size();i++){
			Saving saving = savings.get(i);
			String key = saving.getPlayerid()+ITEM_KEY;
			jedis2.del(key);
		}
		for (int i=0; i<savings.size();i++){
			Saving record = savings.get(i);
			String key = record.getPlayerid()+ITEM_KEY;
			jedis2.hset(key, record.getItemid().toString(),record.toString());
		}		
		jedis2.close();
	}	
	public synchronized boolean add(Saving record)
	{		
		try {
			savingMapper.insert(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}
	
	public synchronized boolean removeSavings(Vector<Saving> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
				Saving record = records.get(i);
				SavingExample example = new SavingExample();
				Criteria criteria = example.createCriteria();
				criteria.andPlayeridEqualTo(record.getPlayerid());
				criteria.andItemidEqualTo(record.getItemid());
				savingMapper.deleteByExample(example);			
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}
	
	public boolean remove(Saving record)
	{
		System.out.println("删除saving记录:"+record.getItemid()+";"+record.getPlayerid());
		
		try {
			SavingExample example = new SavingExample();
			Criteria criteria = example.createCriteria();
			criteria.andPlayeridEqualTo(record.getPlayerid());
			criteria.andItemidEqualTo(record.getItemid());
			savingMapper.deleteByExample(example);
			DBCommit();
			SavingManager.getInstance().deleteSaving(record.getPlayerid(), record);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}	
	
	public synchronized boolean update(Saving record)
	{
		try {
			SavingExample example = new SavingExample();
			Criteria criteria = example.createCriteria();
			criteria.andPlayeridEqualTo(record.getPlayerid());
			criteria.andItemidEqualTo(record.getItemid());			
			savingMapper.updateByExampleSelective(record, example);
			DBCommit();
			SavingManager.getInstance().updateSaving(record.getPlayerid(), record);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}				
		return true;
	}

	public synchronized List<Saving> getDBSavings(int playerId)
	{
		List<Saving> savings = new ArrayList<Saving>();
		
		SavingExample example = new SavingExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		savings = savingMapper.selectByExample(example);
		
		return savings;
	}

	public synchronized boolean addSavings(Vector<Saving> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
			savingMapper.insert(records.get(i));
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}

	public synchronized boolean updateSavings(Vector<Saving> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
				Saving record = records.get(i);
				SavingExample example = new SavingExample();
				Criteria criteria = example.createCriteria();
				criteria.andPlayeridEqualTo(record.getPlayerid());
				criteria.andItemidEqualTo(record.getItemid());			
				savingMapper.updateByExampleSelective(record, example);				
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}		
}
