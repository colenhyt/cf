package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Quest;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.tools.InitdataService;

import com.alibaba.fastjson.JSON;

public class DataManager extends MgrBase {
	protected Logger log = Logger.getLogger(getClass());
	public List<String> pps = new ArrayList<String>();
	private final int idStep = 100;
	private DBDataSaver saver;
	private int currMaxPlayerId = -1;

	private LoginAction loginAction;
	private Init init;

	public Init getInit() {
		return init;
	}

	public Map<String, Integer> playerIdMaps;
	public Map<Integer, Player> playerMaps;
	private int nextPlayerId;

	private static DataManager uniqueInstance = null;

	public static DataManager getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new DataManager();
		}
		return uniqueInstance;
	}

	public DataManager() {
		nextPlayerId = 0;
		saver = new DBDataSaver();
		saver.start();
	}

	public synchronized int assignNextId() {
		if (currMaxPlayerId==-1||nextPlayerId+1>currMaxPlayerId){
			Jedis jedis = jedisClient.getJedis();
			if (jedis==null){
				log.error("could not get redis,redis may not be run");
				return -1;
			}		
			String guidplayer = jedis.get(super.DATAKEY_GUID_PLAYER);
			if (guidplayer!=null){
				currMaxPlayerId = Integer.valueOf(guidplayer)+idStep;
			}else
				currMaxPlayerId = idStep;
			jedis.set(super.DATAKEY_GUID_PLAYER,String.valueOf(currMaxPlayerId));
			nextPlayerId = currMaxPlayerId - idStep;
			log.warn("reset guid id "+currMaxPlayerId);
			jedisClient.returnResource(jedis);
		}
		
		nextPlayerId++;
		return nextPlayerId;
	}

	public synchronized String login(String openId,String playerName,int sex,String settingStr,HttpServletRequest request) {
//		String loginStr = settingStr+",ip:"+loginAction.getIpAddress(request);
//		log.warn("login: "+loginStr);
		Player pp = new Player();
		pp.setPlayername(playerName);
		pp.setOpenid(openId);
		pp.setSex((byte)sex);
		loginAction.setPlayer(pp);
		return loginAction.login();
	}

	public synchronized String getData(int playerid, int typeid) {
		String data = "";
		if (typeid == 1) {
			data = get_saving(playerid);
		} else if (typeid == 2) {
			data = get_insure(playerid);
		} else if (typeid == 3) {
			data = get_stock(playerid);
		} else if (typeid == 4) {
			int top = get_top(playerid);
			data = String.valueOf(top);
		}
		return data;
	}

	public synchronized String get_saving(int playerid) {
		Map<Integer, Saving> data = loginAction.findUpdatedSavings(playerid);
		return JSON.toJSONString(data);
	}


	public String get_info(int playerid){
		Player player = this.findPlayer(playerid);
		if (player==null)
			return null;
		
		return loginAction.getPlayerJsonData(player);
	}
	
	public synchronized String get_insure(int playerid) {
		Map<Integer, Insure> data = loginAction.findUpdatedInsures(playerid);
		return JSON.toJSONString(data);
	}

	public synchronized String get_stock(int playerid) {
		Map<Integer, List<Stock>> data = StockManager.getInstance()
				.findMapStocks(playerid);
		return JSON.toJSONString(data);
	}

	private synchronized boolean isSameDay(Date date){
		Date now = new Date();
		if (date==null||now.getYear()!=date.getYear()||now.getMonth()!=date.getMonth()||now.getDay()!=date.getDay()){
			return false;
		}		
		return true;
	}
	
	public synchronized String get_quest(int playerid){
		Player p = findPlayer(playerid);
		if (p==null) return "";
		//随机任务两个:
		int questcount = 0;
		Date lastlogin = p.getLastlogin();
		boolean istoday = isSameDay(p.getLastlogin());
		String queststr = p.getQuestStr();
		int qid = -1;
		if (queststr==null){
			if (!istoday){
				questcount = 2;
			}
		}else {
			List<Quest> ql = JSON.parseArray(queststr, Quest.class);
			//只有一个任务, 隔天，再分配一个:
			if (ql.size()==1){
				qid = ql.get(0).getId();
				if (istoday==false){
					questcount = 1;
				}
			}
			questcount -= ql.size();
		}		for (int i=0;i<questcount;i++){
			
		}
		return "";
	}
	
	public synchronized int get_top(int playerid) {
		float fMm = loginAction.calculatePlayerMoney(playerid);

		ToplistManager.getInstance().load();
		int top = ToplistManager.getInstance().findCountByGreaterMoney(
				playerid, 0, fMm);
		// 800ms/1k
		return top + 1;
	}

	public synchronized boolean addPlayer(PlayerWithBLOBs player) {
		int playerid = player.getPlayerid();
		Player pp = findPlayer(playerid);
		if (pp!=null)
			return false;

		playerMaps.put(playerid, player);
		playerIdMaps.put(player.getPlayername(), playerid);
		
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.push(player);
		saver.add(player);
		return true;
	}

	public synchronized Player findPlayer(String playerName) {
		Integer playerid = playerIdMaps.get(playerName);
		if (playerid == null) {
			Jedis jedis = jedisClient.getJedis();
			String idstr = jedis.hget(super.DATAKEY_PLAYER_ID, playerName);
			jedisClient.returnResource(jedis);
			if (idstr!=null){
				playerid = Integer.valueOf(idstr);
			}else
				return null;
		}
		return findPlayer(playerid);
	}

	public synchronized Player findPlayer(int playerid) {
		Player player = playerMaps.get(playerid);
		if (player==null){
			Jedis jedis = jedisClient.getJedis();
			String itemstr = jedis.hget(super.DATAKEY_PLAYER, String.valueOf(playerid));
			jedisClient.returnResource(jedis);			
			if (itemstr!=null){
				player = (PlayerWithBLOBs)JSON.parseObject(itemstr,PlayerWithBLOBs.class);
				log.warn("find player :"+player.getPlayerid());
				playerMaps.put(playerid, player);
				if (!playerIdMaps.containsKey(player.getPlayername())){
					playerIdMaps.put(player.getPlayername(), playerid);
				}
			}
		}
		return player;
	}

	public synchronized boolean updatePlayer(PlayerWithBLOBs player) {
		Player pp = findPlayer(player.getPlayerid());
		if (pp != null) {
			pp.setExp(player.getExp());
			DataThread dataThread = dataThreads.get(player.getPlayerid()%dataThreads.size());
			dataThread.updatePlayer(pp);
			return true;
		}
		return false;
	}

	public synchronized void update(int playerid,int type){
		Player p = findPlayer(playerid);
		if (p==null) return;
		
		switch (type){
		case 0:
			p.setLastlogin(new Date());
			addSignin(playerid);
			break;
		case 1:
			p.setQuestdonecount(p.getQuestdonecount()+1);
			if (p.getQuestdonecount()==2){
				addDoneQuest(playerid);
			}
			p.setQuestDoneTime(new Date());
			if (p.getQuestdonecount()==3){
				p.setQuestdonecount(1);
			}
			break;
		case 2:
			p.setOpenstock((byte)1);
		}
		log.warn("update:"+playerid+",t:"+type);
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.updatePlayer(p);
	}
	
	public synchronized void addSignin(int playerid) {
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.addSignin(playerid);
	}

	public synchronized void addDoneQuest(int playerid) {
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.addDoneQuest(playerid);
	}

	public synchronized boolean updateZan(int playerid, int zan) {
		Player pp = findPlayer(playerid);
		if (pp != null) {
			pp.setZan(zan);
			DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
			dataThread.updatePlayer(pp);
			return true;
		}
		return false;
	}

	public synchronized List<String> getplayers() {
		Jedis jedis = jedisClient.getJedis();
		return jedis.hvals(super.DATAKEY_PLAYER);
	}
	
	private synchronized void load() {
		playerIdMaps.clear();
		playerMaps.clear();
		nextPlayerId = 0;
//		PlayerService playerService = new PlayerService();
//		List<PlayerWithBLOBs> players = playerService.findAll();
//		for (int i = 0; i < players.size(); i++) {
//			PlayerWithBLOBs player = players.get(i);
		Jedis jedis = jedisClient.getJedis();
		if (jedis==null){
			log.error("could not get redis,redis may not be run");
			return;
		}
		String guidplayer = jedis.get(super.DATAKEY_GUID_PLAYER);
		if (guidplayer!=null){
			log.info("start playerid "+guidplayer);
		}else
			log.info("start playerid "+currMaxPlayerId);
			
		List<String> items = jedis.hvals(super.DATAKEY_PLAYER);
		for (String item:items){
			PlayerWithBLOBs player = (PlayerWithBLOBs)JSON.parseObject(item,PlayerWithBLOBs.class);
			playerMaps.put(player.getPlayerid(), player);
			playerIdMaps.put(player.getPlayername(), player.getPlayerid());
		}
		jedisClient.returnResource(jedis);
		log.warn("load all players :" + items.size());

	}

	public void init() {
		loginAction = new LoginAction();

		InitdataService initdataService = new InitdataService();
		try {
			init = initdataService.findInit();
		}catch (Exception e){
			log.warn("could not connect mysql");
		}


		playerIdMaps = Collections
				.synchronizedMap(new HashMap<String, Integer>());
		playerMaps = Collections
				.synchronizedMap(new HashMap<Integer, Player>());
		
		this.load();
	}

	public static void main(String[] args) {
		DataManager stmgr = DataManager.getInstance();
		stmgr.init();
		stmgr.findPlayer(1335);
		stmgr.findPlayer("ppnane");
		PlayerWithBLOBs pp = new PlayerWithBLOBs();
		pp.setPlayerid(33);
		stmgr.addPlayer(pp);
//		SavingManager.getInstance().init();
//		InsureManager.getInstance().init();
//		StockManager.getInstance().init();
//		ToplistManager.getInstance().init();
//		stmgr.init();

	}
}
