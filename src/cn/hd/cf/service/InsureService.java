package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.InsureMapper;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.InsureExample;
import cn.hd.cf.model.SavingExample;
import cn.hd.cf.model.InsureExample.Criteria;

public class InsureService extends BaseService {
	private InsureMapper	insureMapper;
	private SavingService savingService;
	private InsuredataService insuredataService;
	
	public InsureMapper getInsureMapper() {
		return insureMapper;
	}

	public void setInsureMapper(InsureMapper insureMapper) {
		this.insureMapper = insureMapper;
	}

	public InsureService()
	{
		initMapper("insureMapper");
		savingService = new SavingService();
		insuredataService = new InsuredataService();
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
	
	public Map<Integer,Insure> findUpdatedInsures(int playerId)
	{
		Date curr = new Date();
	
        Calendar cCurr = Calendar.getInstance(); 
        cCurr.setTime(curr);
        Calendar c2 = Calendar.getInstance(); 
		
		List<Insure> insures = this.findByPlayerId(playerId);
		
		Map<Integer,Insure>	mdata = new HashMap<Integer,Insure>();
		
		try {		
		for (int i=0;i<insures.size();i++){
			Insure insure = insures.get(i);
			float inter = 0;
	        c2.setTime(insure.getUpdatetime());
			float diffdd = super.findDayMargin(cCurr.getTimeInMillis(),c2.getTimeInMillis(),0);
			float periodMinutes = insure.getPeriod()*60;
			if ((diffdd-periodMinutes)>0.001)
			{
				//到期,删除并移出现金:
				if (insure.getType()!=null&&insure.getType()==1){
				
					Insure incfg = insuredataService.findInsure(insure.getItemid());
					inter = incfg.getProfit()*insure.getQty();
					savingService.addAmountToLiveSaving(playerId, insure.getAmount()+inter);
				}else {		//保险到期，移除
					inter = -1;
				}
				InsureExample example = new InsureExample();
				Criteria criteria = example.createCriteria();
				criteria.andPlayeridEqualTo(playerId);
				criteria.andItemidEqualTo(insure.getItemid());
				insureMapper.deleteByExample(example);
				DBCommit();					
			}
			
			Insure uinsure = new Insure();
			uinsure.setAmount(insure.getAmount());
			uinsure.setCreatetime(insure.getCreatetime());
			uinsure.setQty(insure.getQty());
			uinsure.setProfit(inter);
			mdata.put(insure.getItemid(), uinsure);
		}
		}catch (Exception e){
			e.printStackTrace();
			return mdata;
		}		
		return mdata;
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
}
