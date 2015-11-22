package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import cn.hd.base.Config;
import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.tools.InitdataService;

import com.alibaba.fastjson.JSON;

public class DataManager extends MgrBase {
	protected Logger log = Logger.getLogger(getClass());
	public List<String> pps = new ArrayList<String>();

	private LoginAction loginAction;
	private Init init;

	public Init getInit() {
		return init;
	}

	public Map<String, Integer> playerIdMaps;
	public Map<Integer, PlayerWithBLOBs> playerMaps;
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
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized int assignNextId() {
		nextPlayerId++;
		return nextPlayerId;
	}

	public synchronized String login(String playerName, String tel, String sex) {
		PlayerWithBLOBs pp = new PlayerWithBLOBs();
		pp.setPlayername(playerName);
		pp.setTel(tel);
		pp.setSex(Byte.valueOf(sex));
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
		PlayerWithBLOBs player = this.findPlayer(playerid);
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

	public synchronized int get_top(int playerid) {
		float fMm = loginAction.calculatePlayerMoney(playerid);

		int top = ToplistManager.getInstance().findCountByGreaterMoney(
				playerid, 0, fMm);
		// 800ms/1k
		return top + 1;
	}

	public synchronized boolean addPlayer(PlayerWithBLOBs player) {
		PlayerWithBLOBs pp = findPlayer(player.getPlayerid());
		if (pp!=null)
			return false;

		playerMaps.put(player.getPlayerid(), player);
		playerIdMaps.put(player.getPlayername(), player.getPlayerid());
		dataThread.push(player);
		return true;
	}

	public synchronized PlayerWithBLOBs findPlayer(String playerName) {
		Integer playerid = playerIdMaps.get(playerName);
		if (playerid == null) {
			Jedis jedis = jedisClient.getJedis();
			if (!jedis.hexists(super.DATAKEY_PLAYER_ID, playerName)){
				return null;
			}
			String idstr = jedis.hget(super.DATAKEY_PLAYER_ID, playerName);
			jedisClient.returnResource(jedis);
			if (idstr!=null){
				playerid = Integer.valueOf(idstr);
				playerIdMaps.put(idstr, playerid);
			}else
				return null;
		}
		return findPlayer(playerid);
	}

	public synchronized PlayerWithBLOBs findPlayer(int playerid) {
		PlayerWithBLOBs player = playerMaps.get(playerid);
		if (player==null){
			Jedis jedis = jedisClient.getJedis();
			String itemstr = jedis.hget(super.DATAKEY_PLAYER, String.valueOf(playerid));
			jedisClient.returnResource(jedis);			
			if (itemstr!=null){
				player = (PlayerWithBLOBs)JSON.parseObject(itemstr,PlayerWithBLOBs.class);
				playerMaps.put(playerid, player);
				if (!playerIdMaps.containsKey(player.getPlayername())){
					playerIdMaps.put(player.getPlayername(), playerid);
				}
			}
		}
		return player;
	}

	public synchronized boolean updatePlayer(PlayerWithBLOBs player) {
		PlayerWithBLOBs pp = findPlayer(player.getPlayerid());
		if (pp != null) {
			pp.setExp(player.getExp());
			dataThread.updatePlayer(pp);
			return true;
		}
		return false;
	}

	public synchronized void addSignin(int playerid) {
		dataThread.addSignin(playerid);
	}

	public synchronized void addDoneQuest(int playerid) {
		dataThread.addDoneQuest(playerid);
	}

	public synchronized boolean updateZan(int playerid, int zan) {
		PlayerWithBLOBs pp = findPlayer(playerid);
		if (pp != null) {
			pp.setZan(zan);
			dataThread.updatePlayer(pp);
			return true;
		}
		return false;
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
		List<String> items = jedis.hvals(super.DATAKEY_PLAYER);
		for (int i=0;i<items.size();i++){
			PlayerWithBLOBs player = (PlayerWithBLOBs)JSON.parseObject(items.get(i),PlayerWithBLOBs.class);
			playerMaps.put(player.getPlayerid(), player);
			playerIdMaps.put(player.getPlayername(), player.getPlayerid());
			if (player.getPlayerid() > nextPlayerId)
				nextPlayerId = player.getPlayerid();
		}
		jedisClient.returnResource(jedis);
		log.warn("load all players :" + items.size());

	}

	public void init() {
		
//		String path = Thread.currentThread().getContextClassLoader()
//				.getResource("/").getPath();
//		String cfgstr = FileUtil.readFile(path + "config.properties");
//		if (cfgstr == null || cfgstr.trim().length() <= 0) {
//			return;
//		}
//		JSONObject ppObj = JSONObject.fromObject(cfgstr);
//		cfg = (Config) JSONObject.toBean(ppObj, Config.class);
//		System.out.println(cfgstr);

		loginAction = new LoginAction();

		InitdataService initdataService = new InitdataService();
		init = initdataService.findInit();


		playerIdMaps = Collections
				.synchronizedMap(new HashMap<String, Integer>());
		playerMaps = Collections
				.synchronizedMap(new HashMap<Integer, PlayerWithBLOBs>());
		this.load();
	}

	public static void main(String[] args) {
		DataManager stmgr = DataManager.getInstance();
		stmgr.init();
		stmgr.findPlayer(33);
		stmgr.findPlayer("ppnane");
		PlayerWithBLOBs pp = new PlayerWithBLOBs();
		pp.setPlayerid(33);
		pp.setPlayername("ppnane");
		stmgr.addPlayer(pp);
//		SavingManager.getInstance().init();
//		InsureManager.getInstance().init();
//		StockManager.getInstance().init();
//		ToplistManager.getInstance().init();
//		stmgr.init();
		float count = 500000;
		long s = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
//			String s2 = String.valueOf(i);
//			String str = stmgr.login(s2, s2, "1");
			// System.out.println(str);
		}
		float e = System.currentTimeMillis() - s;
		System.out.println("run " + count + ", cost time : " + e + "ms,"
				+ (e / count) + "s/1000");
		// SavingdataService ss = new SavingdataService();
		// Savingdata dd = ss.findActive();
		// DataThread aa = new DataThread();
		// aa.start();
	}
}
