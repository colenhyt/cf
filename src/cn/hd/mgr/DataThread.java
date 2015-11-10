package cn.hd.mgr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import redis.clients.jedis.Pipeline;
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
	
	private Vector<Saving>			newSavingVect;
	private Vector<Saving>			updateSavingVect;
	private Vector<Saving>			deleteSavingVect;
	private Vector<Insure>			newInsureVect;
	private Vector<Insure>			deleteInsureVect;
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
		
		newSavingVect = new Vector<Saving>();
		updateSavingVect = new Vector<Saving>();
		deleteSavingVect = new Vector<Saving>();
		
		newInsureVect = new Vector<Insure>();
		deleteInsureVect = new Vector<Insure>();
		
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
	
	public synchronized void pushInsure(Insure record){
		newInsureVect.add(record);
	}
	
	public synchronized void deleteInsure(Insure record){
		deleteInsureVect.add(record);
	}
	
	
	public synchronized void pushStock(Stock record){
		newStockVect.add(record);
	}
	
	public synchronized void updateStock(Stock record){
		updateStockVect.add(record);
	}
	
	public synchronized void deleteStock(Stock record){
		deleteStockVect.add(record);
	}
	
	public synchronized void updateSaving(int playerid,String jsonSavings){
		updateSavingMap.put(playerid, jsonSavings);
	}
	
	public synchronized void pushToplist(Toplist record){
		newToplistVect.add(record);
	}
	
	public synchronized void updateToplist(Toplist record){
		updateToplistVect.add(record);
	}	
	
	public synchronized void updateToplistZan(Toplist record){
		updateToplistZanVect.add(record);
	}	
	
	public synchronized void addSignin(int playerid){
		signinVect.add(playerid);
	}
	
	public synchronized void addDoneQuest(int playerid){
		doneQuestVect.add(playerid);
	}
		
	public void run() {
		while (1==1){
				synchronized(this)
				{
	        		Pipeline p = jedisClient.jedis.pipelined();
	        	if (newPlayersVect.size()>0){
//		    		PlayerService service= new PlayerService();
//		    		service.addPlayers(newPlayersVect);
	        		for (int i=0;i<newPlayersVect.size();i++){
	        			PlayerWithBLOBs item = newPlayersVect.get(i);
	        			log.warn("batch add players :"+JSON.toJSONString(item));
	        			p.hset(DataManager.getInstance().DATAKEY_PLAYER, String.valueOf(item.getPlayerid()), JSON.toJSONString(item));
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
		    		log.warn("batch update saving :"+updateSavingMap.size());
		    		updateSavingMap.clear();    	    			
	    		}
	    		
	        	
	    		if (newInsureVect.size()>0){
		    		InsureService service2= new InsureService();
		    		service2.addInsures(newInsureVect);
		    		log.warn("batch add insure :"+newInsureVect.size());
		    		newInsureVect.clear();    	    			
	    		}
	        	
	    		if (deleteInsureVect.size()>0){
	    			InsureService service2= new InsureService();
		    		service2.deleteInsures(deleteInsureVect);
		    		log.warn("batch delete insure :"+deleteInsureVect.size());
		    		deleteInsureVect.clear();    	    			
	    		}
	        	
	    		if (newStockVect.size()>0){
		    		StockService service2= new StockService();
		    		service2.addStocks(newStockVect);
		    		log.warn("batch add stock :"+newStockVect.size());
		    		newStockVect.clear();    	    			
	    		}
	        	
	    		if (updateStockVect.size()>0){
	    			StockService service2= new StockService();
		    		service2.updateStocks(updateStockVect);
		    		log.warn("batch update stock :"+updateStockVect.size());
		    		updateStockVect.clear();    	    			
	    		}
	    		
	    		if (deleteStockVect.size()>0){
	    			StockService service2= new StockService();
		    		service2.deleteStocks(deleteStockVect);
		    		log.warn("batch delete stock :"+deleteStockVect.size());
		    		deleteStockVect.clear();    	    			
	    		}
	    		
	    		if (newToplistVect.size()>0){
		    		ToplistService service2= new ToplistService();
		    		service2.addToplists(newToplistVect);
		    		log.warn("batch add toplist :"+newToplistVect.size());
		    		newToplistVect.clear();    	    			
	    		}
	        	
	    		if (updateToplistVect.size()>0){
	    			ToplistService service2= new ToplistService();
		    		service2.updateToplists(updateToplistVect);
		    		log.warn("batch update toplist :"+updateToplistVect.size());
		    		updateToplistVect.clear();    	    			
	    		}	    		
	        	
	    		if (updateToplistZanVect.size()>0){
	    			ToplistService service2= new ToplistService();
		    		service2.updateToplists(updateToplistZanVect);
		    		log.warn("batch update toplist zan:"+updateToplistZanVect.size());
		    		updateToplistZanVect.clear();    	    			
	    		}
        		p.sync();
	        	
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
				
	        	try {
//	        		System.out.println("size :"+DataManager.getInstance().playerMaps.size());
					super.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

    }	
}
