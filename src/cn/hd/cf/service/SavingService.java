package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.SavingMapper;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.SavingExample;
import cn.hd.cf.model.SavingExample.Criteria;

public class SavingService extends BaseService {
	private SavingMapper	savingMapper;
	
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
	
	public List<Saving> findAll()
	{
		SavingExample example = new SavingExample();
		return savingMapper.selectByExample(example);
	}
	
	public List<Saving> findByPlayerId(int playerId)
	{
		SavingExample example = new SavingExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		return savingMapper.selectByExample(example);
	}
	
	public Saving findLivingSavingByPlayerId(int playerId)
	{
		SavingExample example = new SavingExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		List<Saving> savings = savingMapper.selectByExample(example);
		return savings.get(0);
	}
	
	public boolean updateSavings(List<Saving> savings)
	{
		try {
			for (int i=0;i<savings.size();i++){
				Saving record = savings.get(i);
				savingMapper.updateByPrimaryKeySelective(record);
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}			
		return true;
	}
	
	public boolean add(Saving record)
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
	
	public boolean remove(int playerid,int itemid)
	{
		try {
			SavingExample example = new SavingExample();
			Criteria criteria = example.createCriteria();
			criteria.andPlayeridEqualTo(playerid);
			criteria.andItemidEqualTo(itemid);
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
