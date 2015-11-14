package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import cn.hd.base.BaseService;
import cn.hd.cf.model.Saving;
import cn.hd.cf.tools.SavingdataService;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;


public class SavingManager extends MgrBase{
	private Map<Integer,String>	savingMap;
	private Map<Integer,Saving>	savingCfgMap;	
	private Map<Integer,List<Saving>>	savingsMap;
	Saving savingCfg;
	
    public Saving getSavingCfg(int itemId) {
		return savingCfgMap.get(itemId);
	}

	private static SavingManager uniqueInstance = null;  
	
    public static SavingManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new SavingManager();  
        }  
        return uniqueInstance;  
     } 
    
    public void init(){
    	savingCfgMap = new HashMap<Integer,Saving>();
    	SavingdataService savingdataService = new SavingdataService();
    	List<Saving> data = savingdataService.findSavings();
    	for (int i=0;i<data.size();i++){
    		Saving saving = data.get(i);
    		if (!savingCfgMap.containsKey(saving.getId()))
    			savingCfgMap.put(saving.getId(), saving);
    	}

    	savingMap = new HashMap<Integer,String>();
    	
    	savingsMap = new HashMap<Integer,List<Saving>>();
    	
    	Jedis jedis = jedisClient.getJedis();
    	if (!jedis.exists(super.DATAKEY_SAVING)){
    		jedisClient.returnResource(jedis);
    		return;
    	}
    	
    	Set<String> playerids = jedis.hkeys(super.DATAKEY_SAVING);
    	for (String strpid:playerids){
    		String jsonitems = jedis.hget(super.DATAKEY_SAVING, strpid);
    		log.warn("get saving:"+jsonitems);
    		List<Saving> list = BaseService.jsonToBeanList(jsonitems, Saving.class);
    		savingsMap.put(Integer.valueOf(strpid), list);
    	}
    	jedisClient.returnResource(jedis);
    }
    
    public synchronized boolean updateLiveSaving(Saving record){
    	record.setItemid(1);
    	return updateSavingAmount(record);
    }
    
    public synchronized boolean updateSavingAmount(Saving record){
    	List<Saving> list = savingsMap.get(record.getPlayerid());
    	if (list==null)
    		return false;
    	
    	Saving s = null;
    	for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
    			s = list.get(i);
    			break;
    		}
    	}    
    	if (s!=null){
    		s.setAmount(record.getAmount());
    		s.setUpdatetime(new Date());
    		dataThread.updateSaving(record.getPlayerid(), JSON.toJSONString(list));
//    		updateSavingVect.add(s);
    		return true;
    	}
    	return false;
    }
    
    public synchronized boolean updateSaving(int playerId,Saving record){
    	List<Saving> list = savingsMap.get(playerId);
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
    	if (!found){
    		return false;
    	}
    	
    	dataThread.updateSaving(playerId, JSON.toJSONString(list));
    	list.add(record);
    	return true;
    }
    
    public synchronized Saving getSaving(int playerId,int itemid){
		Saving saving = null;
    	List<Saving> list = savingsMap.get(playerId);
    	if (list==null)
    		return saving;
    	
    	for (int i=0;i<list.size();i++){
    		if (list.get(i).getItemid().intValue()==itemid){
    			saving = list.get(i);
    			break;
    		}
    	}
    	return saving;
	}
    
    public synchronized List<Saving> getSavingList(int playerId){
		return savingsMap.get(playerId);
	}
    
	public synchronized boolean deleteSaving(int playerId,Saving record){
		List<Saving> list = savingsMap.get(playerId);
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
			dataThread.updateSaving(playerId, JSON.toJSONString(list));
			return true;
		}
		return false;
	}

	public synchronized boolean addSaving(int playerId,Saving record){
		List<Saving> list = savingsMap.get(playerId);
		boolean found = false;
		if (list==null){
			list = new ArrayList<Saving>();
			savingsMap.put(playerId, list);
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
		dataThread.updateSaving(playerId, JSON.toJSONString(list));
		return true;
	}

	public static void main(String[] args) {
		
    	SavingManager stmgr = SavingManager.getInstance();
    	stmgr.init();
//    	Saving ss = new Saving();
//    	ss.setItemid(2);
//    	stmgr.addSaving(1, ss);
//    	Saving s2 = new Saving();
//    	s2.setPlayerid(1);
//    	s2.setItemid(2);
//    	stmgr.deleteSaving(1, s2);
		Saving sa = new Saving();
		sa.setPlayerid(265);
		sa.setItemid(1);
		sa.setAmount((float)30);
		stmgr.deleteSaving(1, sa);
		stmgr.addSaving(sa.getPlayerid(), sa);
		stmgr.deleteSaving(sa.getPlayerid(), sa);
    }
}
