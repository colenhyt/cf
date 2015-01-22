package cn.hd.cf.tools;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.StockdataMapper;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.model.StockdataExample;
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
	
	public List<Stockdata> findActive()
	{
		StockdataExample example = new StockdataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Stockdata> datas = stockdataMapper.selectByExampleWithBLOBs(example);
		return datas;
	}
	
	public Stockdata findStockdata(String stockName){
		StockdataExample example = new StockdataExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(stockName);
		List<Stockdata> datas = stockdataMapper.selectByExampleWithBLOBs(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public Stock findStock(String stockName){
		Stockdata data = null;
		String jsonStr = null;
			jsonStr = new String(data.getQuotes());
		List<Stock> stocks = jsonToBeanList(jsonStr,Stock.class);
		for (int i=0;i<stocks.size();i++){
			if (stocks.get(i).getName().equals(stockName))
				return stocks.get(i);
		}
		return null;
	}
	
	public boolean add(Stockdata record)
	{
		stockdataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public boolean update(Stockdata record)
	{
		stockdataMapper.updateByPrimaryKeySelective(record);
		DBCommit();
		return true;
	}
	
	public boolean updateByKey(Stockdata record)
	{
		stockdataMapper.updateByPrimaryKeySelective(record);
		DBCommit();
		return true;
	}
	
	public boolean clear()
	{
		StockdataExample example = new StockdataExample();
		stockdataMapper.deleteByExample(example);
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
