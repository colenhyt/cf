package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.StockMapper;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.StockExample;
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
	
	public Map<Integer,List<Stock>> findMapByPlayerId(int playerId)
	{
		StockExample example = new StockExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		List<Stock> ss = stockMapper.selectByExample(example);
		Map<Integer,List<Stock>> smap = new HashMap<Integer,List<Stock>>();
		for (int i=0;i<ss.size();i++){
			List<Stock> list = smap.get(ss.get(i).getItemid());
			if (list==null){
				list = new ArrayList<Stock>();
				smap.put(ss.get(i).getItemid(), list);
			}
			list.add(ss.get(i));
		}
		return smap;
	}	
	public boolean removeStock(int playerId,int stockId,int qty)
	{
		StockExample example = new StockExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		criteria.andItemidEqualTo(stockId);
		example.setOrderByClause("qty");
		List<Stock> list = stockMapper.selectByExample(example);
		int needRemoveQty = qty;
		boolean exec = false;
		for (int i=0;i<list.size();i++){
			Stock ss = list.get(i);
			if (ss.getQty()<=needRemoveQty){
				needRemoveQty -= ss.getQty();
				stockMapper.deleteByPrimaryKey(ss.getId());
			}else {
				ss.setQty(ss.getQty()-needRemoveQty);
				needRemoveQty = 0;
				stockMapper.updateByPrimaryKey(ss);
			}
			if (needRemoveQty==0) {
				DBCommit();
				exec = true;
				break;
			}
		}
		return exec;
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
		try {
			stockMapper.updateByPrimaryKeySelective(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}			
		return true;
	}
}
