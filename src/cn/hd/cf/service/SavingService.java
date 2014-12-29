package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	
	//
	public List<Saving> findUpdatedSavings(int playerId,Date lastLogin)
	{
		if (lastLogin==null)
			lastLogin = new Date();
	
		Date d2 = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(lastLogin);
        Calendar c2 = Calendar.getInstance(); 
        c2.setTime(d2);
        long diffdd = super.findDayMargin(c.getTimeInMillis(),c2.getTimeInMillis(),1);
		
		System.out.println("相差天数: "+diffdd);
		List<Saving> savings = this.findByPlayerId(playerId);
		List<Saving> updateSavings = new ArrayList<Saving>();
		
		for (int i=0;i<savings.size();i++){
			Saving saving = savings.get(i);
			Saving usaving = new Saving();
			float inter = 0;
			//if (saving.getPeriod()<=diffdd)
			{
				inter = saving.getAmount()*saving.getRate()/100;
				saving.setAmount(saving.getAmount()+inter);
				saving.setUpdatetime(d2);
				update(saving);
			}
			usaving.setPlayerid(saving.getPlayerid());
			usaving.setAmount(saving.getAmount());
			usaving.setRate(saving.getRate());
			usaving.setQty(saving.getQty());
			usaving.setItemid(saving.getItemid());
			usaving.setProfit(inter);
			System.out.println("得到利息: "+inter);
			updateSavings.add(usaving);
		}
		return updateSavings;
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
