package cn.hd.mgr;

import java.util.Vector;

import net.sf.json.JSONObject;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.action.SavingAction;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.Saving;
import cn.hd.util.HdTimer;

public class SavingDataManager extends HdTimer{
	private int ADD_PERIOD = 20*30;		//20*60: 一小时
	private Vector<Saving>	addSavingVect;
	private int tick = 0;
	
    private static SavingDataManager uniqueInstance = null;  
	
    public static SavingDataManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new SavingDataManager();  
        }  
        return uniqueInstance;  
     } 
    
    public SavingDataManager(){
    	try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	addSavingVect = new Vector<Saving>();
     }
	
	public String addSaving(Saving stock){
		addSavingVect.add(stock);
		Message msg = new Message();
		msg.setCode(RetMsg.MSG_OK);		//不存在
		JSONObject obj = JSONObject.fromObject(msg);
		return obj.toString();			
	}
	
	private void addSavings(){
		SavingAction savingAction = new SavingAction();
		for (int i=0;i<addSavingVect.size();i++){
			Saving saving = addSavingVect.get(i);
			savingAction.addSaving(saving);
		}
		System.out.println("购买股票:"+addSavingVect.size());
		addSavingVect.clear();
	}
		
	public void run(){
    	tick ++;
    	if (addSavingVect.size()>0||tick%ADD_PERIOD==0){
    		//addSavings();
    	}
    	//System.out.println("Saving run");
	}
    
    public static void main(String[] args) {
    	SavingDataManager stmgr = SavingDataManager.getInstance();
    }
}
