package cn.hd.mgr;

import java.util.Vector;

import org.apache.log4j.Logger;

import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.ToplistService;

public class DataThread extends Thread {
	private Vector<PlayerWithBLOBs> newPlayersVect;
	private Vector<Saving>			newSavingVect;
	private Vector<Saving>			updateSavingVect;
	private Vector<PlayerWithBLOBs>	updatePlayersVect;	
	private Vector<Toplist>			newToplistVect;
	private Vector<Toplist>			updateToplistVect;		
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public DataThread(){
		newPlayersVect = new Vector<PlayerWithBLOBs>();
		updatePlayersVect = new Vector<PlayerWithBLOBs>();
		newSavingVect = new Vector<Saving>();
		updateSavingVect = new Vector<Saving>();
	}
	
	public synchronized void push(PlayerWithBLOBs record){
		newPlayersVect.add(record);
	}
	
	public synchronized void updatePlayer(PlayerWithBLOBs record){
		updatePlayersVect.add(record);
	}
	
	public synchronized void pushSaving(Saving record){
		newSavingVect.add(record);
	}
	
	public synchronized void updateSaving(Saving record){
		updateSavingVect.add(record);
	}
	
	public synchronized void pushToplist(Toplist record){
		newToplistVect.add(record);
	}
	
	public synchronized void updateToplist(Toplist record){
		updateToplistVect.add(record);
	}	
	
	public void run() {
		while (1==1){
				synchronized(this)
				{
	        	if (newPlayersVect.size()>0){
		    		PlayerService service= new PlayerService();
		    		service.addPlayers(newPlayersVect);
		    		log.warn("batch add plsyer :"+newPlayersVect.size());
		    		newPlayersVect.clear(); 	        		
	        	}
	        	
	        	if (updatePlayersVect.size()>0){
		    		PlayerService service= new PlayerService();
		    		service.updatePlayers(updatePlayersVect);
		    		log.warn("batch update plsyer :"+updatePlayersVect.size());
		    		updatePlayersVect.clear(); 	        		
	        	}
	        	
	    		if (newSavingVect.size()>0){
		    		SavingService service2= new SavingService();
		    		service2.addSavings(newSavingVect);
		    		log.warn("batch add saving :"+newSavingVect.size());
		    		newSavingVect.clear();    	    			
	    		}
	        	
	    		if (updateSavingVect.size()>0){
		    		SavingService service2= new SavingService();
		    		service2.updateSavings(updateSavingVect);
		    		log.warn("batch update saving :"+updateSavingVect.size());
		    		updateSavingVect.clear();    	    			
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
