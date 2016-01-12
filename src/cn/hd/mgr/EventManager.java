package cn.hd.mgr;

import org.apache.log4j.Logger;


public class EventManager extends java.util.TimerTask{
	public static int TICK_PERIOD = 3000;
    private static EventManager uniqueInstance = null;  
	private Boolean isStart;
	protected Logger  log = Logger.getLogger(getClass()); 
	
    public static EventManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new EventManager();  
        }  
        return uniqueInstance;  
     } 
    
	public EventManager(){
		isStart = false;
	}
	
	public void start(){
		if (isStart) return;
		
		log.info("EventManager start....");
		
		isStart = true;
		
		java.util.Timer timer = new java.util.Timer(true);  
		timer.schedule(this, TICK_PERIOD,TICK_PERIOD);   		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		System.out.println("eventMgr timer here");
		StockManager.getInstance().update();
//		SavingManager.getInstance().update();
//		DataManager.getInstance().update();
	}
}
