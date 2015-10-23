package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.StockMapper;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.StockExample;
import cn.hd.cf.model.StockExample.Criteria;
import cn.hd.mgr.StockManager;

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
	
	public List<Stock> findAll(){
		StockExample example = new StockExample();
		return stockMapper.selectByExample(example);
	}
	
	public List<Stock> findByPlayerId(int playerId)
	{
		String jsonstr = StockManager.getInstance().getStocks(playerId);
    	List<Stock> list = BaseService.jsonToBeanList(jsonstr, Stock.class);
		return list;
	}
	
	public List<Stock> getDBStocks(int playerId)
	{
		StockExample example = new StockExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		return stockMapper.selectByExample(example);
	}
	
	public boolean remove(Stock record)
	{
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
		List<Stock> all = findByPlayerId(playerId);
		List<Stock> list = new ArrayList<Stock>();
		for (int i=0;i<all.size();i++){
			if (all.get(i).getItemid().intValue()==stockId){
				list.add(all.get(i));
			}
		}
		int needRemoveQty = qty;
		boolean exec = false;
		for (int i=0;i<list.size();i++){
			Stock ss = list.get(i);
			if (ss.getQty()<=needRemoveQty){
				needRemoveQty -= ss.getQty();
				System.out.println("删除:"+needRemoveQty);
				remove(ss);
			}else {
				ss.setQty(ss.getQty()-needRemoveQty);
				ss.setAmount(ss.getQty()*ss.getPrice());
				System.out.println("更新:"+needRemoveQty);
				needRemoveQty = 0;
				update(ss);
			}
			if (needRemoveQty==0) {
				exec = true;
				break;
			}
		}
		return exec;
	}
	
	public boolean add(Stock record)
	{
		try {
			stockMapper.insertSelective(record);
			DBCommit();
			StockManager.getInstance().addStock(record.getPlayerid(), record);
			System.out.println("增加stock记录:"+record.toString()+";自增长id:"+record.getId());
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
			StockManager.getInstance().updateStock(record.getPlayerid(), record);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}			
		return true;
	}
}
