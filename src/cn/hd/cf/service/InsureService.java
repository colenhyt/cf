package cn.hd.cf.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
	
	public String readFile2(String filePath,List<String> cfgs) {
		String fileContent = "";
                //目标地址
		String u=this.getClass().getResource("/").getPath() ;
		File file = new File(u+"/"+filePath);
		if (file.isFile() && file.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				try {
                                        //循环，每次读一行
					while ((line = reader.readLine()) != null) {
						if (line.indexOf("mysql.url")==0||line.indexOf("mysql.username")==0||line.indexOf("mysql.password")==0){
							line = line.substring(line.indexOf("=")+1).replace("\\", "");
							cfgs.add(line);
						}
						fileContent += line;
					}
					reader.close();
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return fileContent;
	}	
	public List<Insure> findByPlayerId(int playerId)
	{
		InsureExample example = new InsureExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		List<Insure> insures = insureMapper.selectByExample(example);
		MybatisSessionFactory.getSession().clearCache();
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
