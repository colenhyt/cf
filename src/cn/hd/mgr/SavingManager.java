package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import redis.clients.jedis.Jedis;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.action.SavingAction;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Saving;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;


public class SavingManager extends MgrBase{
	private Map<Integer,Saving>	savingCfgMap;	
	private Map<Integer,List<Saving>>	savingsMap;
	private SavingAction savingAction;
	private Vector<RedisClient> redisClients;
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
    
	/**
	 * 增加存款
	 * @param int playerid
	 * @param int item id
	 * @param int 数量
	 * @param float 价格
	 * @param float 金额
	 * @return String 存款数据
	 * */
    public synchronized String add(int playerid,int itemid,int qty,float price,float amount,String sessionstr){
		Player p = DataManager.getInstance().findPlayer(playerid);
		if (p==null){
			return savingAction.msgStr(RetMsg.MSG_PlayerNotExist);
		}    	
		long clientSessionid = 0;
		if (sessionstr!=null){
			clientSessionid = Long.valueOf(sessionstr);
		}
		long canSubmit = DataManager.getInstance().canSubmit(playerid, clientSessionid);
		if (canSubmit<=0){
			return savingAction.msgStr((int)canSubmit);
		}
		
		Saving item = new Saving();
		item.setPlayerid(playerid);
		item.setItemid(itemid);
		item.setQty(qty);
		item.setAmount(amount);
		item.setPrice(price);
    	//savingAction.setSaving(item);
    	return savingAction.add(canSubmit,item);
    }
    
    public void init(){
    	savingAction = new SavingAction();
    	
		
		redisClients = new Vector<RedisClient>();
		dataThreads = new Vector<DataThread>();
		
		for (int i=0;i<redisCfg1.getThreadCount();i++){
			 //read:
			RedisClient client = new RedisClient(redisCfg1);
			redisClients.add(client);
			
			 //write:
			 DataThread dataThread = new DataThread(redisCfg1);
			dataThreads.add(dataThread);
			dataThread.setUpdateDuration(200);
			dataThread.start();
		 }	    	
    	
    	savingCfgMap = new HashMap<Integer,Saving>();
		Jedis j3 = jedisClient3.getJedis();
		String dataStr = j3.get(MgrBase.DATAKEY_DATA_SAVING);
		jedisClient3.returnResource(j3);
		List<Saving> data2 = JSON.parseArray(dataStr, Saving.class);
		
    	for (int i=0;i<data2.size();i++){
    		Saving saving = data2.get(i);
    		if (!savingCfgMap.containsKey(saving.getId()))
    			savingCfgMap.put(saving.getId(), saving);
    	}
    	log.warn("init savingdata:"+savingCfgMap.size());

    	savingsMap = Collections.synchronizedMap(new HashMap<Integer,List<Saving>>());
    	
    	Jedis jedis = redisClients.get(0).getJedis();   
		if (jedis==null){
			log.error("could not get redis,redis1 may not be run");
			return;
		}
		
//    	Set<String> playerids = jedis.hkeys(super.DATAKEY_SAVING);
//    	for (String strpid:playerids){
//    		String jsonitems = jedis.hget(super.DATAKEY_SAVING, strpid);
////    		LogMgr.getInstance().log("get saving:"+strpid+",data:"+jsonitems);
//    		List<Saving> list = JSON.parseArray(jsonitems, Saving.class);
//    		
//    		savingsMap.put(Integer.valueOf(strpid), list);
//    	}
		redisClients.get(0).returnResource(jedis);
//    	LogMgr.getInstance().log("saving init:"+playerids.size());
    }
    
	/**
	 * 玩家活期存款增加资金
	 * @param int playerid
	 * @param float 增加资金
	 * @return float 存款总额
	 * */
    public synchronized float updateLiveSavingAndTop(int playerid,float addedAmount){
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerid);
		int liveIndex = 0;
		if (savings!=null){
			for (int i=0;i<savings.size();i++){
				Saving saving = savings.get(i);
				if (saving.getItemid()==1){	//活期
					saving.setAmount(saving.getAmount()+addedAmount);
					if (saving.getAmount()<0)
						saving.setAmount((float)0);
					liveIndex = i;
				}
			}			
		}
		
