package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.SavingdataMapper;
import cn.hd.cf.model.SavingdataExample;
import cn.hd.cf.model.Savingdata;
import cn.hd.cf.model.SavingdataExample.Criteria;

public class SavingdataService extends BaseService {
	private SavingdataMapper	savingdataMapper;
	
	public SavingdataMapper getSavingdataMapper() {
		return savingdataMapper;
	}

	public void setSavingdataMapper(SavingdataMapper savingdataMapper) {
		this.savingdataMapper = savingdataMapper;
	}

	public SavingdataService()
	{
		initMapper("savingdataMapper");
	}
	
	public Savingdata findActive()
	{
		SavingdataExample example = new SavingdataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Savingdata> datas = savingdataMapper.selectByExampleWithBLOBs(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public boolean add(Savingdata record)
	{
		savingdataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public void resetInacvtive(Savingdata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		savingdataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
}
