package cn.hd.mgr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;

public class DataThread extends Thread {
	private RedisClient		jedisClient;
	private Vector<PlayerWithBLOBs> newPlayersVect;
	private Vector<PlayerWithBLOBs>	updatePlayersVect;	
	private Map<Integer,String>		updateSavingMap;
	
	private Map<Integer,String>		updateInsureMap;
	private Map<Integer,String>		updateStockMap;
	
	private Vector<Stock>			newStockVect;
	private Vector<Stock>			updateStockVect;
	private Vector<Stock>			deleteStockVect;
	private Vector<Toplist>			newToplistVect;
	private Vector<Toplist>			updateToplistVect;		
	private Vector<Toplist>			updateToplistZanVect;		
	private Vector<Integer>			signinVect;
	private Vector<Integer>			doneQuestVect;
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public DataThread(){
		jedisClient = new RedisClient();
		
		newPlayersVect = new Vector<PlayerWithBLOBs>();
		updatePlayersVect = new Vector<PlayerWithBLOBs>();
		
		updateSavingMap  = new HashMap<Integer,String>();
		
		updateInsureMap = new HashMap<Integer,String>();
		
		updateStockMap = new HashMap<Integer,String>();
		
		newStockVect = new Vector<Stock>();
		deleteStockVect = new Vector<Stock>();		
		updateStockVect = new Vector<Stock>();	
		
		newToplistVect = new Vector<Toplist>();
		updateToplistVect = new Vector<Toplist>();
		updateToplistZanVect = new Vector<Toplist>();
		
		signinVect = new Vector<Integer>();
		doneQuestVect = new Vector<Integer>();
	}
	
	public synchronized void push(PlayerWithBLOBs record){
		newPlayersVect.add(record);
	}
	
	public synchronized void updatePlayer(PlayerWithBLOBs record){
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
		signinVect.add(playerid);
	}
	
	public synchronized void addDoneQuest(int playerid){
		doneQuestVect.add(playerid);
	}
		
	public void run() {
		while (1==1){
        	try {
				synchronized(this)
				{
//					System.out.println(jedisClient.jedis.isConnected());
//					if (!jedisClient.jedis.isConnected())
//						jedisClient.jedis.connect();
					Jedis jedis = jedisClient.getJedis();
	        		Pipeline p = jedis.pipelined();
	        	if (newPlayersVect.size()>0){
//		    		PlayerService service= new PlayerService();
//		    		service.addPlayers(newPlayersVect);
	        		for (int i=0;i<newPlayersVect.size();i++){
	        			PlayerWithBLOBs item = newPlayersVect.get(i);
	        			p.hset(DataManager.getInstance().DATAKEY_PLAYER, String.valueOf(item.getPlayerid()), JSON.toJSONString(item));
	        			p.hset(DataManager.getInstance().DATAKEY_PLAYER_ID, item.getPlayername(),String.valueOf(item.getPlayerid()));
	        		}
		    		log.warn("batch add players :"+newPlayersVect.size());
		    		newPlayersVect.clear(); 	        		
	        	}
	        	
	        	if (updatePlayersVect.size()>0){
	        		for (int i=0;i<updatePlayersVect.size();i++){
	        			PlayerWithBLOBs item = updatePlayersVect.get(i);
	        			p.hset(DataManager.getInstance().DATAKEY_PLAYER, String.valueOf(item.getPlayerid()), JSON.toJSONString(item));
	        		}
		    		log.warn("batch update players :"+updatePlayersVect.size());
		    		updatePlayersVect.clear(); 	        		
	        	}
	        	
	    		if (updateSavingMap.size()>0){
	        		Set<Integer> ps = updateSavingMap.keySet();
	        		for (int playerid:ps){
	        			String json = updateSavingMap.get(playerid);
	        			p.hset(DataManager.getInstance().DATAKEY_SAVING, String.valueOf(playerid), json);
	        		}
		    		log.warn("batch set saving :"+updateSavingMap.size());
		    		updateSavingMap.clear();    	    			
	    		}
	    		
	        	
	    		if (updateInsureMap.size()>0){
	        		Set<Integer> ps = updateInsureMap.keySet();
	        		for (int playerid:ps){
	        			String json = updateInsureMap.get(playerid);
	        			p.hset(DataManager.getInstance().DATAKEY_INSURE, String.valueOf(playerid), json);
	        		}
		    		log.warn("batch set insure :"+updateInsureMap.size());
		    		updateInsureMap.clear();    	    			
	    		}
	        	
	    		if (updateStockMap.size()>0){
	        		Set<Integer> ps = updateStockMap.keySet();
	        		for (int playerid:ps){
	        			String json = updateStockMap.get(playerid);
	        			p.hset(DataManager.getInstance().DATAKEY_STOCK, String.valueOf(playerid), json);
	        		}
		    		log.warn("batch set stock :"+updateStockMap.size());
		    		updateStockMap.clear();    	    			
	    		}	    	
	    		
	    		if (newToplistVect.size()>0){
		    		ToplistService service2= new ToplistService();
		    		service2.addToplists(newToplistVect);
		    		log.warn("batch add toplist :"+newToplistVect.size());
		    		newToplistVect.clear();    	    			
	    		}
	        	
	    		if (updateToplistVect.size()>0){
	    			for (int i=0;i<updateToplistVect.size();i++){
		    			Toplist toplist = updateToplistVect.get(i);
	        			p.hset(DataManager.getInstance().DATAKEY_TOPLIST, String.valueOf(toplist.getPlayerid()), JSON.toJSONString(toplist));
	    				
	    			}
		    		log.warn("batch update toplist :"+updateToplistVect.size());
		    		updateToplistVect.clear();    	    			
	    		}	    		
        		p.sync();
        		jedisClient.returnResource(jedis);
        		
	    		if (signinVect.size()>0){
	    			PlayerService service2= new PlayerService();
		    		service2.addSignins(signinVect);
		    		log.warn("batch add signins:"+signinVect.size());
		    		signinVect.clear();    	    			
	    		}	    		
				
	        	
	    		if (doneQuestVect.size()>0){
	    			PlayerService service2= new PlayerService();
		    		service2.addDoneQuests(doneQuestVect);
		    		log.warn("batch add donequest:"+doneQuestVect.size());
		    		doneQuestVect.clear();    	    			
	    		}
	    		
				}
				
//	        		System.out.println("size :"+DataManager.getInstance().playerMaps.size());
				super.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				if (e instanceof JedisConnectionException) {
					JedisConnectionException new_name = (JedisConnectionException) e;
				}else
					e.printStackTrace();
			}
		}

    }	
}
