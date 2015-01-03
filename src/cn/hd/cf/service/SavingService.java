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
	
	//
	public Map<Integer,Saving> findUpdatedSavings(int playerId)
	{
		Date currDate = new Date();
        Calendar cCurr = Calendar.getInstance(); 
        cCurr.setTime(currDate);
        Calendar c2 = Calendar.getInstance(); 
		
		List<Saving> savings = this.findByPlayerId(playerId);
		
		Map<Integer,Saving>	 mdata = new HashMap<Integer,Saving>();
		Saving liveSaving = null;
		for (int i=0;i<savings.size();i++){
			Saving saving = savings.get(i);
			if (saving.getType()==0)
				liveSaving = saving;
			float inter = 0;
	        c2.setTime(saving.getUpdatetime());
	        float diffdd = super.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
	        float periodMinutes = saving.getPeriod()*60;
			System.out.println("利息到期时间:"+diffdd+","+periodMinutes);
			if ((diffdd-periodMinutes)>0.001)
			{
				//活期
				if (saving.getType()==0)
				{
					long diff = (long)(diffdd/periodMinutes);
					inter = diff * saving.getAmount()*saving.getRate()/100;
					saving.setAmount(saving.getAmount()+inter);
					saving.setUpdatetime(currDate);
					update(saving);
				}else //定期，取出来,跟利息一起放回到活期
				{
					inter = saving.getAmount()*saving.getRate()/100;
					float newsaving = saving.getAmount()+inter;
					if (liveSaving!=null){
						liveSaving.setAmount(liveSaving.getAmount()+newsaving);
						update(liveSaving);
						remove(playerId,saving.getItemid());
						Saving ll = mdata.get(liveSaving.getItemid());
						if (ll!=null)
							ll.setAmount(liveSaving.getAmount());
					}
				}
			}
			
			Saving usaving = new Saving();
			usaving.setAmount(saving.getAmount());
			usaving.setCreatetime(saving.getCreatetime());
			usaving.setQty(saving.getQty());
			usaving.setProfit(inter);
			System.out.println("得到利息: "+inter);
			mdata.put(saving.getItemid(), usaving);
		}
		return mdata;
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
	
	public boolean addAmountToLiveSaving(int playerId,float amount)
	{
		SavingExample example = new SavingExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		List<Saving> savings = savingMapper.selectByExample(example);
		Saving liveSaving = null;
		for (int i=0;i<savings.size();i++){
			if (savings.get(i).getType()==0){
				liveSaving = savings.get(i);
				break;
			}
		}
		if (liveSaving!=null)
		{
			liveSaving.setAmount(liveSaving.getAmount()+amount); 
			update(liveSaving);
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
