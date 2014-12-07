package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.StockdataMapper;
import cn.hd.cf.model.StockdataExample;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.model.StockdataExample.Criteria;

public class StockdataService extends BaseService {
	private StockdataMapper	stockdataMapper;
	
	public StockdataMapper getStockdataMapper() {
		return stockdataMapper;
	}

	public void setStockdataMapper(StockdataMapper stockdataMapper) {
		this.stockdataMapper = stockdataMapper;
	}

	public StockdataService()
	{
		initMapper("stockdataMapper");
	}
	
	public Stockdata findActive()
	{
		StockdataExample example = new StockdataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Stockdata> datas = stockdataMapper.selectByExample(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public boolean add(Stockdata record)
	{
		stockdataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public void resetInacvtive(Stockdata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		stockdataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
}