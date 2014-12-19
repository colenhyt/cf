package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hd.cf.model.Saving;
import cn.hd.cf.service.SavingService;

public class PlayerManager {
	public static int DAY_SECOND = 3600*24;
	private int STOCK_UPDATE_PERIOD = 1;
	private int STOCK_SAVE_PERIOD = 3600;
	private SavingService savingService;
	private int tick = 0;
	
    private static PlayerManager uniqueInstance = null;  
	
    public static PlayerManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new PlayerManager();  
        }  
        return uniqueInstance;  
     } 
    
    public PlayerManager(){
    	savingService = new SavingService();
    	
     }
  
    public void update(){
    	tick ++;
		//stock price update:
		if (tick%STOCK_UPDATE_PERIOD==0)
		{
			Date time = new Date(); 
			
			List<Saving> savings = savingService.findAll();
			List<Saving> updateSavings = new ArrayList<Saving>();
			for (int i=0;i<savings.size();i++){
				Saving saving = savings.get(i);
				long duration = time.getTime() - saving.getCreatetime().getTime();
				if (duration>DAY_SECOND){
					saving.setAmount(saving.getAmount()*(1+saving.getRate()/100));
					updateSavings.add(saving);
				}
			}
			savingService.updateSavings(updateSavings);
	    }		
		
		//数据库保存:
		if (tick%STOCK_SAVE_PERIOD==0){

		}
	}
    
    public static void main(String[] args) {
    	PlayerManager stmgr = PlayerManager.getInstance();
    	stmgr.update();
    }
}
