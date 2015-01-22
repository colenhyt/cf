package cn.hd.cf.tools;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.TitledataMapper;
import cn.hd.cf.model.TitledataExample;
import cn.hd.cf.model.Titledata;
import cn.hd.cf.model.TitledataExample.Criteria;

public class TitledataService extends BaseService {
	private TitledataMapper	titledataMapper;
	
	public TitledataMapper getTitledataMapper() {
		return titledataMapper;
	}

	public void setTitledataMapper(TitledataMapper titledataMapper) {
		this.titledataMapper = titledataMapper;
	}

	public TitledataService()
	{
		initMapper("titledataMapper");
	}
	
	public Titledata findActive()
	{
		TitledataExample example = new TitledataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Titledata> datas = titledataMapper.selectByExampleWithBLOBs(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public boolean add(Titledata record)
	{
		titledataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public void resetInacvtive(Titledata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		titledataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
}
