package cn.hd.mgr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import cn.hd.cf.action.LoginAction;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Quest;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Signin;
import cn.hd.cf.model.Stock;
import cn.hd.util.DateUtil;
import cn.hd.util.QuestLog;
import cn.hd.util.RedisClient;
import cn.hd.util.SigninLog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class DataManager extends MgrBase {
	protected Logger log = Logger.getLogger(getClass());
	public List<String> pps = new ArrayList<String>();
	private final int idStep = 100;
	private final int SESSION_PERIOD = 1000*60*15;		//15分钟，令牌
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

	/**
	 * 申请新playerid
	 * @return int player 新ID
	 * */
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
	public synchronized String getXXX(String playerName,String playeridStr,String pwd) {
		if (!pwd.equals("hdcf"))
			return "illegal access";
		
		String str = "";
		str += "当前玩家总数 :"+String.valueOf(findPlayerCount())+" <br>";
		str += "当前排行榜 人数:"+String.valueOf(ToplistManager.getInstance().getTopCount())+" <br>";
		if (playerName==null&&playeridStr==null)
			return str;
		
		Player p = null;
		if (playerName!=null){
			p = findPlayer(playerName);
		}
		if (playeridStr!=null){
			p = findPlayer(Integer.valueOf(playeridStr));
		}
		if (p==null)
			return str +="no player";
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		String xxx = "玩家数据: pid:"+p.getPlayerid()+",name:"+p.getPlayername()+",openid:"+p.getOpenid()+" <br>";
		String ctime = p.getCreateTimeStr();
		 if (p.getCreatetime()!=null)
			 ctime = formatter.format(p.getCreatetime());
		xxx += ",createtime:"+ctime+"<br>";
		xxx += "player :"+JSON.toJSONString(p)+" <br><br>";
		xxx += "存款 :<br>";
		Map<Integer,Saving> savings = JSON.parseObject(get_saving2(p.getPlayerid()), new TypeReference<Map<Integer, Saving>>() {});
		for (Integer itemid:savings.keySet()){
			Saving item = savings.get(itemid); 
			String cstr = formatter.format(item.getCreatetime());
			xxx += "createtime: "+cstr+",data:"+JSON.toJSONString(item)+"<br>";
		}
		xxx += "<br><br>";
		xxx += "保险 :<br>";
		Map<Integer,Insure> insures = JSON.parseObject(get_insure2(p.getPlayerid()), new TypeReference<Map<Integer, Insure>>() {});
		for (Integer itemid:insures.keySet()){
			Insure item = insures.get(itemid); 
			String cstr = formatter.format(item.getCreatetime());
			xxx += "createtime: "+cstr+",data:"+JSON.toJSONString(item)+"<br>";
		}
		xxx += "<br><br>";
		xxx += "股票 :<br>";
		Map<Integer,List<Stock>> stocks = JSON.parseObject(getData(p.getPlayerid(),3), new TypeReference<Map<Integer, List<Stock>>>() {});
		for (Integer itemid:stocks.keySet()){
			List<Stock> items = stocks.get(itemid); 
			for (Stock item:items){
				String cstr = formatter.format(item.getCreatetime());
				xxx += "createtime: "+cstr+",data:"+JSON.toJSONString(item)+"<br>";
			}
		}
		xxx += "<br><br>";
		xxx += "日志:<br>";
		xxx += getData(p.getPlayerid(),5);
		
		xxx += "<br><br>";
		xxx += "top :"+getData(p.getPlayerid(),4)+" <br>";
		return str + xxx;
	}

	/**
	 * 玩家完成任务
	 * @param int playerid
	 * @param int 任务类型
	 * @return boolean true为更新成功，false为更新失败
	 * */
	public synchronized boolean doneQuest(int playerid,int doType){
		Player player = findPlayer(playerid);
		if (player==null)
			return false;
		
		//saving
		int questid = -1;
		String queststr = player.getQuestStr();
		if (queststr==null)
			return false;
		
		if (doType==1&&queststr.indexOf("5")>=0){
			questid = 5;
		}else if (doType==2&&queststr.indexOf("1")>=0){
			questid = 1;
		}else if (doType==3&&queststr.indexOf("2")>=0){
			questid = 2;
		}else if (doType==4&&queststr.indexOf("3")>=0){
			questid = 3;
		}else if (doType==5&&queststr.indexOf("4")>=0){
			questid = 4;
		}
		
		if (questid>0){
			queststr = queststr.replace(String.valueOf(questid), "").replace(",","");
			player.setQuestStr(queststr);
			if (queststr.length()<=0||queststr.split(",").length<=0){
				player.setQuestDoneTime(new Date());
				
				Quest record = new Quest();
				record.setPlayerid(playerid);
				record.setCrdate(new Date());		
				QuestLog.getRootLogger().info(JSON.toJSONString(record));
				LogMgr.getInstance().log(playerid," done daily quest");
			}	
			DataManager.getInstance().updatePlayerQuest(player);
			return true;
		}
		return false;
	}
	
	public synchronized String login(String openId,String playerName,String sexstr,String playerstr,String settingStr,HttpServletRequest request) {
		String loginStr = "loginReq: openId:"+openId+",playerName:"+playerName+",";
		if (playerstr!=null)
			loginStr += "playerid:"+playerstr;
		loginStr += ",setting:"+settingStr+",ip:"+loginAction.getIpAddress(request);
		log.warn(loginStr);
//		return null;
		if (settingStr.indexOf("android:true")<0&&settingStr.indexOf("iphone:true")<0){
			String pwd = request.getParameter("pwd");
			if (pwd==null||!pwd.endsWith("hdcf")){
				log.warn("illegal access!!");
				return loginAction.msgStr(RetMsg.MSG_IllegalAccess);
			}
		}
			
		if (!Pattern.matches("[0-9]+", openId)){
			log.warn("illegal openid:"+openId);
			return loginAction.msgStr(RetMsg.MSG_WrongOpenID);
		}
		
		int sex = 0;
		if (sexstr!=null)
			sex = Integer.valueOf(sexstr);
		
		int playerid = -1;
		if (playerstr!=null)
			playerid = Integer.valueOf(playerstr);
		
		long clientSessionid = 0;
		String sessionstr = request.getParameter("sessionid");
		if (sessionstr!=null){
			clientSessionid = Long.valueOf(sessionstr);
		}
		
		return loginAction.login(playerid,playerName,openId,(byte)sex,clientSessionid);
	}

	/**
	 * 获取玩家数据
	 * @param int playerid
	 * @param int 类型，包括存款，股票，保险
	 * @return String json数据
	 * */
	public synchronized String getData(int playerid, int typeid) {
		String data = "";
		if (typeid == 1) {
			data = loginAction.get_savingAndInsure(playerid);
		} else if (typeid == 3) {
			data = get_stock(playerid);
		} else if (typeid == 4) {
			int top = get_top(playerid);
			data = String.valueOf(top);
		} else if (typeid==5&&jedisClient4!=null){
			Jedis j4 = jedisClient4.getJedis();
			String key = MgrBase.DATAKEY_DATA_LOG+playerid;
			int len = (j4.llen(key).intValue());
			for (int i=len-1;i>=0;i--)
			{
				data += j4.lindex(key,i)+"<br>";
			}
		}
		return data;
	}

	/**
	 * 根据玩家id得到存款数据
	 * @param int playerid
	 * @return String 玩家存款json数据
	 * */
	public synchronized String get_saving2(int playerid) {
		List<Saving> savings = SavingManager.getInstance().getSavingList(playerid);
		if (savings==null) return "{}";
		Map<Integer,Saving>	 mdata = new HashMap<Integer,Saving>();
		for (Saving item:savings){
			mdata.put(item.getItemid(), item);
		}
		return JSON.toJSONString(mdata);
	}

	/**
	 * 获取玩家数据集
	 * @param int playerid
	 * @return String 玩家json数据
	 * */
	public String get_info(int playerid){
		Player player = this.findPlayer(playerid);
		if (player==null)
			return null;
		
		return loginAction.getPlayerJsonData(player);
	}
	
	/**
	 * 根据玩家id得到保险数据
	 * @param int playerid
	 * @return String 玩家保险json数据
	 * */
	public synchronized String get_insure2(int playerid) {
		List<Insure> insures = InsureManager.getInstance().getInsureList(playerid);
		if (insures==null) return "{}";
		Map<Integer,Insure>	mdata = new HashMap<Integer,Insure>();
		for (Insure item:insures){
			mdata.put(item.getItemid(), item);
		}
		return JSON.toJSONString(mdata);
	}
	
	/**
	 * 根据玩家id得到股票数据
	 * @param int playerid
	 * @return String 玩家股票00json数据
	 * */
	public synchronized String get_stock(int playerid) {
		Map<Integer, List<Stock>> data = StockManager.getInstance()
				.findMapStocks(playerid);
		return JSON.toJSONString(data);
	}

	public synchronized String get_quest(int playerid){
		Player p = findPlayer(playerid);
		if (p==null) return "";
		//随机任务两个:
		int questcount = 0;
		Date lastlogin = p.getLastlogin();
		boolean istoday = DateUtil.isToday(p.getLastlogin());
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
	
	/**
	 * 根据玩家id得到排名
	 * @param int playerid
	 * @return int 玩家周排名
	 * */
	public synchronized int get_top(int playerid) {
		ToplistManager.getInstance().load();
	
		int top = ToplistManager.getInstance().getTopNumber(
				playerid, 0);
		// 800ms/1k
		return top+1;
	}

	/**
	 * 新增玩家
	 * @param Player 对象
	 * @return boolean true表示增加成功，false表示增加失败
	 * */
	public synchronized boolean addPlayer(Player player) {
		int playerid = player.getPlayerid();
//		Player pp = findPlayer(playerid);
//		if (pp!=null)
//			return false;

		//playerMaps.put(playerid, player);
		playerIdMaps.put(player.getPlayername(), playerid);
		
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.push(player);
		//saver.add(player);
		//LogMgr.getInstance().log(System.currentTimeMillis());
		return true;
	}

	/**
	 * 根据昵称获取玩家对象
	 * @param String 玩家昵称
	 * @return Player 对象
	 * */
	public synchronized Player findPlayer(String playerName) {
		long s = System.currentTimeMillis();
		Integer playerid = playerIdMaps.get(playerName);
		if (playerid != null) {
			return findPlayer(playerid);
		}
		
		int index = Math.abs(playerName.hashCode())%redisClients.size();
		RedisClient jedisClient = redisClients.get(index);
		Jedis jedis = jedisClient.getJedis();
		String idstr = jedis.hget(MgrBase.DATAKEY_PLAYER_ID, playerName);
		jedisClient.returnResource(jedis);
		if (idstr!=null){
			playerid = Integer.valueOf(idstr);
				//LogMgr.getInstance().log("find player :"+playerid);
			//playerMaps.put(playerid, player);
				playerIdMaps.put(playerName, playerid);
			return findPlayer(playerid);
		}
		long cost = System.currentTimeMillis()-s;
		if (cost>10)
			log.warn("register find cost :"+cost+",name:"+playerName);
		return null;
	}

	//是否能执行存款，股票，保险，签到等登陆后修改数据操作:
	public synchronized long canSubmit(int playerid,long clientSessionid) {
		//执行操作时必须有客户端sessionid:
		if (clientSessionid<=0)
			return RetMsg.MSG_WrongSession;
		
		long sessionid = findSession(playerid);
		if (sessionid<=0){
			return RetMsg.MSG_WrongSession;
		}
		
		long curr = System.currentTimeMillis();
		long diff = curr - sessionid;
		if (diff<=SESSION_PERIOD){			//未过期
			if (sessionid!=clientSessionid)			//有效期内必须相等，否则客户端session无效
				return RetMsg.MSG_WrongSession;
			else
				return sessionid;
		}
		
		//session过期,重置返回新的sessionid:
		return resetSession(playerid);
	}

	//是否能进行登陆:
	public synchronized long canLogin(int playerid,long clientSessionid,boolean isRegister) {
		long serverSessionid = findSession(playerid);
		//session尚未存在
		if (serverSessionid<=0){
			if (clientSessionid>0)
				return RetMsg.MSG_WrongSession;			//客户端先于服务器存在
			else
				return resetSession(playerid);			//注册/第一次使用, reset并返回最新的
		}
		
		//注册时已有sessionid:
		if (serverSessionid>0&&isRegister)
			return RetMsg.MSG_WrongSession;
		
		long curr = System.currentTimeMillis();
		long diff = curr - serverSessionid;
		if (diff>SESSION_PERIOD){			//过期,,重置返回新的sessionid
			return resetSession(playerid);
		}
		
		//session未过期，服务器和客户端必须相等:
		if (serverSessionid!=clientSessionid)
			return RetMsg.MSG_WrongSession;
		else
			return serverSessionid;
	}
	
	public synchronized long resetSession(int playerid) {
		long sessionid = System.currentTimeMillis();
		int index = playerid%redisClients.size();
		RedisClient jedisClient = redisClients.get(index);
		Jedis jedis = jedisClient.getJedis();
		jedis.hset(MgrBase.DATAKEY_SESSION, String.valueOf(playerid),String.valueOf(sessionid));
		jedisClient.returnResource(jedis);	
		log.warn("pid:"+playerid+" session reset:"+sessionid);
		return sessionid;
	}

	public synchronized long findSession(int playerid) {
		long sessionid = 0;
		int index = playerid%redisClients.size();
		RedisClient jedisClient = redisClients.get(index);
		Jedis jedis = jedisClient.getJedis();
		String sessionstr = jedis.hget(MgrBase.DATAKEY_SESSION, String.valueOf(playerid));
		jedisClient.returnResource(jedis);	
		if (sessionstr!=null){
			sessionid = Long.valueOf(sessionstr);
		}
		log.warn("pid:"+playerid+" find session:"+sessionid);
		return sessionid;
	}
	
	/**
	 * 根据playerid获取玩家对象
	 * @param int 玩家id
	 * @return Player 对象
	 * */
	public synchronized Player findPlayer(int playerid) {
		Player player = null;
		long s = System.currentTimeMillis();

		if (player==null)
		{
			int index = playerid%redisClients.size();
			RedisClient jedisClient = redisClients.get(index);
			Jedis jedis = jedisClient.getJedis();
			String itemstr = jedis.hget(super.DATAKEY_PLAYER, String.valueOf(playerid));
			jedisClient.returnResource(jedis);			
			if (itemstr!=null){
				player = (Player)JSON.parseObject(itemstr,Player.class);
				//LogMgr.getInstance().log("find player :"+player.getPlayerid());
				//playerMaps.put(playerid, player);
				if (!playerIdMaps.containsKey(player.getPlayername())){
					playerIdMaps.put(player.getPlayername(), playerid);
				}
			}
		}
		//LogMgr.getInstance().log("findid cost :"+(System.currentTimeMillis()-s));
		return player;
	}

	public synchronized void updatePlayerQuest(Player player) {
		DataThread dataThread = dataThreads.get(player.getPlayerid()%dataThreads.size());
		dataThread.updatePlayer(player);
	}
	
	/**
	 * 更新玩家数据集
	 * @param int playerid
	 * @param int 类型，包括签到，开通股票账户，完成任务，以及事件
	 * @param String itemid
	 * @param String 金额
	 * @return String 玩家保险json数据
	 * */
	public synchronized String update(int playerid,int type,String itemstr,String amountStr,String sessionstr){
		long clientSessionid = 0;
		if (sessionstr!=null){
			clientSessionid = Long.valueOf(sessionstr);
		}
		Player p = findPlayer(playerid);
		if (p==null) return "";
		
		long canSubmit = canSubmit(playerid, clientSessionid);
		if (canSubmit<=0){
			return loginAction.msgStr((int)RetMsg.MSG_WrongSession);
		}
		
		Date last = p.getLastlogin();

		Player data = new Player();
		data.setPlayerid(playerid);
		switch (type){
		case 0:
			if (DateUtil.isToday(last)){
				return loginAction.msgStr2(RetMsg.MSG_ExecError, String.valueOf(canSubmit));
			}
			
			Integer days = p.getSigninCount();
			if (days<1)
				return loginAction.msgStr2(RetMsg.MSG_ExecError, String.valueOf(canSubmit));
			
			int count = (days-1)%signinMoneys.size();
			int money = signinMoneys.get(count);
			int exp = signinExps.get(count);
			p.setExp(p.getExp()+exp);
			p.setLastlogin(new Date());
			p.setEventCount(0);
			this.addSignin(playerid);
			data.setExp(p.getExp());
			float newTotal = SavingManager.getInstance().updateLiveSavingAndTop(playerid,money);
			int newTop = ToplistManager.getInstance().findTopCount(null,0,newTotal);
			data.setTop(newTop+1);
			LogMgr.getInstance().log(playerid," signin days:"+days+" add prize,money: "+money+", exp:"+exp);
			break;
		case 2:
			p.setOpenstock((byte)1);
			break;
		case 3:
			//todo: 这里加上正数的每日次数限制:
			float amount = Float.valueOf(amountStr);
			//非法:
			if (amount>0){
				if (amount>10000)
				{
					log.warn("pid:"+playerid+" error,flush event,amount:"+amount);
					return loginAction.msgStr2(RetMsg.MSG_ExecError, String.valueOf(canSubmit));
				}
				//不同一天登陆不允许正向事件:
				if (last==null||!DateUtil.isToday(last)){
					return loginAction.msgStr2(RetMsg.MSG_ExecError, String.valueOf(canSubmit));
				}
				if (p.getEventCount()>50){
					log.warn("pid:"+playerid+" error,flush event count");
					return loginAction.msgStr2(RetMsg.MSG_ExecError, String.valueOf(canSubmit));
				}
				
				p.setEventCount(p.getEventCount()+1);
			}
			LogMgr.getInstance().log(playerid," event fire, amount:"+amount);
			float newTotal2 = SavingManager.getInstance().updateLiveSavingAndTop(playerid,amount);
			int newTop2 = ToplistManager.getInstance().findTopCount(null,0,newTotal2);
			data.setTop(newTop2+1);
			break;
			
		}
		data.setSessionid(canSubmit);
		DataThread dataThread = dataThreads.get(playerid%dataThreads.size());
		dataThread.updatePlayer(p);
		return JSON.toJSONString(data);
	}
	
	/**
	 * 增加签到记录
	 * @param int playerid
	 * @return 无
	 * */
	public synchronized void addSignin(int playerid) {
		Signin record = new Signin();
		record.setPlayerid(playerid);
		record.setCrdate(new Date());		
		SigninLog.getRootLogger().info(JSON.toJSONString(record));
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
		jedisClient3.returnResource(j3);
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
	
	private long findPlayerCount(){
		Jedis jedis = redisClients.get(0).getJedis();
		if (jedis==null){
			log.error("could not get player redis,redis0 may not be run");
			return 0 ;
		}		
		long count = jedis.hlen(MgrBase.DATAKEY_PLAYER);
		redisClients.get(0).returnResource(jedis);	
		return count;
	}
	
	public synchronized String addXXX(String pwd,int type,int playerid,String str) {
		if (!pwd.equals("hdcf"))
			return "illegal access";
		
		switch (type){
		case 0:
			Player player = (Player)JSON.parseObject(str,Player.class);
			addPlayer(player);
			break;
		case 1:
			Saving saving = (Saving)JSON.parseObject(str,Saving.class);
			SavingManager.getInstance().addSaving(playerid, saving);
			break;
		
		case 2:
			Insure insure = (Insure)JSON.parseObject(str,Insure.class);
			InsureManager.getInstance().addInsure(playerid, insure);
			break;
		
		}
		return "ok";
	}
	
	public synchronized String setXXX(String pwd,String playerName,String playeridStr,String typestr,String itemstr,String qtystr,String psstr) {
		if (!pwd.equals("hdcf"))
			return "illegal access";
		
		Player p = null;
		if (playerName!=null){
			p = findPlayer(playerName);
		}else if (playeridStr!=null){
			p = findPlayer(Integer.valueOf(playeridStr));
		}
		if (p==null)
			return "没找到对应玩家";
		
		int ret = RetMsg.MSG_OK;
		int type = Integer.valueOf(typestr);
		int itemid = Integer.valueOf(itemstr);
		float qty = Float.valueOf(qtystr);
		switch (type){
		case 0:		//exp:
			p.setExp(p.getExp()+(int)qty);
			if (p.getExp()<=0)
				p.setExp(0);
			DataThread dataThread = dataThreads.get(p.getPlayerid()%dataThreads.size());
			dataThread.push(p);
			break;
		case 1:		//saving
			if (itemid<=0||itemid>6){
				return "没有这种存款";
			}
			Saving saving = new Saving();
			saving.setPlayerid(p.getPlayerid());
			saving.setItemid(itemid);
			if (qty<=0){
				ret = SavingManager.getInstance().deleteSaving(p.getPlayerid(), saving);
				break;
			}
			List<Saving> list = SavingManager.getInstance().getSavingList(p.getPlayerid());
			Saving saving2 = null;
			for (Saving item:list){
				if (item.getItemid()==itemid){
					saving2 = item;
					item.setAmount(item.getAmount()+qty);
				}
			}
			//add saving:
			if (saving2==null){
				Saving savingCfg = SavingManager.getInstance().getSavingCfg(itemid);
				saving.setAmount(qty);
				saving.setName(savingCfg.getName());
				saving.setCreatetime(new Date());
				saving.setUpdatetime(new Date());
				saving.setRate(savingCfg.getRate());
				saving.setQty(1);
				saving.setType(savingCfg.getType());
				saving.setPeriod(savingCfg.getPeriod());			
				list.add(saving);
			}
			SavingManager.getInstance().updateSavings(p.getPlayerid(), list);
			break;
		case 2:		//insure
			if (itemid<=0||itemid>8){
				return "没有这种保险";
			}
			Insure insure = new Insure();
			insure.setPlayerid(p.getPlayerid());
			insure.setItemid(itemid);
			if (qty<=0){
				ret = InsureManager.getInstance().deleteInsure(insure.getPlayerid(), insure);	
				break;
			}
			Insure incfg = InsureManager.getInstance().getInsureCfg(itemid);
			insure.setCreatetime(new Date());
			insure.setUpdatetime(new Date());
			insure.setQty((int)qty);
			insure.setAmount(incfg.getPrice()*insure.getQty());
			insure.setPeriod(incfg.getPeriod()*insure.getQty());
			insure.setType(incfg.getType());
			ret = InsureManager.getInstance().updateInsure(p.getPlayerid(), insure);
			if (ret==RetMsg.MSG_InsureNotExist){
				ret = InsureManager.getInstance().addInsure(p.getPlayerid(), insure);
			}
			break;
		case 3:		//stock
			if (itemid<=0||itemid>51){
				return "没有这种股票";
			}
			if (qty<=0){
				StockManager.getInstance().deleteStock(p.getPlayerid(), itemid, (int)(0-qty));
				break;
			}
			float ps = Float.valueOf(psstr);
			Stock stock = new Stock();
			stock.setPlayerid(p.getPlayerid());
			stock.setItemid(itemid);
			stock.setQty((int)qty);
			stock.setPrice(ps);
			stock.setAmount(ps*stock.getQty());
			ret = StockManager.getInstance().updateStock(stock);
			if (ret==RetMsg.MSG_StockNotExist){
				stock.setCreatetime(new Date());
				ret = StockManager.getInstance().addStock(p.getPlayerid(), stock);
			}
			break;
		case 4:		//quest
			String q = p.getQuestStr();
			if (qty<0){
				if (q.length()>0&&itemid>0&&itemid<=5){
					q = q.replace(itemstr+",","").replace(","+itemstr,"").replace(itemstr,"");
				}
			}else {
				int qid = (int)(Math.random()*5);
				if (itemid>0&&itemid<=5){
					qid = itemid;
				}
				if (q!=null&&q.length()>0){
					q += ","+qid;
				}else
					q = String.valueOf(qid);
			}
			p.setQuestStr(q);
			DataThread dataThread2 = dataThreads.get(p.getPlayerid()%dataThreads.size());
			dataThread2.push(p);
			break;
		}
		String str = "gm:pid:"+playeridStr+",type:"+type+",itemid:"+itemid+",qty:"+qty;
		if (psstr!=null){
			str += ", ps:"+psstr;
		}
		str += ", ret:"+ret;
		log.warn(str);
		return "设置结果:"+ret;
	}

	public static void main(String[] args) {
		
		long s = Long.valueOf(1454491582484L);
		Date now = new Date();
		String a1 = null;
		String b = "aa"+a1;
		Date last= new Date(1455594730831L);
		boolean a = DateUtil.isToday(last);
		int d = now.getYear();
//		DataManager stmgr = DataManager.getInstance();
//		stmgr.init();
//		long s = System.currentTimeMillis();
//		for (int i=0;i<1000;i++)
//			stmgr.findPlayer(1335);
//		System.out.println("cost t:"+(System.currentTimeMillis()-s));
		
		 Date date = new Date(99, 3, 21);
	      Date date2 = new Date(99, 3, 9);
	      
	      // make 3 comparisons with them
	      int comparison = date.compareTo(date2);
	      int comparison2 = date2.compareTo(date);
	      int comparison3 = date.compareTo(date);

	      // print the results
	      
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
