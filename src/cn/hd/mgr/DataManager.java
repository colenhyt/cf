package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
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

	public synchronized String login(String jsonStr,HttpServletRequest request) {
		String loginStr = jsonStr+",ip:"+loginAction.getIpAddress(request);
		log.warn("login: "+loginStr);
		JSONObject jsonobj = JSONObject.fromObject(jsonStr);
		PlayerWithBLOBs pp = new PlayerWithBLOBs();
		pp.setPlayername(jsonobj.getString("playername"));
		pp.setOpenid(jsonobj.getString("openid"));
		pp.setSex(Byte.valueOf(jsonobj.getString("sex")));
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
			String idstr = jedis.hget(super.DATAKEY_PLAYER_ID, playerName);
			jedisClient.returnResource(jedis);
			if (idstr!=null){
				playerid = Integer.valueOf(idstr);
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
				.synchronizedMap(new HashMap<Integer, PlayerWithBLOBs>());
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
