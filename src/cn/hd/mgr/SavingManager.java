package cn.hd.mgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.base.BaseService;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.tools.SavingdataService;


public class SavingManager{
	private Map<Integer,String>	savingMap;
	Saving savingCfg;
	
    public Saving getSavingCfg() {
		return savingCfg;
	}

	private static SavingManager uniqueInstance = null;  
	
    public static SavingManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new SavingManager();  
        }  
        return uniqueInstance;  
     } 
    
    public void init(){
    	SavingdataService savingdataService = new SavingdataService();
    	savingCfg = savingdataService.findSaving(1);
    }
    
    public SavingManager(){
    	savingMap = new HashMap<Integer,String>();
     }
    

    public synchronized boolean updateSaving(int playerId,Saving record){
    	String jsonstr = getSavings(playerId);
    	List<Saving> list = BaseService.jsonToBeanList(jsonstr, Saving.class);
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
		jsonstr = BaseService.beanListToJson(list,Saving.class);
		savingMap.put(playerId, jsonstr);
    	return true;
    }
    
    public synchronized Saving getSaving(int playerId,int itemid){
		String jsonstr = getSavings(playerId);
		Saving saving = null;
    	List<Saving> list = BaseService.jsonToBeanList(jsonstr, Saving.class);
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

	public synchronized boolean deleteSaving(int playerId,Saving record){
		String jsonstr = getSavings(playerId);
		List<Saving> list = BaseService.jsonToBeanList(jsonstr, Saving.class);
		boolean found = false;
		for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
				list.remove(i);
				found = true;
				break;
			}
		}
		if (found){
			jsonstr = BaseService.beanListToJson(list,Saving.class);
			System.out.println("删除后json: "+jsonstr);
			savingMap.put(playerId, jsonstr);
			return true;
		}
		return false;
	}

	public synchronized boolean addSaving(int playerId,Saving record){
		String jsonstr = getSavings(playerId);
		List<Saving> list = BaseService.jsonToBeanList(jsonstr, Saving.class);
		boolean found = false;
		for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().intValue()==record.getItemid().intValue()){
				found = true;
				break;
			}
		}
		if (found){
			return false;
		}
		list.add(record);
		jsonstr = BaseService.beanListToJson(list,Saving.class);
		savingMap.put(playerId, jsonstr);
		return true;
	}

	public static void main(String[] args) {
		
//    	SavingManager stmgr = SavingManager.getInstance();
//    	Saving ss = new Saving();
//    	ss.setItemid(2);
//    	stmgr.addSaving(1, ss);
//    	Saving s2 = new Saving();
//    	s2.setPlayerid(1);
//    	s2.setItemid(2);
//    	stmgr.deleteSaving(1, s2);
		SavingService ss = new SavingService();
		Saving sa = new Saving();
		sa.setPlayerid(265);
		sa.setItemid(1);
		sa.setAmount((float)30);
		ss.update(sa);
    }
}
