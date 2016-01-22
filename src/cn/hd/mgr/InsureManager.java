package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import cn.hd.cf.action.InsureAction;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.tools.InsuredataService;

import com.alibaba.fastjson.JSON;


public class InsureManager extends MgrBase{
	private Map<Integer,Insure>	insureCfgMap;
	private Map<Integer,List<Insure>>	insuresMap;
	private InsureAction action;
	
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
    	action = new InsureAction();
    	
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
    
	public synchronized int deleteInsure(int playerId,Insure record){
		List<Insure> list = getInsureList(playerId);
		if (list==null)
			return RetMsg.MSG_InsureNotExist;
		
		boolean found = false;
		for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
				list.remove(i);
				found = true;
				break;
			}
		}
		if (found){
			dataThread.updateInsure(playerId, JSON.toJSONString(list));
			return RetMsg.MSG_OK;
		}
		return RetMsg.MSG_InsureNotExist;
	}

	public synchronized int addInsure(int playerId,Insure record){
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
			return RetMsg.MSG_InsureIsExist;
		}
		list.add(record);
		dataThread.updateInsure(playerId, JSON.toJSONString(list));
		return RetMsg.MSG_OK;
	}

	public synchronized String add(int playerid,int itemid,int qty,float price,float amount){
		
		Player p = DataManager.getInstance().findPlayer(playerid);
		if (p==null){
			return action.msgStr(RetMsg.MSG_PlayerNotExist);
		}    	
		Insure item = new Insure();
		item.setPlayerid(playerid);
		item.setItemid(itemid);
		item.setQty(qty);
		item.setAmount(amount);
		item.setPrice(price);
		action.setInsure(item);
		return action.add();
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
