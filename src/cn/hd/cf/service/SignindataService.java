package cn.hd.cf.service;

import java.util.List;

import cn.freeteam.base.BaseService;
import cn.hd.cf.dao.SignindataMapper;
import cn.hd.cf.model.SignindataExample;
import cn.hd.cf.model.Signindata;
import cn.hd.cf.model.SignindataExample.Criteria;

public class SignindataService extends BaseService {
	private SignindataMapper	signindataMapper;
	
	public SignindataMapper getSignindataMapper() {
		return signindataMapper;
	}

	public void setSignindataMapper(SignindataMapper signindataMapper) {
		this.signindataMapper = signindataMapper;
	}

	public SignindataService()
	{
		initMapper("signindataMapper");
	}
	
	public Signindata findActive()
	{
		SignindataExample example = new SignindataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Signindata> datas = signindataMapper.selectByExample(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public boolean add(Signindata record)
	{
		signindataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public void resetInacvtive(Signindata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		signindataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
}
