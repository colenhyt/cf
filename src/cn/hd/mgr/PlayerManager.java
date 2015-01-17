package cn.hd.mgr;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hd.cf.action.SavingAction;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.ToplistService;

public class PlayerManager {
	private int WEEK_UPDATE_PERIOD = 20;		//20*60: 一小时
	private ToplistService toplistService;
	private PlayerService playerService;
	private Map<Integer,PlayerWithBLOBs> playerMap;
	public SavingAction saveAction;
	private int tick = 0;
	
    private static PlayerManager uniqueInstance = null;  
	
    public static PlayerManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new PlayerManager();  
        }  
        return uniqueInstance;  
     } 
    
    public PlayerManager(){
    	playerService = new PlayerService();
    	saveAction = new SavingAction();
    	playerMap = new HashMap<Integer,PlayerWithBLOBs>();
    	toplistService = new ToplistService();
    	updateToplist();
     }
  
    public void updateToplist(){
		Date d1 = new Date();
	      Calendar cl = Calendar. getInstance();
	      cl.setTime(d1);
	      cl.setFirstDayOfWeek(Calendar.MONDAY);
	      System.out.println("装载玩家数据到内存");
	      
	      //更新全部人最新的财富值:
	      List<PlayerWithBLOBs> all = playerService.findAll();
	      for (int i=0;i<all.size();i++){
    		PlayerWithBLOBs pp = all.get(i);
	    	  playerMap.put(pp.getPlayerid(), pp);
//    		float money = saveAction.calculatePlayerMoney(pp.getPlayerid());
//    		pp.setMoney(money);
//    		toplistService.updateCurrData(pp.getPlayerid(),pp.getPlayername(),money);
    	  }
    }
    
    public PlayerWithBLOBs findPlayer(int playerid){
    	PlayerWithBLOBs pp = playerMap.get(playerid);
    	return pp;
    }
    
    public boolean addPlayer(PlayerWithBLOBs player){
    	boolean exist = playerMap.containsKey(player.getPlayerid());
    	if (exist==false)
    		playerMap.put(player.getPlayerid(), player);
    	
    	return true;
    }
    
    public void update(){
    	tick ++;
//    	if (tick%WEEK_UPDATE_PERIOD==0){
//    		updateToplist();
//    	}
	}
    
    public static void main(String[] args) {
    	PlayerManager stmgr = PlayerManager.getInstance();
    	stmgr.saveAction.pushLive(47, 10000);
    	//stmgr.updateToplist();
    	ToplistService toplistService = new ToplistService();
    	List<Toplist> list = toplistService.findByType(0);
    	int a = toplistService.findCountByGreaterMoney(39,0,0);
    	a = 10;
    	//int b = toplistService.findCountByGreaterMoney(0,199);
    }
}
