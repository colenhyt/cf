package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import cn.hd.base.BaseService;
import cn.hd.base.Bean;
import cn.hd.cf.dao.StockMapper;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.StockExample;
import cn.hd.cf.model.StockExample.Criteria;

public class StockService extends BaseService {
	private StockMapper	stockMapper;
	public static String ITEM_KEY = "stock";
	
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
	
	public List<Stock> findByPlayerId(int playerId)
	{
		StockExample example = new StockExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		return stockMapper.selectByExample(example);
	}
	
	public Map<Integer,List<Stock>> findMapByPlayerId(int playerId)
	{
		Map<Integer,List<Stock>> smap = new HashMap<Integer,List<Stock>>();
		
		if (jedis!=null){
		String key = playerId+ITEM_KEY;
		Collection<String> l = jedis.hgetAll(key).values();
		for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
			  String str = (String)iter.next();
			Stock stock =(Stock)Bean.toBean(str, Stock.class);
			List<Stock> list = smap.get(stock.getItemid());
			if (list==null){
				list = new ArrayList<Stock>();
				smap.put(stock.getItemid(), list);
			}
			list.add(stock);
		}		
		jedis.close();
		return smap;
		}
		
		StockExample example = new StockExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		List<Stock> ss = stockMapper.selectByExample(example);
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
	
	public boolean remove(Stock record)
	{
		if (jedis!=null){
		String key = record.getPlayerid()+ITEM_KEY;
		jedis.hdel(key, record.getItemid().toString());
		jedis.close();
		}
		System.out.println("删除stock记录:"+record.toString());
		
		try {
			stockMapper.deleteByPrimaryKey(record.getId());
			DBCommit();	
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		
		return true;
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
				remove(ss);
			}else {
				ss.setQty(ss.getQty()-needRemoveQty);
				ss.setAmount(ss.getQty()*ss.getPrice());
				needRemoveQty = 0;
				update(ss);
			}
			if (needRemoveQty==0) {
				DBCommit();
				exec = true;
				break;
			}
		}
		return exec;
	}
	
	public boolean add(Stock record)
	{
		if (jedis!=null){
		String key = record.getPlayerid()+ITEM_KEY;
		jedis.hset(key, record.getItemid().toString(), record.toString());
		jedis.close();
		}
		System.out.println("增加stock记录:"+record.toString());
		
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
		if (jedis!=null){
		String key = record.getPlayerid()+ITEM_KEY;
		jedis.hset(key, record.getItemid().toString(), record.toString());
		jedis.close();
		}
		
		try {
			stockMapper.updateByPrimaryKeySelective(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}			
		return true;
	}

	public void initData(Jedis jedis2){
		if (jedis2==null)
			return;
		
		StockExample example = new StockExample();
		List<Stock> stocks = stockMapper.selectByExample(example);
		for (int i=0; i<stocks.size();i++){
			Stock stock = stocks.get(i);
			String key = stock.getPlayerid()+ITEM_KEY;
			jedis2.del(key);
		}
		for (int i=0; i<stocks.size();i++){
			Stock record = stocks.get(i);
			String key = record.getPlayerid()+ITEM_KEY;
			jedis2.hset(key, record.getItemid().toString(),record.toString());
		}	
		jedis2.close();
	}
}
