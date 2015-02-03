package cn.hd.mgr;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import cn.hd.base.Bean;
import cn.hd.cf.action.SavingAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Savingdata;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.ToplistService;
import cn.hd.cf.tools.InitdataService;
import cn.hd.cf.tools.SavingdataService;
import cn.hd.util.MD5;
import cn.hd.util.RedisClient;
import cn.hd.util.StringUtil;

import com.alibaba.fastjson.JSON;

public class DataManager {
	private int UPDATE_PERIOD = 20*30;		//20*60: 一小时
	public boolean useJedis;
	public Jedis jedis;
	public RedisClient redisClient;	
	private Init	init;
	private Map<Integer,Saving>		savingData;
	private Map<String,PlayerWithBLOBs> playerMaps;
	private List<Toplist>			currMonthList;
	private Vector<PlayerWithBLOBs>	newPlayersVect;
	private Vector<PlayerWithBLOBs>	updatePlayersVect;
	private Vector<Toplist>			updateToplistVect;
	private Vector<Saving>	newSavingVect;
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
	
	public String register(String playername,int accountid,byte sex)
	{
		String playerStr = null;
		synchronized(playerMaps) {
		boolean exist = playerMaps.containsKey(playername);
//		if (exist){
//			System.out.println("该玩家已存在:"+playername);
//			Message msg = new Message();
//			msg.setCode(RetMsg.MSG_PlayerNameIsExist);		//重名
//			JSONObject obj = JSONObject.fromObject(msg);
//			return obj.toString();
//		}
		Date time = new Date(); 
		
		PlayerWithBLOBs playerBlob = new PlayerWithBLOBs();
		playerBlob.setPlayername(playername);
		playerBlob.setAccountid(accountid);
		playerBlob.setSex(sex);
		playerBlob.setCreatetime(time);
		playerBlob.setZan(0);
		String pwd = StringUtil.getRandomString(10);
		playerBlob.setPwd(MD5.MD5(pwd));
		playerBlob.setPlayerid(assignNextId());
		
		ToplistService toplistService = new ToplistService();
		int top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),0,0);
		playerBlob.setWeektop(top+1);
//		top = toplistService.findCountByGreaterMoney(playerBlob.getPlayerid(),1,0);
//		playerBlob.setMonthtop(top+1);	
		
		if (init!=null){
			playerBlob.setExp(init.getExp());
		}
		
		 playerMaps.put(playerBlob.getPlayername(), playerBlob);
			
		 if (init!=null&&init.getMoney()>0){
			 Map<Integer,Saving> savings = new HashMap<Integer,Saving>();
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
		savings.put(savingCfg.getId(), saving);
		playerBlob.setSaving(JSON.toJSONString(savings));
		 }
		  JSONObject obj = JSONObject.fromObject(playerBlob);
		  playerStr = obj.toString();
		newPlayersVect.add(playerBlob);
		}
		
		
		System.out.println("新增玩家:"+playerMaps.size());
		return playerStr;
	}
	
	public PlayerWithBLOBs findPlayer(int playerid){
		PlayerWithBLOBs player = null;
		Collection<PlayerWithBLOBs> l = playerMaps.values();
		for (Iterator<PlayerWithBLOBs> iter = l.iterator(); iter.hasNext();) {
			PlayerWithBLOBs pp = (PlayerWithBLOBs)iter.next();
			if (pp.getPlayerid()==playerid){
				player = pp;
			}
		}	
		return player;
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
//		ToplistService toplistService = new ToplistService();
//		LoginAction.initToplist(player, toplistService);
		
		float margin = StockManager.getInstance().getMarginSec();
		player.setQuotetime(margin);
		player.setLastlogin(new Date());
		
		JSONObject obj = JSONObject.fromObject(player);	
		return obj.toString();
	}
	
	public void onMoneyChanged(int playerid,float fChangedMoney){
		String playername = null;
		for (int i=0;i<currMonthList.size();i++){
			Toplist topi = currMonthList.get(i);
			if (topi.getPlayerid()==playerid){
				playername = topi.getPlayername();
				float newMoney = topi.getMoney().floatValue()+fChangedMoney;
				if (newMoney>0)
				 topi.setMoney(BigDecimal.valueOf(newMoney));
			}
		}
		if (playername==null){
			PlayerWithBLOBs player = findPlayer(playerid);
			if (player!=null)
			 playername = player.getPlayername();
		}
		
		Toplist toplist = new Toplist();
		toplist.setPlayerid(playerid);
		toplist.setPlayername(playername);
		toplist.setMoney(BigDecimal.valueOf(fChangedMoney));
		updateToplistVect.add(toplist);
	}
	
	public void updatePlayer(PlayerWithBLOBs player)
	{
		PlayerWithBLOBs cachePlayer = playerMaps.get(player.getPlayername());
		if (cachePlayer==null){
			return;
		}
		cachePlayer.setExp(player.getExp());
		cachePlayer.setSex(player.getSex());
		cachePlayer.setQuest(player.getQuest());
		cachePlayer.setOpenstock(player.getOpenstock());
		System.out.println("更新内存玩家数据:"+player.getExp());
		player.setPwd(cachePlayer.getPwd());
		player.setAccountid(cachePlayer.getAccountid());
		player.setCreatetime(cachePlayer.getCreatetime());
		player.setZan(cachePlayer.getZan());
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
		List<PlayerWithBLOBs> players = playerService.findAll();
		for (int i=0; i<players.size();i++){
			PlayerWithBLOBs player = players.get(i);
			playerMaps.put(player.getPlayername(), player);
			if (player.getPlayerid()>nextPlayerId)
				nextPlayerId = player.getPlayerid();			
		}
		System.out.println("load players :"+nextPlayerId);
		
    	ToplistService toplistService = new ToplistService();
    	currMonthList = toplistService.findCurrMonthToplists();
    	
    	
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
			playerService.updateByKey(updatePlayersVect.get(i));
		}
		playerService.DBCommit();
		System.out.println("更新玩家:"+updatePlayersVect.size());
		updatePlayersVect.clear();
	}

	private void updateToplists(){
		ToplistService toplistService = new ToplistService();
		for (int i=0;i<updateToplistVect.size();i++){
			Toplist toplist = updateToplistVect.get(i);
			toplistService.changeToplist(toplist.getPlayerid(),toplist.getPlayername(),toplist.getMoney().doubleValue());
		}
		toplistService.DBCommit();
		System.out.println("更新排行榜:"+updatePlayersVect.size());
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
//    	if (newPlayersVect.size()>0||tick%UPDATE_PERIOD==0){
//    		pushPlayers();
//    	}
//    	
//    	if (updatePlayersVect.size()>0||tick%UPDATE_PERIOD==0){
//    		updatePlayers();
//    	}
//    	
//    	if (newSavingVect.size()>0||tick%UPDATE_PERIOD==0){
//    		pushSavings();
//    	}
    	if (updateToplistVect.size()>0||tick%UPDATE_PERIOD==0){
    		updateToplists();
    	}
    	
	}
    
    public static void main(String[] args) {
    	DataManager stmgr = DataManager.getInstance();
    	stmgr.init();
    }
}
