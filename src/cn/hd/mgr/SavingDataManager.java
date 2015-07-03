package cn.hd.mgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.base.BaseService;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.SavingService;


public class SavingDataManager{
	private Map<Integer,String>	savingMap;
	
    private static SavingDataManager uniqueInstance = null;  
	
    public static SavingDataManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new SavingDataManager();  
        }  
        return uniqueInstance;  
     } 
    
    public SavingDataManager(){
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
		System.out.println("更新内存存款:  "+jsonstr);
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
		System.out.println("存款找这里::::");
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
    	SavingDataManager stmgr = SavingDataManager.getInstance();
    	Saving ss = new Saving();
    	ss.setItemid(2);
    	stmgr.addSaving(1, ss);
    	Saving s2 = new Saving();
    	s2.setPlayerid(1);
    	s2.setItemid(2);
    	stmgr.deleteSaving(1, s2);
    }
}