		float newTotal = updateSavings(playerid, savings);
		LogMgr.getInstance().log(playerid," live saving added:"+addedAmount+" new value:"+savings.get(liveIndex).getAmount());
		return newTotal;
    }
    
	/**
	 * 更新玩家活期存款
	 * @param int playerid
	 * @param Saving 活期saving对象
	 * @return float 存款总额
	 * */
    public synchronized float updateLiveSaving(int playerid,Saving liveSaving){
    	boolean found = false;
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerid);
		float savingAmount = 0;
		if (savings!=null){
			for (Saving saving:savings){
				if (saving.getItemid()==liveSaving.getItemid()){	//活期
					found = true;
					saving.setAmount(liveSaving.getAmount());
					if (saving.getAmount()<0)
						saving.setAmount((float)0);
				}
				savingAmount += saving.getAmount();
			}			
		}
		if (found){
			DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
			dataThread.updateSaving(playerid, JSON.toJSONString(savings));
		}
		LogMgr.getInstance().log(playerid," live saving update:"+liveSaving.getAmount());
		return savingAmount;
    }
    
    public synchronized boolean updateLiveSaving(Saving record){
    	record.setItemid(1);
    	return updateSavingAmount(record);
    }
    
	/**
	 * 更新玩家活期存款
	 * @param int playerid
	 * @param Saving saving对象列表
	 * @return float 存款总额
	 * */
    public synchronized float updateSavings(int playerid,List<Saving> list){     	
    		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
    		dataThread.updateSaving(playerid, JSON.toJSONString(list));
    		float savingAmount = 0;
    		for (Saving item:list){
    			savingAmount += item.getAmount();
    		}    	
        	float newTotal = ToplistManager.getInstance().getCurrentTotalMoney(playerid, savingAmount);
    		ToplistManager.getInstance().updateToplist(playerid,null,newTotal);			    		
    		return newTotal;
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
    
	/**
	 * 获取玩家某个存款
	 * @param int playerid
	 * @param int 存款id
	 * @return Saving 对象
	 * */
    public Saving getSaving(int playerId,int itemid){
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
    
	/**
	 * 获取玩家所有存款
	 * @param int playerid
	 * @return Saving 对象列表
	 * */
    public List<Saving> getSavingList(int playerId){
    	List<Saving> list = null;
    	if (list==null){
    		int index = playerId%redisClients.size();
			RedisClient jedisClient = redisClients.get(index);
			Jedis jedis = jedisClient.getJedis();
			String liststr = jedis.hget(MgrBase.DATAKEY_SAVING, String.valueOf(playerId));
			jedisClient.returnResource(jedis);    	
			if (liststr!=null){
				//log.warn("pid:"+playerId+" get saving "+liststr);
				list = JSON.parseArray(liststr, Saving.class);
				//savingsMap.put(playerId, list);
			}
    	}	
    	return list;
	}
    
	/**
	 * 删除存款
	 * @param int playerid
	 * @param Saving 对象
	 * @return int 0表示增加成功
	 * */
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

	/**
	 * 增加活期存款
	 * @param int playerid
	 * @param Saving 对象
	 * @return int 0表示增加成功
	 * */
	public int addFirstSaving(int playerId,Saving record){
		List<Saving> list  = new ArrayList<Saving>();
		//savingsMap.put(playerId, list);
		list.add(record);
		DataThread dataThread = dataThreads.get(playerId%dataThreads.size());
		dataThread.updateSaving(playerId, JSON.toJSONString(list));
		return RetMsg.MSG_OK;
	}

	/**
	 * 增加存款
	 * @param int playerid
	 * @param Saving 对象
	 * @return int 0表示增加成功
	 * */
	public synchronized int addSaving(int playerId,Saving record){
		List<Saving> list = getSavingList(playerId);
		boolean found = false;
		if (list==null){
			list = new ArrayList<Saving>();
			//savingsMap.put(playerId, list);
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
