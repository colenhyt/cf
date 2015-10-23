package cn.hd.cf.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

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
	public List<Insure> getDBInsures(int playerId)
	{
		InsureExample example = new InsureExample();
		Criteria criteria = example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerId));
		List<Insure> insures = insureMapper.selectByExample(example);
		return insures;
	}	
	
	public boolean add(Insure record)
	{
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
		System.out.println("删除记录:"+record.toString());
		
		try {
			InsureExample example = new InsureExample();
			Criteria criteria = example.createCriteria();
			criteria.andPlayeridEqualTo(record.getPlayerid());
			criteria.andItemidEqualTo(record.getItemid());
			insureMapper.deleteByExample(example);
			DBCommit();	
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Insure> findAll(){
		InsureExample example = new InsureExample();
		return insureMapper.selectByExample(example);
	}
	
	public synchronized boolean addInsures(Vector<Insure> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
			insureMapper.insert(records.get(i));
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}

	public synchronized boolean removeInsures(Vector<Insure> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
				Insure record = records.get(i);
				InsureExample example = new InsureExample();
				Criteria criteria = example.createCriteria();
				criteria.andPlayeridEqualTo(record.getPlayerid());
				criteria.andItemidEqualTo(record.getItemid());
				insureMapper.deleteByExample(example);			
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}

	public synchronized boolean updateInsures(Vector<Insure> records)
	{		
		try {
			for (int i=0;i<records.size();i++){
				Insure record = records.get(i);
				InsureExample example = new InsureExample();
				Criteria criteria = example.createCriteria();
				criteria.andPlayeridEqualTo(record.getPlayerid());
				criteria.andItemidEqualTo(record.getItemid());			
				insureMapper.updateByExampleSelective(record, example);				
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}	
		return true;
	}	
}
