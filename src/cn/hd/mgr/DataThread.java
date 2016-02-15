package cn.hd.mgr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Signin;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.util.RedisClient;
import cn.hd.util.RedisConfig;

import com.alibaba.fastjson.JSON;

public class DataThread extends Thread {
	private RedisClient		jedisClient;
	private Vector<Player> newPlayersVect;
	private Vector<Player>	updatePlayersVect;	
	private Map<Integer,String>		updateSavingMap;
	
	private Map<Integer,String>		updateInsureMap;
	private Map<Integer,String>		updateStockMap;
	private int updateDuration = 500;
	public int getUpdateDuration() {
		return updateDuration;
	}

	public void setUpdateDuration(int updateDuration) {
		this.updateDuration = updateDuration;
	}

	private Vector<Stock>			newStockVect;
	private Vector<Stock>			updateStockVect;
	private Vector<Stock>			deleteStockVect;
	private Vector<Toplist>			updateToplistVect;		
	private Vector<Toplist>			updateToplistZanVect;		
	private Vector<String>			signinVect;
	private Vector<Integer>			doneQuestVect;
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public DataThread(RedisConfig cfg){
		jedisClient = new RedisClient(cfg);
		
		newPlayersVect = new Vector<Player>();
		updatePlayersVect = new Vector<Player>();
		
		updateSavingMap  = new HashMap<Integer,String>();
		
		updateInsureMap = new HashMap<Integer,String>();
		
		updateStockMap = new HashMap<Integer,String>();
		
		newStockVect = new Vector<Stock>();
		deleteStockVect = new Vector<Stock>();		
		updateStockVect = new Vector<Stock>();	
		
		updateToplistVect = new Vector<Toplist>();
		updateToplistZanVect = new Vector<Toplist>();
		
		signinVect = new Vector<String>();
		doneQuestVect = new Vector<Integer>();
	}
	
	public synchronized void push(Player record){
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
		newPlayersVect.add(record);
	}
	
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
	
	public synchronized void updateStock(Stock record){
		updateStockVect.add(record);
	}
	
	public synchronized void updateSaving(int playerid,String jsonSavings){
		updateSavingMap.put(playerid, jsonSavings);
	}
	
	public synchronized void updateInsure(int playerid,String jsonInsures){
		updateInsureMap.put(playerid, jsonInsures);
	}
	
	public synchronized void updateStock(int playerid,String jsonStocks){
		updateStockMap.put(playerid, jsonStocks);
	}
	
	public synchronized void updateToplist(Toplist record){
		updateToplistVect.add(record);
	}	
	
	public synchronized void addSignin(int playerid){
		Signin record = new Signin();
		record.setPlayerid(playerid);
		record.setCrdate(new Date());		
		signinVect.add(JSON.toJSONString(record));
	}
	
	public synchronized void addDoneQuest(int playerid){
		doneQuestVect.add(playerid);
	}
		
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
//		    		PlayerService service= new PlayerService();
//		    		service.addPlayers(newPlayersVect);
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
	        	
	    		if (updateSavingMap.size()>0){
	        		Set<Integer> ps = updateSavingMap.keySet();
	        		for (int playerid:ps){
	        			String json = updateSavingMap.get(playerid);
	        			p.hset(MgrBase.DATAKEY_SAVING, String.valueOf(playerid), json);
	        		}
//		    		log.warn("batch set saving :"+updateSavingMap.size());
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
				
	        	
	    		if (doneQuestVect.size()>0){
	    			PlayerService service2= new PlayerService();
		    		service2.addDoneQuests(doneQuestVect);
		    		log.warn("batch add donequest:"+doneQuestVect.size());
		    		doneQuestVect.clear();    	    			
	    		}
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
