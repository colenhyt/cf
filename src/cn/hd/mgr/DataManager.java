package cn.hd.mgr;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import cn.hd.cf.action.LoginAction;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.action.SavingAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Savingdata;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;
import cn.hd.cf.tools.InitdataService;
import cn.hd.cf.tools.SavingdataService;
import cn.hd.util.MD5;
import cn.hd.util.RedisClient;
import cn.hd.util.StringUtil;

public class DataManager {
	private int UPDATE_PERIOD = 20*30;		//20*60: 一小时
	public boolean useJedis;
	public Jedis jedis;
	public RedisClient redisClient;	
	private Init	init;
	private Map<Integer,Saving>		savingData;
	private Map<String,PlayerWithBLOBs> playerMaps;
	private Vector<PlayerWithBLOBs>	newPlayersVect;
	private Vector<PlayerWithBLOBs>	updatePlayersVect;
	private Vector<Saving>	newSavingVect;
	private ToplistService toplistService;
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
	
	public String register(String playername)
	{
		String playerStr = null;
		synchronized(playerMaps) {
		boolean exist = playerMaps.containsKey(playername);
//		if (exist){
//			System.out.println("该玩家已存在:"+playername);
//			return playerStr;
//		}
		Date time = new Date(); 
		
		PlayerWithBLOBs playerBlob = new PlayerWithBLOBs();
		playerBlob.setPlayername(playername);
		playerBlob.setAccountid(1);
		playerBlob.setCreatetime(time);
		String pwd = StringUtil.getRandomString(10);
		playerBlob.setPwd(MD5.MD5(pwd));
		playerBlob.setPlayerid(assignNextId());
		
		if (init!=null){
			playerBlob.setExp(init.getExp());
		}
		
		 playerMaps.put(playerBlob.getPlayername(), playerBlob);
			
		 if (init!=null&&init.getMoney()>0){
		Saving savingCfg = savingData.get(1);
		Saving saving = new Saving();
		saving.setName(savingCfg.getName());
		saving.setPeriod(savingCfg.getPeriod());
		saving.setRate(savingCfg.getRate());
		saving.setItemid(savingCfg.getId());
		saving.setQty(1);
		saving.setUpdatetime(time);
		saving.setCreatetime(time);
		saving.setType(savingCfg.getType());
		saving.setPlayerid(playerBlob.getPlayerid());
		saving.setAmount(Float.valueOf(init.getMoney().intValue()));
		newSavingVect.add(saving);
		 }
		  JSONObject obj = JSONObject.fromObject(playerBlob);
		  playerStr = obj.toString();
		newPlayersVect.add(playerBlob);
		}
		
		
		System.out.println("新增玩家:"+playerMaps.size());
		return playerStr;
	}
	
	public String login(String playerName){
		PlayerWithBLOBs player = playerMaps.get(playerName);
		if (player==null){
			PlayerService playerService = new PlayerService();
			player = playerService.findByName(playerName);
			if (player!=null){
				playerMaps.put(player.getPlayername(), player);				
			}
		}
		ToplistService toplistService = new ToplistService();
		LoginAction.initToplist(player, toplistService);
		
		float margin = StockManager.getInstance().getMarginSec();
		player.setQuotetime(margin);
		player.setLastlogin(new Date());
		
		JSONObject obj = JSONObject.fromObject(player);	
		return obj.toString();
	}
	
	public void updatePlayer(PlayerWithBLOBs player)
	{
		updatePlayersVect.add(player);
	}
	
    public void init(){
    	useJedis = false;
    	if (useJedis==true){
    		redisClient = new RedisClient();
    		jedis = redisClient.jedis;
    	}
		
    	newPlayersVect = new Vector<PlayerWithBLOBs>();
    	updatePlayersVect = new Vector<PlayerWithBLOBs>();
    	
    	newSavingVect = new Vector<Saving>();
    	
    	playerMaps = Collections.synchronizedMap(new HashMap<String,PlayerWithBLOBs>());
    	
    	InitdataService initdataService = new InitdataService();
    	init = initdataService.findInit();
    	
    	savingData = Collections.synchronizedMap(new HashMap<Integer,Saving>());
		try {
	    	SavingdataService savingdataService = new SavingdataService();
	    	Savingdata  sdata = savingdataService.findActive();
	    	String savingstr;
			savingstr = new String(sdata.getData(),"utf-8");
			List<Saving> list = savingdataService.jsonToBeanList(savingstr, Saving.class);
			for (int i=0;i<list.size();i++){
				savingData.put(list.get(i).getId(), list.get(i));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	PlayerService playerService = new PlayerService();
		nextPlayerId = playerService.initData(jedis);
		
		List<PlayerWithBLOBs> players = playerService.findAll();
		for (int i=0; i<players.size();i++){
			PlayerWithBLOBs player = players.get(i);
			playerMaps.put(player.getPlayername(), player);
		}
		System.out.println("load players :"+nextPlayerId);
		
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

	private void pushPlayers(){
		PlayerService playerService = new PlayerService();
		for (int i=0;i<newPlayersVect.size();i++){
			playerService.add(newPlayersVect.get(i));
		}
		playerService.DBCommit();
		System.out.println("持久化玩家:"+newPlayersVect.size());
		newPlayersVect.clear();
	}

	private void updatePlayers(){
		PlayerService playerService = new PlayerService();
		for (int i=0;i<updatePlayersVect.size();i++){
			playerService.add(updatePlayersVect.get(i));
		}
		playerService.DBCommit();
		System.out.println("更新玩家:"+updatePlayersVect.size());
		updatePlayersVect.clear();
	}
	
	private void pushSavings(){
		SavingService savingService = new SavingService();
		SavingAction savingAction = new SavingAction();
		for (int i=0;i<newSavingVect.size();i++){
			Saving saving = newSavingVect.get(i);
			savingService.add(saving);
			savingAction.playerTopUpdate(saving.getPlayerid());
		}
		savingService.DBCommit();
		System.out.println("持久化存款:"+newSavingVect.size());
		newSavingVect.clear();
	}
	
	public void update(){
    	tick ++;
    	if (newPlayersVect.size()>0||tick%UPDATE_PERIOD==0){
    		pushPlayers();
    	}
    	
    	if (updatePlayersVect.size()>0||tick%UPDATE_PERIOD==0){
    		updatePlayers();
    	}
    	
    	if (newSavingVect.size()>0||tick%UPDATE_PERIOD==0){
    		pushSavings();
    	}    	
	}
    
    public static void main(String[] args) {
    	DataManager stmgr = DataManager.getInstance();
    	stmgr.init();
    }
}
