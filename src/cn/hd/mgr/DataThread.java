package cn.hd.mgr;

import java.util.Vector;

import org.apache.log4j.Logger;

import cn.hd.cf.model.Insure;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;

public class DataThread extends Thread {
	private Vector<PlayerWithBLOBs> newPlayersVect;
	private Vector<PlayerWithBLOBs>	updatePlayersVect;	
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
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public DataThread(){
		newPlayersVect = new Vector<PlayerWithBLOBs>();
		updatePlayersVect = new Vector<PlayerWithBLOBs>();
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
	
	public synchronized void pushSaving(Saving record){
		newSavingVect.add(record);
	}
	
	public synchronized void updateSaving(Saving record){
		updateSavingVect.add(record);
	}
	
	public synchronized void deleteSaving(Saving record){
		deleteSavingVect.add(record);
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
	        	
	    		if (deleteSavingVect.size()>0){
		    		SavingService service2= new SavingService();
		    		service2.deleteSavings(deleteSavingVect);
		    		log.warn("batch delete saving :"+deleteSavingVect.size());
		    		deleteSavingVect.clear();    	    			
	    		}
	    		
	    		if (updateSavingVect.size()>0){
		    		SavingService service2= new SavingService();
		    		service2.updateSavings(updateSavingVect);
		    		log.warn("batch update saving :"+updateSavingVect.size());
		    		updateSavingVect.clear();    	    			
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
