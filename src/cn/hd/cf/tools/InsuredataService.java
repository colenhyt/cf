package cn.hd.cf.tools;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.InsuredataMapper;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Insuredata;
import cn.hd.cf.model.InsuredataExample;
import cn.hd.cf.model.InsuredataExample.Criteria;

public class InsuredataService extends BaseService {
	private InsuredataMapper	insuredataMapper;
	
	public InsuredataMapper getInsuredataMapper() {
		return insuredataMapper;
	}

	public void setInsuredataMapper(InsuredataMapper insuredataMapper) {
		this.insuredataMapper = insuredataMapper;
	}

	public InsuredataService()
	{
		initMapper("insuredataMapper");
	}
	
	public Insuredata findActive()
	{
		InsuredataExample example = new InsuredataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Insuredata> datas = insuredataMapper.selectByExampleWithBLOBs(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public boolean add(Insuredata record)
	{
		insuredataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public List<Insure> findInsures()
	{
		try {
			Insuredata data = findActive();
			String str = new String(data.getData(),"utf-8");
			return jsonToBeanList(str, Insure.class);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return null;		
	}
	
	public void resetInacvtive(Insuredata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		insuredataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
}
