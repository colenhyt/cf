package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.InsureMapper;
import cn.hd.cf.model.InsureExample;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Saving;
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
	
	public List<Insure> findUpdatedSavings(int playerId,Date lastLogin)
	{
		if (lastLogin==null)
			lastLogin = new Date();
	
		Date d2 = new Date();
		long diffdd = super.findDayMargin(lastLogin,d2);
		
		System.out.println("保险相差天数: "+diffdd);
		List<Insure> insures = this.findByPlayerId(playerId);
		List<Insure> updateInsures = new ArrayList<Insure>();
		
		for (int i=0;i<insures.size();i++){
			Insure insure = insures.get(i);
			Insure uinsure = new Insure();
			float inter = 0;
			if (insure.getType()!=null&&insure.getType()==1){
				if (insure.getPeriod()<=diffdd)
				{
					//inter = insure.getAmount()*insure.getRate()/100;
					insure.setAmount(insure.getAmount()+inter);
					insure.setUpdatetime(d2);
					update(insure);
				}
			}
			
			uinsure.setPlayerid(insure.getPlayerid());
			uinsure.setAmount(insure.getAmount());
			uinsure.setItemid(insure.getItemid());
			uinsure.setQty(insure.getQty());
			uinsure.setProfit(inter);
			updateInsures.add(uinsure);
		}
		return updateInsures;
	}
	
	public boolean updateInsures(List<Insure> insures)
	{
		for (int i=0;i<insures.size();i++){
			Insure record = insures.get(i);
			insureMapper.updateByPrimaryKeySelective(record);
		}
		DBCommit();
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
}
