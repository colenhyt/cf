package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;

import cn.hd.base.BaseService;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Insuredata;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.tools.InsuredataService;


public class InsureManager extends MgrBase{
	private Map<Integer,Insure>	insureCfgMap;
	private Map<Integer,List<Insure>>	insuresMap;
	
    public synchronized Insure getInsureCfg(int itemId) {
		return insureCfgMap.get(itemId);
	}

	private static InsureManager uniqueInstance = null;  
	
    public static InsureManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new InsureManager();  
        }  
        return uniqueInstance;  
     } 
    
    public void init(){
    	insureCfgMap = new HashMap<Integer,Insure>();
    	InsuredataService insuredataService = new InsuredataService();
    	List<Insure> data = insuredataService.findInsures();
    	for (int i=0;i<data.size();i++){
    		Insure insure = data.get(i);
    		if (!insureCfgMap.containsKey(insure.getId()))
    			insureCfgMap.put(insure.getId(), insure);
    	}

    	insuresMap = Collections.synchronizedMap(new HashMap<Integer,List<Insure>>());
    	
    	Jedis jedis = jedisClient.getJedis();   	
    	Set<String> playerids = jedis.hkeys(super.DATAKEY_INSURE);
    	for (String strpid:playerids){
    		String jsonitems = jedis.hget(super.DATAKEY_INSURE, strpid);
    		log.warn("get insure:"+jsonitems);
    		List<Insure> list = JSON.parseArray(jsonitems, Insure.class);
    		insuresMap.put(Integer.valueOf(strpid), list);
    	}
    	jedisClient.returnResource(jedis);
    	log.warn("load insures :" + playerids.size());    	
    }
    
    public synchronized Insure getInsure(int playerId,int itemid){
		Insure insure = null;
    	List<Insure> list = getInsureList(playerId);
    	if (list==null)
    		return insure;
    	
    	for (int i=0;i<list.size();i++){
    		if (list.get(i).getItemid().intValue()==itemid){
    			insure = list.get(i);
    			break;
    		}
    	}
    	return insure;
	}
    
    public synchronized List<Insure> getInsureList(int playerId){
    	List<Insure> list = insuresMap.get(playerId);
    	if (list==null){
			Jedis jedis = jedisClient.getJedis();
			String liststr = jedis.hget(super.DATAKEY_INSURE, String.valueOf(playerId));
			jedisClient.returnResource(jedis);    		
			if (liststr!=null){
				list = JSON.parseArray(liststr, Insure.class);
				insuresMap.put(playerId, list);
			}
    	}
    	return list;
	}
    
	public synchronized boolean deleteInsure(int playerId,Insure record){
		List<Insure> list = getInsureList(playerId);
		if (list==null)
			return false;
		
		boolean found = false;
		for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
				list.remove(i);
				found = true;
				break;
			}
		}
		if (found){
			System.out.println("删除后json: "+list.size());
			dataThread.updateInsure(playerId, JSON.toJSONString(list));
			return true;
		}
		return false;
	}

	public synchronized boolean addInsure(int playerId,Insure record){
		List<Insure> list = getInsureList(playerId);
		boolean found = false;
		if (list==null){
			list = new ArrayList<Insure>();
			insuresMap.put(playerId, list);
		}else {
			for (int i=0;i<list.size();i++){
				if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
					found = true;
					break;
				}
			}
		}
		if (found){
			return false;
		}
		list.add(record);
		dataThread.updateInsure(playerId, JSON.toJSONString(list));
		return true;
	}

	public static void main(String[] args) {
		
    	InsureManager stmgr = InsureManager.getInstance();
    	stmgr.init();
//    	Insure ss = new Insure();
//    	ss.setItemid(2);
//    	stmgr.addInsure(1, ss);
//    	Insure s2 = new Insure();
//    	s2.setPlayerid(1);
//    	s2.setItemid(2);
//    	stmgr.deleteInsure(1, s2);
		Insure sa = new Insure();
		sa.setPlayerid(265);
		sa.setItemid(1);
		sa.setAmount((float)30);
		stmgr.deleteInsure(1, sa);
		stmgr.addInsure(sa.getPlayerid(), sa);
		stmgr.deleteInsure(sa.getPlayerid(), sa);
    }
}
