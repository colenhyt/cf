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
	
	public boolean updateSavings(List<Saving> savings)
	{
		for (int i=0;i<savings.size();i++){
			Saving record = savings.get(i);
			savingMapper.updateByPrimaryKey(record);
		}
		DBCommit();
		return true;
	}
	
	public boolean add(Saving record)
	{
		savingMapper.insert(record);
		DBCommit();
		return true;
	}

}
