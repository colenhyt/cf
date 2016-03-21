package cn.hd.mgr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import cn.hd.cf.model.Player;
import cn.hd.cf.model.Signin;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.util.RedisClient;
import cn.hd.util.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class DataThread extends Thread {
	private RedisClient		jedisClient;
	private List<Player> newPlayersVect;
	private List<Player>	updatePlayersVect;	
	private Map<Integer,String>		updateSavingMap;
	
	private Map<Integer,Vector<String>>		playerLogs;
	private Map<Integer,String>		updateInsureMap;
	private Map<Integer,String>		updateStockMap;
	private int updateDuration = 500;
	public int getUpdateDuration() {
		return updateDuration;
	}

	public void setUpdateDuration(int updateDuration) {
		this.updateDuration = updateDuration;
	}

	private List<Stock>			newStockVect;
	private List<Stock>			updateStockVect;
	private List<Stock>			deleteStockVect;
	private List<Toplist>			updateToplistVect;		
	private List<Toplist>			updateToplistZanVect;		
	private List<String>			signinVect;
	private List<Integer>			doneQuestVect;
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public DataThread(RedisConfig cfg){
		jedisClient = new RedisClient(cfg);
		
		newPlayersVect = Collections.synchronizedList(new ArrayList<Player>());
		updatePlayersVect = Collections.synchronizedList(new ArrayList<Player>());
		
		updateSavingMap  = Collections.synchronizedMap(new HashMap<Integer,String>());
		
		updateInsureMap = Collections.synchronizedMap(new HashMap<Integer,String>());
		
		updateStockMap = Collections.synchronizedMap(new HashMap<Integer,String>());
		
		playerLogs = Collections.synchronizedMap(new HashMap<Integer,Vector<String>>());
		
		newStockVect = Collections.synchronizedList(new ArrayList<Stock>());
		deleteStockVect = Collections.synchronizedList(new ArrayList<Stock>());		
		updateStockVect = Collections.synchronizedList(new ArrayList<Stock>());	
		
		updateToplistVect = Collections.synchronizedList(new ArrayList<Toplist>());
		updateToplistZanVect = Collections.synchronizedList(new ArrayList<Toplist>());
		
		signinVect = new Vector<String>();
		doneQuestVect = new Vector<Integer>();
	}
	
	/**
	 * 存储新增
	 * @param Player 对象
	 * @return 无
	 * */
	public synchronized void push(Player record){
//		InetAddress addr;
//		try {
//			addr = InetAddress.getLocalHost();
//			String ip=addr.getHostAddress().toString();	
//			record.setIpAddress(ip);
////			log.warn("ip address "+ip);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//		}
		newPlayersVect.add(record);
	}
	
	/**
	 * 存储玩家更新
	 * @param Player 玩家对象
	 * @return 无
	 * */
	public synchronized void updatePlayer(Player record){
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();	
			record.setIpAddress(ip);
//			log.warn("ip address "+ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		updatePlayersVect.add(record);
	}
	
	/**
	 * 存储存款
	 * @param int playerid
	 * @param String 玩家存款数据
	 * @return 无
	 * */
	public synchronized void updateSaving(int playerid,String jsonSavings){
		updateSavingMap.put(playerid, jsonSavings);
	}
	
	/**
	 * 存储存款
	 * @param int playerid
	 * @param String 玩家保险数据
	 * @return 无
	 * */
	public synchronized void updateInsure(int playerid,String jsonInsures){
		updateInsureMap.put(playerid, jsonInsures);
	}
	
	/**
	 * 存储存款
	 * @param int playerid
	 * @param String 玩家股票数据
	 * @return 无
	 * */
	public synchronized void updateStock(int playerid,String jsonStocks){
		updateStockMap.put(playerid, jsonStocks);
	}
	
	/**
	 * 存储存款
	 * @param int playerid
	 * @param String 玩家排行榜数据
	 * @return 无
	 * */
	public synchronized void updateToplist(Toplist record){
		updateToplistVect.add(record);
	}	
	
	/**
	 * 存储签到
	 * @param int playerid
	 * @return 无
	 * */
	public synchronized void addSignin(int playerid){
		Signin record = new Signin();
		record.setPlayerid(playerid);
		record.setCrdate(new Date());		
		signinVect.add(JSON.toJSONString(record));
	}
	
	/**
	 * 存储任务
	 * @param int playerid
	 * @return 无
	 * */
	public synchronized void addDoneQuest(int playerid){
		doneQuestVect.add(playerid);
	}
	
	/**
	 * 存储日志
	 * @param int playerid
	 * @param String log
	 * @return 无
	 * */
	public synchronized void addLog(int playerid,String logStr){
		Vector<String> ss = playerLogs.get(playerid);
		if (ss==null){
			ss = new Vector<String>();
		}
		ss.add(logStr);
		playerLogs.put(playerid, ss);
	}
	
	/**
	 * 异步数据落地
	 * @return 无
	 * */
	public void run() {
		Jedis jedis = null;
		while (1==1){
        	try {
				synchronized(this)
				{
//					System.out.println(jedisClient.jedis.isConnected());
//					if (!jedisClient.jedis.isConnected())
//						jedisClient.jedis.connect();
					jedis = jedisClient.getJedis();
					if (jedis==null){
						continue;
					}
	        		Pipeline p = jedis.pipelined();
	        	if (newPlayersVect.size()>0){
	        		for (int i=0;i<newPlayersVect.size();i++){
	        			Player item = newPlayersVect.get(i);
	        			p.hset(MgrBase.DATAKEY_PLAYER, String.valueOf(item.getPlayerid()), JSON.toJSONString(item));
	        			p.hset(MgrBase.DATAKEY_PLAYER_ID, item.getPlayername(),String.valueOf(item.getPlayerid()));
	        		}
		    		//log.warn("batch add players :"+newPlayersVect.size());
		    		newPlayersVect.clear(); 	        		
	        	}
	        	      	
	        	
	        	if (updatePlayersVect.size()>0){
	        		for (int i=0;i<updatePlayersVect.size();i++){
	        			Player item = updatePlayersVect.get(i);
	        			p.hset(MgrBase.DATAKEY_PLAYER, String.valueOf(item.getPlayerid()), JSON.toJSONString(item));
	        		}
		    		//log.warn("batch update players :"+updatePlayersVect.size());
		    		updatePlayersVect.clear(); 	        		
	        	}
	        	
	    		if (playerLogs.size()>0){
	        		Set<Integer> ps = playerLogs.keySet();
	        		for (int playerid:ps){
	        			Vector<String> logstr = playerLogs.get(playerid);
	        			for (String s:logstr){
	        				p.lpush(MgrBase.DATAKEY_DATA_LOG+playerid, s);
	        			}
	        		}
	        		playerLogs.clear();    	    			
	    		}
	    		
	    		if (updateSavingMap.size()>0){
	        		Set<Integer> ps = updateSavingMap.keySet();
	        		for (int playerid:ps){
	        			String json = updateSavingMap.get(playerid);
	        			p.hset(MgrBase.DATAKEY_SAVING, String.valueOf(playerid), json);
	        		}
		    		updateSavingMap.clear();    	    			
	    		}
	    		
	        	
	    		if (updateInsureMap.size()>0){
	        		Set<Integer> ps = updateInsureMap.keySet();
	        		for (int playerid:ps){
	        			String json = updateInsureMap.get(playerid);
	        			p.hset(MgrBase.DATAKEY_INSURE, String.valueOf(playerid), json);
	        		}
//		    		log.warn("batch set insure :"+updateInsureMap.size());
		    		updateInsureMap.clear();    	    			
	    		}
	        	
	    		if (updateStockMap.size()>0){
	        		Set<Integer> ps = updateStockMap.keySet();
	        		for (int playerid:ps){
	        			String json = updateStockMap.get(playerid);
	        			p.hset(MgrBase.DATAKEY_STOCK, String.valueOf(playerid), json);
	        		}
//		    		log.warn("batch set stock :"+updateStockMap.size());
		    		updateStockMap.clear();    	    			
	    		}	    	
	    		
	    		if (updateToplistVect.size()>0){
	    			for (int i=0;i<updateToplistVect.size();i++){
		    			Toplist toplist = updateToplistVect.get(i);
	        			p.hset(MgrBase.DATAKEY_TOPLIST, String.valueOf(toplist.getPlayerid()), JSON.toJSONString(toplist));
	    				
	    			}
//		    		log.warn("batch update toplist :"+updateToplistVect.size());
		    		updateToplistVect.clear();    	    			
	    		}	    		
        		
	    		if (signinVect.size()>0){
	    			for (String item:signinVect){
	    				p.lpush(MgrBase.DATAKEY_SIGNIN, item);
	    			}	    			
		    		log.warn("batch add signins:"+signinVect.size());
		    		signinVect.clear();    	    			
	    		}	    		
				
	        	
//	    		if (doneQuestVect.size()>0){
//	    			PlayerService service2= new PlayerService();
//		    		service2.addDoneQuests(doneQuestVect);
//		    		log.warn("batch add donequest:"+doneQuestVect.size());
//		    		doneQuestVect.clear();    	    			
//	    		}
        		p.sync();
        		jedisClient.returnResource(jedis);
	    		
				}
				
//	        		System.out.println("size :"+DataManager.getInstance().playerMaps.size());
				super.sleep(updateDuration);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
	        	jedisClient.returnResource(jedis);
					
				e.printStackTrace();
			}catch (Exception e) {
	        	jedisClient.returnResource(jedis);
				log.error(e.getMessage());
//				if (e instanceof JedisConnectionException) {
//					JedisConnectionException new_name = (JedisConnectionException) e;
//				}else
//					e.printStackTrace();
			}
		}

    }	
}
