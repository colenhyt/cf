package cn.hd.cf.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseService;
import cn.hd.cf.dao.InitdataMapper;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Initdata;
import cn.hd.cf.model.InitdataExample;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.InitdataExample.Criteria;

public class InitdataService extends BaseService {
	private InitdataMapper	initdataMapper;
	
	public InitdataMapper getInitdataMapper() {
		return initdataMapper;
	}

	public void setInitdataMapper(InitdataMapper initdataMapper) {
		this.initdataMapper = initdataMapper;
	}

	public InitdataService()
	{
		initMapper("initdataMapper");
	}
	
	public Initdata findActive()
	{
		InitdataExample example = new InitdataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Initdata> datas = initdataMapper.selectByExampleWithBLOBs(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public Init findInit(){
		try {
			Initdata data = findActive();
			String str = new String(data.getData(),"utf-8");
			List<Init> list = jsonToBeanList(str, Init.class);
			if (list.size()>0)
				return list.get(0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	public boolean add(Initdata record)
	{
		initdataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public void resetInacvtive(Initdata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		initdataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
	
	 public static void main(String[] args) {
			Date time = new Date(); 
		 SavingdataService savingdataService = new SavingdataService();
		 SavingService savingService = new SavingService();
			Saving savingCfg = savingdataService.findSaving((byte)0);
			Saving saving = new Saving();
			saving.setName(savingCfg.getName());
			saving.setPeriod(savingCfg.getPeriod());
			saving.setRate(savingCfg.getRate());
			saving.setType(savingCfg.getType());
			saving.setPlayerid(1);
			saving.setAmount(Float.valueOf(1));
			saving.setCreatetime(time);
			savingService.add(saving);
	 }
}
