package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.SavingMapper;
import cn.hd.cf.model.SavingExample;
import cn.hd.cf.model.Saving;
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
		for (int i=0;i<savings.size();i++){
			Saving record = savings.get(i);
			savingMapper.updateByPrimaryKeySelective(record);
		}
		DBCommit();
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
	
	public boolean update(Saving record)
	{
		try {
			savingMapper.updateByPrimaryKeySelective(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}				
		return true;
	}
}
