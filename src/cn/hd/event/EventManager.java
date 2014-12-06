package cn.hd.event;

public class EventManager extends java.util.TimerTask{
    private static EventManager uniqueInstance = null;  
	private Boolean isStart;
	
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
		System.out.println("EventManager start....");
		
		isStart = true;
		
		java.util.Timer timer = new java.util.Timer(true);  
		timer.schedule(this, 3000,3000);   		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		System.out.println("eventMgr timer here");
	}
}
