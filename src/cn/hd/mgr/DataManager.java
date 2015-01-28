package cn.hd.mgr;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;
import cn.hd.util.RedisClient;

public class DataManager {
	private int UPDATE_PERIOD = 2;		//20*60: 一小时
	public boolean useJedis;
	public Jedis jedis;
	private RedisClient redisClient;	
	private ToplistService toplistService;
	private PlayerService playerService;
	private SavingService savingService;
	private InsureService insureService;
	private StockService stockService;
	private int tick = 0;
	private int nextPlayerId;
	
    private static DataManager uniqueInstance = null;  
	
    public static DataManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new DataManager();  
        }  
        return uniqueInstance;  
     } 
    
    public DataManager(){
    	nextPlayerId = 0;
    	jedis = null;
     }
	
	public int assignNextId(){
		nextPlayerId++;
		return nextPlayerId;
	}
	
    public void init(){
    	useJedis = false;
    	if (useJedis==true){
    		redisClient = new RedisClient();
    		jedis = redisClient.jedis;
    	}
		
		playerService = new PlayerService();
		nextPlayerId = playerService.initData(jedis);
		
    	savingService = new SavingService();
    	savingService.initData(jedis);
    	
    	insureService = new InsureService();
    	insureService.initData(jedis);
    	
    	stockService = new StockService();    	
    	stockService.initData(jedis);
    	
    	toplistService = new ToplistService();
    	toplistService.initData(jedis);
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
    	stmgr.init();
    	boolean ex = stmgr.getPlayerService().have("啊啊啊");
    	System.out.println(ex);
    }
}
