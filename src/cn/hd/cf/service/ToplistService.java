package cn.hd.cf.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.ToplistMapper;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.model.ToplistExample;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.model.ToplistExample;
import cn.hd.cf.model.ToplistExample.Criteria;

public class ToplistService extends BaseService {
	private ToplistMapper toplistMapper;
	private static String ITEM_KEY = "toplist";
	
	public synchronized List<Toplist> findAll(){
		List<Toplist> list = null;
		ToplistExample example=new ToplistExample();
		list = toplistMapper.selectByExample(example);
		return list;
	}
	
	public int add(Toplist record){	
		toplistMapper.insert(record);
		DBCommit();
		return 0;
	}
	
	public synchronized boolean addToplists(Vector<Toplist> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
			toplistMapper.insert(records.get(i));
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}

	public synchronized boolean updateToplists(Vector<Toplist> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
				Toplist record = records.get(i);
				ToplistExample example = new ToplistExample();
				Criteria criteria = example.createCriteria();
				criteria.andPlayeridEqualTo(record.getPlayerid());
				toplistMapper.updateByExampleSelective(record, example);				
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}	
	
	public int updateByKey(Toplist record){
		
		toplistMapper.updateByPrimaryKey(record);
		DBCommit();
		return 0;
	}
	
	public int updateZan(Toplist toplist){
		System.out.println("update zan: "+(toplist==null));
		
		ToplistExample example=new ToplistExample();
		Criteria criteria=example.createCriteria();	
		criteria.andPlayeridEqualTo(toplist.getPlayerid());
		Toplist toplist2 = new Toplist();
		toplist2.setZan(toplist.getZan());
		toplist2.setPlayerid(toplist.getPlayerid());
		toplistMapper.updateByExampleSelective(toplist2, example);
		System.out.println("更新赞:"+toplist2.getZan());
		DBCommit();
		return 0;
	}
	
	public ToplistService(){
		initMapper("toplistMapper");
	}
	
	public ToplistMapper getToplistMapper() {
		return toplistMapper;
	}

	public void setToplistMapper(ToplistMapper toplistMapper) {
		this.toplistMapper = toplistMapper;
	}

	public static void main(String[] args){
		ToplistService s = new ToplistService();
		Toplist record = new Toplist();
		record.setPlayerid(280);
		record.setZan(3);
		s.updateZan(record);
	}
}
