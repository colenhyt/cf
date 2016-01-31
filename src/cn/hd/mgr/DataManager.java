package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Quest;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Signin;
import cn.hd.cf.model.Stock;
import cn.hd.util.QuestLog;
import cn.hd.util.RedisClient;
import cn.hd.util.SigninLog;

import com.alibaba.fastjson.JSON;

public class DataManager extends MgrBase {
	protected Logger log = Logger.getLogger(getClass());
	public List<String> pps = new ArrayList<String>();
	private final int idStep = 100;
	private int currMaxPlayerId = -1;
	private Vector<RedisClient> redisClients;
	private Vector<Integer> signinMoneys;
	private Vector<Integer> signinExps;

	private LoginAction loginAction;
	private Init init;

	public Init getInit() {
		return init;
	}

	public Map<String, Integer> playerIdMaps;
	public Map<Integer, Player> playerMaps;
	public Map<Integer,Quest> questDataMap;
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
	}

	public synchronized int assignNextId() {
		if (currMaxPlayerId==-1||nextPlayerId+1>currMaxPlayerId){
			Jedis jedis = redisClients.get(0).getJedis();
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
			redisClients.get(0).returnResource(jedis);
		}
		
		nextPlayerId++;
		return nextPlayerId;
	}

	public synchronized String login(String openId,String playerName,int sex,int playerid,String settingStr,HttpServletRequest request) {
//		return null;
		String loginStr = settingStr+",ip:"+loginAction.getIpAddress(request);
		log.warn("login: loginStr: "+loginStr);
		Player pp = new Player();
		pp.setPlayername(playerName);
		pp.setOpenid(openId);
		pp.setSex((byte)sex);
		pp.setPlayerid(playerid);
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
	
	public synchronized int get_registerTop(float fMm) {
		ToplistManager.getInstance().load();
		int top = ToplistManager.getInstance().findTopCount(null, 0, fMm);
		// 800ms/1k
		return top+1;
	}
	
	public synchronized int get_top(int playerid) {
		float fMm = loginAction.calculatePlayerMoney(playerid);
		
		ToplistManager.getInstance().load();
		int top = ToplistManager.getInstance().findCountByGreaterMoney(
				playerid, 0, fMm);
		// 800ms/1k
		return top+1;
	}

	public synchronized boolean addPlayer(PlayerWithBLOBs player) {
		int playerid = player.getPlayerid();
//		Player pp = findPlayer(playerid);
//		if (pp!=null)
//			return false;

		playerMaps.put(playerid, player);
		playerIdMaps.put(player.getPlayername(), playerid);
		
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.push(player);
		//saver.add(player);
		//log.warn(System.currentTimeMillis());
		return true;
	}

	public synchronized Player findPlayer(String playerName) {
		long s = System.currentTimeMillis();
		Integer playerid = playerIdMaps.get(playerName);
		if (playerid != null) {
			return findPlayer(playerid);
		}
		
		Player player = null;
		int index = Math.abs(playerName.hashCode())%redisClients.size();
		RedisClient jedisClient = redisClients.get(index);
		Jedis jedis = jedisClient.getJedis();
		String idstr = jedis.hget(super.DATAKEY_PLAYER_ID, playerName);
		if (idstr!=null){
			playerid = Integer.valueOf(idstr);
			String itemstr = jedis.hget(super.DATAKEY_PLAYER, String.valueOf(playerid));
			player = (Player)JSON.parseObject(itemstr,Player.class);
//				log.warn("find player :"+player.getPlayerid());
			playerMaps.put(playerid, player);
			if (!playerIdMaps.containsKey(player.getPlayername())){
				playerIdMaps.put(player.getPlayername(), playerid);
			}				
		}
		jedisClient.returnResource(jedis);
		long cost = System.currentTimeMillis()-s;
		if (cost>10)
			log.warn("register find cost :"+cost+",name:"+playerName);
		return player;
	}

	public synchronized Player findPlayer(int playerid) {
		Player player = null;
		long s = System.currentTimeMillis();

//		if (player==null)
		{
			int index = playerid%redisClients.size();
			RedisClient jedisClient = redisClients.get(index);
			Jedis jedis = jedisClient.getJedis();
			String itemstr = jedis.hget(super.DATAKEY_PLAYER, String.valueOf(playerid));
			jedisClient.returnResource(jedis);			
			if (itemstr!=null){
				player = (Player)JSON.parseObject(itemstr,Player.class);
//				log.warn("find player :"+player.getPlayerid());
				playerMaps.put(playerid, player);
				if (!playerIdMaps.containsKey(player.getPlayername())){
					playerIdMaps.put(player.getPlayername(), playerid);
				}
			}
		}
		//log.warn("findid cost :"+(System.currentTimeMillis()-s));
		return playerMaps.get(playerid);
	}

	public synchronized void updatePlayerQuest(Player player) {
		DataThread dataThread = dataThreads.get(player.getPlayerid()%dataThreads.size());
		dataThread.updatePlayer(player);
	}
	
	public synchronized void update(int playerid,int type,String itemstr,String amountStr){
		Player p = findPlayer(playerid);
		if (p==null) return;
		
		switch (type){
		case 0:
			Date now = new Date();
			Date last = p.getLastlogin();
			if (last!=null&&last.getYear()==now.getYear()&&last.getMonth()==now.getMonth()&&last.getDay()==now.getDay()){
				return;
			}
			if (itemstr==null||itemstr.length()<=0)
				return;
			
			Integer days = Integer.valueOf(itemstr);
			if (days>signinMoneys.size())
				days = signinMoneys.size();
			int money = signinMoneys.get(days-1);
			int exp = signinExps.get(days-1);
			SavingManager.getInstance().updateLiveSaving(playerid,money);
			p.setExp(p.getExp()+exp);
			p.setLastlogin(new Date());
			log.warn("pid:"+playerid+" signin,days: "+days);
			this.addSignin(playerid);
			break;
		case 1:
			String queststr = p.getQuestStr();
			if (queststr==null||itemstr==null||itemstr.length()<=0) return;
			if (queststr.indexOf(itemstr)<0) return;
			
			SavingManager.getInstance().updateLiveSaving(playerid,(float)5000);
			
			queststr = queststr.replace(itemstr, "").replace(",","");
			log.warn("pid:"+playerid+" done quest "+itemstr);
			p.setQuestStr(queststr);
			if (queststr.length()<=0||queststr.split(",").length<=0){
				p.setQuestDoneTime(new Date());
				this.addDoneQuest(playerid);
				log.warn("pid:"+playerid+" done daily quest");
			}
			break;
		case 2:
			p.setOpenstock((byte)1);
			break;
		case 3:
			//todo: 这里加上正数的每日次数限制:
			float amount = Float.valueOf(amountStr);
			SavingManager.getInstance().updateLiveSaving(playerid,amount);
			log.warn("pid:"+playerid+" event amount update:"+amount);
			break;
			
		}
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.updatePlayer(p);
	}
	
	public synchronized void addSignin(int playerid) {
		Signin record = new Signin();
		record.setPlayerid(playerid);
		record.setCrdate(new Date());		
		SigninLog.getRootLogger().info(JSON.toJSONString(record));
	}

	public synchronized void addDoneQuest(int playerid) {
		Quest record = new Quest();
		record.setPlayerid(playerid);
		record.setCrdate(new Date());		
		QuestLog.getRootLogger().info(JSON.toJSONString(record));
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
		Jedis jedis = redisClients.get(0).getJedis();
		return jedis.hvals(super.DATAKEY_PLAYER);
	}


	public void init() {
		loginAction = new LoginAction();

		signinMoneys = new Vector<Integer>();
		signinMoneys.add(10000);
		signinMoneys.add(15000);
		signinMoneys.add(20000);
		signinMoneys.add(25000);
		signinMoneys.add(30000);
		signinMoneys.add(35000);
		signinMoneys.add(40000);
		
		signinExps = new Vector<Integer>();
		signinExps.add(5);
		signinExps.add(10);
		signinExps.add(15);
		signinExps.add(20);
		signinExps.add(25);
		signinExps.add(30);
		signinExps.add(35);
		
		Jedis j3 = jedisClient3.getJedis();
		
		String strinit = j3.get(MgrBase.DATAKEY_DATA_INIT);
		init = JSON.parseObject(strinit, Init.class);

		log.warn("find init:"+strinit);
		
		questDataMap = Collections
				.synchronizedMap(new HashMap<Integer, Quest>());
		List<String> queststrs = j3.hvals(MgrBase.DATAKEY_DATA_QUEST);
		for (String qstr:queststrs){
			Quest q = JSON.parseObject(qstr,Quest.class);
			questDataMap.put(q.getId(), q);
		}
		log.warn("init questdata "+questDataMap.size());
		
		playerIdMaps = Collections
				.synchronizedMap(new HashMap<String, Integer>());
		playerMaps = Collections
				.synchronizedMap(new HashMap<Integer, Player>());
		
		redisClients = new Vector<RedisClient>();
		for (int i=0;i<redisCfg.getThreadCount();i++){
			RedisClient client = new RedisClient(redisCfg);
			redisClients.add(client);
		}
		
		dataThreads = new Vector<DataThread>();
		 for (int i=0;i<redisCfg.getThreadCount();i++){
			 DataThread dataThread = new DataThread(redisCfg);
			dataThreads.add(dataThread);
			dataThread.start();
		 }		
		 
		this.load();
	}
	
	private synchronized void load() {
		playerIdMaps.clear();
		playerMaps.clear();
		nextPlayerId = 0;
//		PlayerService playerService = new PlayerService();
//		List<PlayerWithBLOBs> players = playerService.findAll();
//		for (int i = 0; i < players.size(); i++) {
//			PlayerWithBLOBs player = players.get(i);
		
		
		Jedis jedis = redisClients.get(0).getJedis();
		if (jedis==null){
			log.error("could not get player redis,redis0 may not be run");
			return;
		}
		
		String guidplayer = jedis.get(super.DATAKEY_GUID_PLAYER);
		if (guidplayer!=null){
			log.info("start playerid "+guidplayer);
		}else
			log.info("start playerid "+currMaxPlayerId);
			
//		List<String> items = jedis.hvals(super.DATAKEY_PLAYER);
//		for (String item:items){
//			PlayerWithBLOBs player = (PlayerWithBLOBs)JSON.parseObject(item,PlayerWithBLOBs.class);
//			playerMaps.put(player.getPlayerid(), player);
//			playerIdMaps.put(player.getPlayername(), player.getPlayerid());
//		}
//		redisClients.get(0).returnResource(jedis);
//		log.warn("load all players :" + items.size());

	}
	public static void main(String[] args) {
		DataManager stmgr = DataManager.getInstance();
		stmgr.init();
		long s = System.currentTimeMillis();
		for (int i=0;i<1000;i++)
			stmgr.findPlayer(1335);
		System.out.println("cost t:"+(System.currentTimeMillis()-s));
//		stmgr.findPlayer("ppnane");
//		PlayerWithBLOBs pp = new PlayerWithBLOBs();
//		pp.setPlayerid(33);
//		stmgr.addPlayer(pp);
//		SavingManager.getInstance().init();
//		InsureManager.getInstance().init();
//		StockManager.getInstance().init();
//		ToplistManager.getInstance().init();
//		stmgr.init();

	}
}
