package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.InsureMapper;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.InsureExample;
import cn.hd.cf.model.InsureExample.Criteria;

public class InsureService extends BaseService {
	private InsureMapper	insureMapper;
	
	public InsureMapper getInsureMapper() {
		return insureMapper;
	}

	public void setInsureMapper(InsureMapper insureMapper) {
		this.insureMapper = insureMapper;
	}

	public InsureService()
	{
		initMapper("insureMapper");
	}
	
	public List<Insure> findAll()
	{
		InsureExample example = new InsureExample();
		return insureMapper.selectByExample(example);
	}
	
	public List<Insure> findByPlayerId(int playerId)
	{
		InsureExample example = new InsureExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		return insureMapper.selectByExample(example);
	}
	
	public boolean updateInsures(List<Insure> insures)
	{
		try {
			for (int i=0;i<insures.size();i++){
				Insure record = insures.get(i);
				insureMapper.updateByPrimaryKeySelective(record);
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public boolean add(Insure record)
	{
		try {
		insureMapper.insert(record);
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean update(Insure record)
	{
		try {
			insureMapper.updateByPrimaryKeySelective(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public boolean delete(int id)
	{
		try {
			insureMapper.deleteByPrimaryKey(id);
			DBCommit();	
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
