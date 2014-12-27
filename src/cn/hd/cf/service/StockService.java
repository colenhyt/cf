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
	
	public boolean removeStock(int playerId,int stockId,int qty)
	{
		StockExample example = new StockExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		criteria.andQtyEqualTo(qty);
		criteria.andItemidEqualTo(stockId);
		List<Stock> list = stockMapper.selectByExample(example);
		if (list.size()>0){
			stockMapper.deleteByPrimaryKey(list.get(0).getId());
			DBCommit();
			return true;
		}
		return false;
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
	
	public boolean add(Stock record)
	{
		try {
			stockMapper.insertSelective(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public boolean update(Stock record)
	{
		stockMapper.updateByPrimaryKeySelective(record);
		DBCommit();
		return true;
	}
}
