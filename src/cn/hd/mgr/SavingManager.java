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
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.action.SavingAction;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Saving;
import cn.hd.cf.tools.SavingdataService;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;


public class SavingManager extends MgrBase{
	private Map<Integer,Saving>	savingCfgMap;	
	private Map<Integer,List<Saving>>	savingsMap;
	private SavingAction savingAction;
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
    
    public synchronized String add(int playerid,int itemid,int qty,float price,float amount){
		Player p = DataManager.getInstance().findPlayer(playerid);
		if (p==null){
			return savingAction.msgStr(RetMsg.MSG_PlayerNotExist);
		}    	
		Saving item = new Saving();
		item.setPlayerid(playerid);
		item.setItemid(itemid);
		item.setQty(qty);
		item.setAmount(amount);
		item.setPrice(price);
    	savingAction.setSaving(item);
    	return savingAction.add();
    }
    
    public void init(){
    	savingAction = new SavingAction();
    	
		
		jedisClient = new RedisClient(redisCfg1);
		
		dataThreads = new Vector<DataThread>();
		 for (int i=0;i<redisCfg1.getThreadCount();i++){
			 DataThread dataThread = new DataThread(redisCfg1);
			dataThreads.add(dataThread);
			dataThread.setUpdateDuration(200);
			dataThread.start();
		 }	    	
    	
    	savingCfgMap = new HashMap<Integer,Saving>();
		Jedis j3 = jedisClient3.getJedis();
		String dataStr = j3.get(MgrBase.DATAKEY_DATA_SAVING);
		List<Saving> data2 = JSON.parseArray(dataStr, Saving.class);
		
    	for (int i=0;i<data2.size();i++){
    		Saving saving = data2.get(i);
    		if (!savingCfgMap.containsKey(saving.getId()))
    			savingCfgMap.put(saving.getId(), saving);
    	}
    	log.warn("init savingdata:"+savingCfgMap.size());

    	savingsMap = Collections.synchronizedMap(new HashMap<Integer,List<Saving>>());
    	
    	Jedis jedis = jedisClient.getJedis();   
		if (jedis==null){
			log.error("could not get redis,redis1 may not be run");
			return;
		}
		
//    	Set<String> playerids = jedis.hkeys(super.DATAKEY_SAVING);
//    	for (String strpid:playerids){
//    		String jsonitems = jedis.hget(super.DATAKEY_SAVING, strpid);
////    		log.warn("get saving:"+strpid+",data:"+jsonitems);
//    		List<Saving> list = JSON.parseArray(jsonitems, Saving.class);
//    		
//    		savingsMap.put(Integer.valueOf(strpid), list);
//    	}
    	jedisClient.returnResource(jedis);
//    	log.warn("saving init:"+playerids.size());
    }
    
    public synchronized void updateLiveSaving(int playerid,float addedAmount){
		log.warn("pid:"+playerid+" added live saving:"+addedAmount);
		savingAction.pushLive(playerid, addedAmount);
    }
    
    public synchronized boolean updateLiveSaving(Saving record){
    	record.setItemid(1);
    	return updateSavingAmount(record);
    }
    
    public synchronized boolean updateSavingAmount(Saving record){
    	List<Saving> list = getSavingList(record.getPlayerid());
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
    		DataThread dataThread = dataThreads.get(record.getPlayerid()%dataThreads.size());
    		dataThread.updateSaving(record.getPlayerid(), JSON.toJSONString(list));
//    		updateSavingVect.add(s);
    		return true;
    	}
    	return false;
    }
    
    public synchronized boolean updateSaving(int playerId,Saving record){
    	List<Saving> list = getSavingList(playerId);
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
    	
    	list.add(record);
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
    	dataThread.updateSaving(playerId, JSON.toJSONString(list));
    	return true;
    }
    
    public synchronized Saving getSaving(int playerId,int itemid){
		Saving saving = null;
    	List<Saving> list = getSavingList(playerId);
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
    
    public synchronized List<Integer> getSavingIds(int playerId){
    	List<Integer> itemids = new ArrayList<Integer>();
    	List<Saving> list = getSavingList(playerId);
    	if (list!=null){
    		for (Saving item:list){
    			itemids.add(item.getItemid());
    		}
    	}
    	return itemids;
    }
    public synchronized List<Saving> getSavingList(int playerId){
    	List<Saving> list = savingsMap.get(playerId);
    	if (list==null){
			Jedis jedis = jedisClient.getJedis();
			if (!jedis.hexists(MgrBase.DATAKEY_SAVING, String.valueOf(playerId))){
				return list;
			}
			String liststr = jedis.hget(MgrBase.DATAKEY_SAVING, String.valueOf(playerId));
			jedisClient.returnResource(jedis);    	
			log.warn("pid:"+playerId+" get saving "+liststr);
			if (liststr!=null){
				list = JSON.parseArray(liststr, Saving.class);
				savingsMap.put(playerId, list);
			}
    	}	
    	return list;
	}
    
	public synchronized int deleteSaving(int playerId,Saving record){
		List<Saving> list = getSavingList(playerId);
		if (list==null)
			return RetMsg.MSG_SavingNotExist;
		
		boolean found = false;
		for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
				list.remove(i);
				found = true;
				break;
			}
		}
		if (found){
			DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
			dataThread.updateSaving(playerId, JSON.toJSONString(list));
			return RetMsg.MSG_OK;
		}
		return RetMsg.MSG_SavingNotExist;
	}

	public synchronized int addFirstSaving(int playerId,Saving record){
		List<Saving> list  = new ArrayList<Saving>();
		savingsMap.put(playerId, list);
		list.add(record);
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
		dataThread.updateSaving(playerId, JSON.toJSONString(list));
		return RetMsg.MSG_OK;
	}

	public synchronized int addSaving(int playerId,Saving record){
		List<Saving> list = getSavingList(playerId);
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
			return RetMsg.MSG_SavingIsExist;
		}
		list.add(record);
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
		dataThread.updateSaving(playerId, JSON.toJSONString(list));
		return RetMsg.MSG_OK;
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
