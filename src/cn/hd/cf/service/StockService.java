package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.StockMapper;
import cn.hd.cf.model.StockExample;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.StockExample.Criteria;

public class StockService extends BaseService {
	private StockMapper	stockMapper;
	
	public StockMapper getStockMapper() {
		return stockMapper;
	}

	public void setStockMapper(StockMapper stockMapper) {
		this.stockMapper = stockMapper;
	}

	public StockService()
	{
		initMapper("stockMapper");
	}
	
	public List<Stock> findAll()
	{
		StockExample example = new StockExample();
		return stockMapper.selectByExample(example);
	}
	
	public List<Stock> findByPlayerId(int playerId)
	{
		StockExample example = new StockExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		return stockMapper.selectByExample(example);
	}
	
	public boolean updateStocks(List<Stock> stocks)
	{
		for (int i=0;i<stocks.size();i++){
			Stock record = stocks.get(i);
			stockMapper.updateByPrimaryKey(record);
		}
		DBCommit();
		return true;
	}
	
	public int add(Stock record)
	{
		stockMapper.insert(record);
		DBCommit();
		return 0;
	}
	
	public boolean update(Stock record)
	{
		stockMapper.updateByPrimaryKeySelective(record);
		DBCommit();
		return true;
	}
}
