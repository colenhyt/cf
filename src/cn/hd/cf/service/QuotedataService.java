package cn.hd.cf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import cn.hd.base.BaseService;
import cn.hd.cf.dao.QuotedataMapper;
import cn.hd.cf.model.Quotedata;
import cn.hd.cf.model.QuotedataExample;
import cn.hd.cf.model.QuotedataExample.Criteria;

public class QuotedataService extends BaseService {
	private QuotedataMapper quotedataMapper;
	private Map<Integer,Quotedata> quoteMap;
	
	public QuotedataMapper getQuotedataMapper() {
		return quotedataMapper;
	}

	public void setQuotedataMapper(QuotedataMapper quotedataMapper) {
		this.quotedataMapper = quotedataMapper;
	}


	public QuotedataService()
	{
		initMapper("quotedataMapper");
		quoteMap = new HashMap<Integer,Quotedata>();
	}
	
	public List<Quotedata> findQuotes()
	{
		QuotedataExample example = new QuotedataExample();
		return quotedataMapper.selectByExampleWithBLOBs(example);
	}
	
	public boolean clear(){
		QuotedataExample example = new QuotedataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStockidIsNotNull();
		quotedataMapper.deleteByExample(example);
		DBCommit();
		return true;
	}
	
	public Map<Integer,Quotedata> getQuoteMap(){
		if (quoteMap.size()<=0){
			List<Quotedata> data = findQuotes();
			for (int i=0;i<data.size();i++){
				quoteMap.put(Integer.valueOf(data.get(i).getStockid()), data.get(i));
			}
		}
		return quoteMap;
	}
	
	public void addNewQuote(int stockId,float newPrice){
//		Map<Integer,Quotedata> quoteM = getQuoteMap();
//		Quotedata quotedata = quoteM.get(Integer.valueOf(stockId));
//		if (quotedata!=null){
//			String jsonString = new String(quotedata.getData());
//			JSONArray arrayStrings = JSONArray.fromObject(jsonString);
//			if (arrayStrings.size()>0){		
//				
//			}			
//		}

	}
	
	public boolean add(Quotedata record)
	{
		quotedataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	
}
