package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cn.hd.base.BaseService;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.tools.SavingdataService;
import cn.hd.cf.tools.SavingdataService;


public class SavingManager extends MgrBase{
	private Map<Integer,String>	savingMap;
	private Map<Integer,Saving>	savingCfgMap;	
	private Map<Integer,List<Saving>>	savingsMap;
	private Vector<Saving>			newSavingVect;
	private Vector<Saving>			updateSavingVect;
	private Vector<Saving>			deleteSavingVect;
	Saving savingCfg;
	private DataThread dataThread;
	
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
    
    public void init(){
    	savingCfgMap = new HashMap<Integer,Saving>();
    	SavingdataService savingdataService = new SavingdataService();
    	List<Saving> data = savingdataService.findSavings();
    	for (int i=0;i<data.size();i++){
    		Saving saving = data.get(i);
    		if (!savingCfgMap.containsKey(saving.getId()))
    			savingCfgMap.put(saving.getId(), saving);
    	}
    	newSavingVect = new Vector<Saving>();
    	updateSavingVect = new Vector<Saving>();
    	deleteSavingVect = new Vector<Saving>();

    	savingMap = new HashMap<Integer,String>();
    	
    	dataThread = new DataThread();
    	dataThread.start();
    	
    	savingsMap = new HashMap<Integer,List<Saving>>();
    	
    	SavingService savingService = new SavingService();
    	List<Saving> svs = savingService.findAll();
    	for (int i=0;i<svs.size();i++){
    		Saving s = svs.get(i);
    		List<Saving> list = savingsMap.get(s.getPlayerid());
    		if (list==null){
    			list = new ArrayList<Saving>();
    			savingsMap.put(s.getPlayerid(), list);
    		}
    		list.add(s);
    	}
    }
    
    public synchronized boolean updateLiveSaving(Saving record){
    	record.setItemid(1);
    	return updateSavingAmount(record);
    }
    
    public synchronized boolean updateSavingAmount(Saving record){
    	List<Saving> list = savingsMap.get(record.getPlayerid());
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
    		dataThread.updateSaving(s);
//    		updateSavingVect.add(s);
    		return true;
    	}
    	return false;
    }
    
    public synchronized boolean updateSaving(int playerId,Saving record){
    	List<Saving> list = savingsMap.get(playerId);
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
    	
    	updateSavingVect.add(record);
    	
    	list.add(record);
    	return true;
    }
    
    public synchronized Saving getSaving(int playerId,int itemid){
		Saving saving = null;
    	List<Saving> list = savingsMap.get(playerId);
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
    
    public synchronized String getSavings(int playerId){
		String jsonstr = null;
		if (savingMap.containsKey(playerId))
		jsonstr = savingMap.get(playerId);
		else {
			SavingService service = new SavingService();
			List<Saving> list = service.getDBSavings(playerId);
			jsonstr = BaseService.beanListToJson(list, Saving.class);
			savingMap.put(playerId, jsonstr);
		}
		return jsonstr;
	}

    
    public synchronized List<Saving> getSavingList(int playerId){
		return savingsMap.get(playerId);
	}
    
	public synchronized boolean deleteSaving(int playerId,Saving record){
		List<Saving> list = savingsMap.get(playerId);
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
		if (found){
			System.out.println("删除后json: "+list.size());
			deleteSavingVect.add(record);
			return true;
		}
		return false;
	}

	public synchronized boolean addSaving(int playerId,Saving record){
		List<Saving> list = savingsMap.get(playerId);
		boolean found = false;
		if (list==null){
			list = new ArrayList<Saving>();
			savingsMap.put(playerId, list);
		}else {
			for (int i=0;i<list.size();i++){
				if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
					found = true;
					break;
				}
			}
		}
		if (found){
			return false;
		}
		list.add(record);
//		newSavingVect.add(record);
		dataThread.pushSaving(record);
		return true;
	}

	public synchronized void update(){
    	tick ++;
    	if (newSavingVect.size()>BATCH_COUNT||tick%UPDATE_PERIOD_BATCH==0){
    		SavingService service= new SavingService();
    		service.addSavings(newSavingVect);
    		log.warn("batch add savings:"+newSavingVect.size());
    		newSavingVect.clear();
    	}
    	
    	if (updateSavingVect.size()>BATCH_COUNT||tick%UPDATE_PERIOD_BATCH==0){
    		SavingService service= new SavingService();
    		service.updateSavings(updateSavingVect);
    		log.warn("batch update savings:"+updateSavingVect.size());
    		updateSavingVect.clear();
    	}    	
    	
    	if (deleteSavingVect.size()>BATCH_COUNT||tick%UPDATE_PERIOD_BATCH==0){
    		SavingService service= new SavingService();
    		service.removeSavings(deleteSavingVect);
    		log.warn("batch remove savings:"+deleteSavingVect.size());
    		deleteSavingVect.clear();
    	}      	
    	
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
