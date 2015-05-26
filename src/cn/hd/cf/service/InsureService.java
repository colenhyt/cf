package cn.hd.cf.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import cn.hd.base.BaseService;
import cn.hd.cf.dao.InsureMapper;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.InsureExample;
import cn.hd.cf.model.InsureExample.Criteria;
import cn.hd.util.MybatisSessionFactory;

public class InsureService extends BaseService {
	private InsureMapper	insureMapper;
	public static String ITEM_KEY = "insure";
	
	public InsureMapper getInsureMapper() {
		return insureMapper;
	}

	public void setInsureMapper(InsureMapper insureMapper) {
		this.insureMapper = insureMapper;
	}

	public InsureService()
	{
		initMapper("insureMapper");
	}
	
	public List<Insure> findByPlayerId(int playerId)
	{
		InsureExample example = new InsureExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
//		Connection conn = MybatisSessionFactory.getSession().getConnection();
		String url="jdbc:mysql://localhost:3306/hdcf?user=root&password=123a123@";
		List<Insure> insures = new ArrayList<Insure>();
		Connection conn = null;
		Statement st = null;
		try {
			conn = DriverManager.getConnection(url);
			st = conn.createStatement();
			ResultSet r = st.executeQuery("select itemid,amount,profit,qty,updatetime,period,type from insure where playerid="+playerId);   
			while (r.next())
			{
				Insure insure = new Insure();
				insure.setItemid(r.getInt(1));
				insure.setAmount(r.getFloat(2));
				insure.setProfit(r.getFloat(3));
				insure.setQty(r.getInt(4));
				insure.setUpdatetime(r.getDate(5));
				insure.setType(r.getByte(6));
				insure.setPeriod(r.getInt(7));
				insures.add(insure);
			// 打印当前行的值。
				System.out.println("ROW = " + insure.getItemid());
			}
			st.close();
			r.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (conn!=null)
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}     
		
	return insures;
	}
	
	public boolean add(Insure record)
	{
		if (jedis!=null){
			String key = record.getPlayerid()+ITEM_KEY;
			jedis.hset(key, record.getItemid().toString(), record.toString());
			jedis.close();
		}
		Connection conn = MybatisSessionFactory.getSession().getConnection();
		
		System.out.println("增加保险:"+record.toString()+"conn:"+conn.toString());
		
		try {
		insureMapper.insert(record);
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean delete(Insure record)
	{
		if (jedis!=null){
			String key = record.getPlayerid()+ITEM_KEY;
			jedis.hdel(key, record.getItemid().toString());
			jedis.close();
		}
		System.out.println("删除记录:"+record.toString());
		
		try {
			insureMapper.deleteByPrimaryKey(record.getId());
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
		
		InsureExample example = new InsureExample();
		List<Insure> insures = insureMapper.selectByExample(example);
		for (int i=0; i<insures.size();i++){
			Insure insure = insures.get(i);
			String key = insure.getPlayerid()+ITEM_KEY;
			jedis2.del(key);
		}
		for (int i=0; i<insures.size();i++){
			Insure record = insures.get(i);
			String key = record.getPlayerid()+ITEM_KEY;
			jedis2.hset(key, record.getItemid().toString(),record.toString());
		}	
		jedis2.close();
	}	
}
