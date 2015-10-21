package cn.hd.mgr;

import java.util.Vector;

import org.apache.log4j.Logger;

import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;

public class DataThread extends Thread {
	private Vector<PlayerWithBLOBs> newPlayersVect;
	private Vector<Saving>			newSavingVect;
	private Vector<PlayerWithBLOBs>	updatePlayersVect;	
	private int tick;
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public DataThread(){
		newPlayersVect = new Vector<PlayerWithBLOBs>();
		updatePlayersVect = new Vector<PlayerWithBLOBs>();
		newSavingVect = new Vector<Saving>();
	}
	
	public synchronized void push(PlayerWithBLOBs record){
//		newPlayersVect.add(record);
//		tick++;
	}
	
	public synchronized void updatePlayer(PlayerWithBLOBs record){
//		updatePlayersVect.add(record);
//		tick++;
	}
	
	public synchronized void pushSaving(Saving record){
//		newSavingVect.add(record);
//		tick++;
	}
	
	public void run() {
		while (1==1){
//			synchronized(this)
			{
	        if (tick>0){
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
		
	    		tick = 0;
	        }else{
	        	try {
					super.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
			}
		}

    }	
}
