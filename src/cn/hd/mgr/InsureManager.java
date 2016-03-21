package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import redis.clients.jedis.Jedis;
import cn.hd.cf.action.InsureAction;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;


public class InsureManager extends MgrBase{
	private Map<Integer,Insure>	insureCfgMap;
	//private Map<Integer,List<Insure>>	insuresMap;
	private InsureAction action;
	private Vector<RedisClient> redisClients;
	
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
		
		redisClients = new Vector<RedisClient>();
		dataThreads = new Vector<DataThread>();
		 for (int i=0;i<redisCfg1.getThreadCount();i++){
			 //read:
			RedisClient client = new RedisClient(redisCfg1);
			redisClients.add(client);
			
			 //write:
			 DataThread dataThread = new DataThread(redisCfg1);
			dataThreads.add(dataThread);
			dataThread.start();
		 }	 
		 
    	insureCfgMap = new HashMap<Integer,Insure>();
    	
		Jedis j3 = jedisClient3.getJedis();
		
		String dataStr = j3.get(MgrBase.DATAKEY_DATA_INSURE);
		jedisClient3.returnResource(j3);
		List<Insure> data2 = JSON.parseArray(dataStr, Insure.class);    	
    	for (int i=0;i<data2.size();i++){
    		Insure insure = data2.get(i);
    		if (!insureCfgMap.containsKey(insure.getId()))
    			insureCfgMap.put(insure.getId(), insure);
    	}
    	log.warn("init insuredata:"+insureCfgMap.size());
    	
    	//insuresMap = Collections.synchronizedMap(new HashMap<Integer,List<Insure>>());
    	
//    	Jedis jedis = jedisClient.getJedis();   
//    	
//    	Set<String> playerids = jedis.hkeys(super.DATAKEY_INSURE);
//    	for (String strpid:playerids){
//    		String jsonitems = jedis.hget(super.DATAKEY_INSURE, strpid);
//    		List<Insure> list = JSON.parseArray(jsonitems, Insure.class);
//    		insuresMap.put(Integer.valueOf(strpid), list);
//    	}
//    	jedisClient.returnResource(jedis);
//    	LogMgr.getInstance().log("load insures :" + playerids.size());   
//    	LogMgr.getInstance().log("insure init :");
    }
    
	/**
	 * 获取保险
	 * @param int playerid
	 * @param int itemid
	 * @return Insure 对象
	 * */
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
    
	/**
	 * 获取玩家保险总金额
	 * @param int playerid
	 * @return float 保险总金额
	 * */
    public synchronized float getInsureAmount(int playerId){
		float insureamount = 0;
		List<Insure> insures = getInsureList(playerId);
		if (insures!=null){
		for (int i=0;i<insures.size();i++){
			insureamount += insures.get(i).getAmount();
		}
		}
		insureamount = Float.valueOf(insureamount).intValue();
		return insureamount;
	}
    
	/**
	 * 获取玩家全部保险
	 * @param int playerid
	 * @return Insure list 对象
	 * */
    public synchronized List<Insure> getInsureList(int playerId){
    	List<Insure> list = null;
    	if (list==null){
    		int index = playerId%redisClients.size();
			RedisClient jedisClient = redisClients.get(index);    		
			Jedis jedis = jedisClient.getJedis();
			String liststr = jedis.hget(MgrBase.DATAKEY_INSURE, String.valueOf(playerId));
			jedisClient.returnResource(jedis);    		
			if (liststr!=null){
				list = JSON.parseArray(liststr, Insure.class);
				//insuresMap.put(playerId, list);
			}
    	}
    	return list;
	}
    
	/**
	 * 更新保险
	 * @param int playerid
	 * @param Insure 对象
	 * @return int 0表示操作成功
	 * */
	public synchronized int updateInsure(int playerId,Insure record){
		List<Insure> list = getInsureList(playerId);
		if (list==null)
			return RetMsg.MSG_InsureNotExist;
		
		boolean found = false;
		for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
				Insure incfg = InsureManager.getInstance().getInsureCfg(list.get(i).getItemid());
				list.get(i).setAmount(record.getQty()*incfg.getPrice());
				list.get(i).setQty(record.getQty());
				found = true;
				break;
			}
		}
		if (found){
			DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
			dataThread.updateInsure(playerId, JSON.toJSONString(list));
			return RetMsg.MSG_OK;
		}
		return RetMsg.MSG_InsureNotExist;
	}

	/**
	 * 增加保险
	 * @param int playerid
	 * @param Insure 对象
	 * @return int 0表示增加成功
	 * */
	public synchronized int addInsure(int playerId,Insure record){
		List<Insure> list = getInsureList(playerId);
		boolean found = false;
		if (list==null){
			list = new ArrayList<Insure>();
			//insuresMap.put(playerId, list);
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
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
		dataThread.updateInsure(playerId, JSON.toJSONString(list));
		return RetMsg.MSG_OK;
	}

	/**
	 * 增加保险
	 * @param int playerid
	 * @param int item id
	 * @param int 数量
	 * @param float 价格
	 * @param float 金额
	 * @return String 保险数据
	 * */
	public synchronized String add(int playerid,int itemid,int qty,float price,float amount,String sessionstr){
		Player p = DataManager.getInstance().findPlayer(playerid);
		if (p==null){
			return action.msgStr(RetMsg.MSG_PlayerNotExist);
		}    	

		long clientSessionid = 0;
//		if (sessionstr!=null){
//			clientSessionid = Long.valueOf(sessionstr);
//		}
		long canSubmit = DataManager.getInstance().canSubmit(playerid, clientSessionid);
		if (canSubmit<=0){
			return action.msgStr((int)canSubmit);
		}
		
		Insure item = new Insure();
		item.setPlayerid(playerid);
		item.setItemid(itemid);
		item.setQty(qty);
		item.setAmount(amount);
		item.setPrice(price);
//		action.setInsure(item);
		return action.add(canSubmit,item);
	}

	/**
	 * 更新全部保险
	 * @param int playerid
	 * @param List<Insure> 全部保险
	 * @return 无
	 * */
	public synchronized void updateInsures(int playerId,List<Insure> insures){
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
		dataThread.updateInsure(playerId, JSON.toJSONString(insures));
	}
	
	/**
	 * 删除保险
	 * @param int playerid
	 * @param Insure 对象
	 * @return int 0 表示操作成功
	 * */
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
			DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
			dataThread.updateInsure(playerId, JSON.toJSONString(list));
			return RetMsg.MSG_OK;
		}
		return RetMsg.MSG_InsureNotExist;
	}
}
