package cn.hd.mgr;

import java.util.List;

import cn.hd.cf.model.Event;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;

public class DataManager {
	private int UPDATE_PERIOD = 2;		//20*60: 一小时
	private ToplistService toplistService;
	private PlayerService playerService;
	private SavingService savingService;
	private InsureService insureService;
	private StockService stockService;
	private int tick = 0;
	
    private static DataManager uniqueInstance = null;  
	
    public static DataManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new DataManager();  
        }  
        return uniqueInstance;  
     } 
    
    public DataManager(){
    	playerService = new PlayerService();
    	toplistService = new ToplistService();
    	savingService = new SavingService();
    	insureService = new InsureService();
    	stockService = new StockService();
     }
  
    public ToplistService getToplistService() {
		return toplistService;
	}

	public void setToplistService(ToplistService toplistService) {
		this.toplistService = toplistService;
	}

	public PlayerService getPlayerService() {
		System.out.println("跑的是这里:::");
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public SavingService getSavingService() {
		return savingService;
	}

	public void setSavingService(SavingService savingService) {
		this.savingService = savingService;
	}

	public InsureService getInsureService() {
		return insureService;
	}

	public void setInsureService(InsureService insureService) {
		this.insureService = insureService;
	}

	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	public void update(){
    	tick ++;
    	if (tick%UPDATE_PERIOD==0){
    		playerService.DBCommit();
    		insureService.DBCommit();
    		savingService.DBCommit();
    		stockService.DBCommit();
    		toplistService.DBCommit();
    	}
	}
    
    public static void main(String[] args) {
    	DataManager stmgr = DataManager.getInstance();
//    	for (int i=0;i<1000;i++){
//    		long id = stmgr.assignPlayerId();
//    		System.out.println("申请ID:"+id);
//    	}
    	Event aa = new Event();
    	aa.setDesc("qqqqqqqqq");
    	System.out.println("申请ID:"+aa.toString());
    	//stmgr.saveAction.pushLive(47, 10000);
    	//stmgr.updateToplist();
    	ToplistService toplistService = new ToplistService();
    	List<Toplist> list = toplistService.findByType(0);
    	int a = toplistService.findCountByGreaterMoney(39,0,0);
    	a = 10;
    	//int b = toplistService.findCountByGreaterMoney(0,199);
    }
}
