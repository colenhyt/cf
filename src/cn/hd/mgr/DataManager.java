package cn.hd.mgr;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.action.SavingAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Savingdata;
import cn.hd.cf.model.Stock;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.StockService;
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
    
    private void initStock(PlayerWithBLOBs player){
    	Connection conn = null;
		Statement stat = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hdcf","root","123a123@");
			stat = conn.createStatement();
	         ResultSet rs = stat.executeQuery("SELECT amount FROM stock where playerid="+player.getPlayerid());  
	         while (rs.next()){  
	             float mm = rs.getFloat(1); 
//	             System.out.println(mm);
	         } 	
	         rs.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stat.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}    	
    }
    
    public DataManager(){
    	nextPlayerId = 0;
    	jedis = null;
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
		float fMoney = Float.valueOf(init.getMoney().intValue());
		saving.setAmount(fMoney);
		newSavingVect.add(saving);
		savings.put(savingCfg.getId(), saving);
		playerBlob.setMoney(fMoney);
		playerBlob.setSaving(JSON.toJSONString(savings));
		 }else{
			 playerBlob.setMoney(Float.valueOf(0));
		 }
			
		playerBlob.setWeektop(getTop(playerBlob.getPlayerid(),playerBlob.getMoney(),0));
		playerBlob.setMonthtop(getTop(playerBlob.getPlayerid(),playerBlob.getMoney(),1));
		 
		  JSONObject obj = JSONObject.fromObject(playerBlob);
		  playerStr = obj.toString();
		newPlayersVect.add(playerBlob);
		
		
		}
		
		
		//System.out.println("新增玩家:"+playerMaps.size());
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
	
	public String login(String playerName,String pwd){
		PlayerWithBLOBs player = playerMaps.get(playerName);
		if (player==null||!player.getPwd().equals(pwd)){
			PlayerService playerService = new PlayerService();
			player = playerService.findByName(playerName);
			if (player==null||!player.getPwd().equals(pwd)){
				System.out.println("用户名或密码不正确:"+playerName);
				Message msg = new Message();
				msg.setCode(RetMsg.MSG_WrongPlayerNameOrPwd);		//不存在
				JSONObject obj = JSONObject.fromObject(msg);
				return obj.toString();				
			}
			
			playerMaps.put(player.getPlayername(), player);		
		}
		if (player.getMoney()==null)
			player.setMoney(Float.valueOf(0));
		
		
		initStock(player);
 
//		StockService stockService = new StockService();
//		try{
//			List<Stock> stocks = stockService.findListByPlayerId(player.getPlayerid());
//			player.setStock(JSON.toJSONString(stocks));	
//			
//		}finally{
//			stockService.DBConnClose();
//		}
		
		player.setWeektop(getTop(player.getPlayerid(),player.getMoney(),0));
		player.setMonthtop(getTop(player.getPlayerid(),player.getMoney(),1));
		
		float margin = StockManager.getInstance().getMarginSec();
		player.setQuotetime(margin);
		player.setLastlogin(new Date());
		
		System.out.println("玩家登陆:"+player.getPlayerid());
		JSONObject obj = JSONObject.fromObject(player);	
		return obj.toString();
	}
	
	public int getTop(int playerid,float fMoney,int type){
		synchronized(currMonthList){
			for (int i=0;i<currMonthList.size();i++){
				Toplist top22 = currMonthList.get(i);
				if (top22.getPlayerid()==playerid
						||top22.getMoney().floatValue()<=fMoney){
					return i;
				}
			}		
			return currMonthList.size()+1;
			
		}
	}
	
	private void sortToplist(){
		synchronized(currMonthList){
			Collections.sort(currMonthList, new Comparator<Toplist>() {
	            public int compare(Toplist arg0, Toplist arg1) {
	                return arg0.getMoney().compareTo(arg1.getMoney());
	            }
	        });			
		}
	}
	
	public void onMoneyChanged(int playerid,float fChangedMoney){
		String playername = null;
		boolean needResort = false;
		for (int i=0;i<currMonthList.size();i++){
			Toplist topi = currMonthList.get(i);
			if (topi.getPlayerid()==playerid){
				playername = topi.getPlayername();
				float newMoney = topi.getMoney().floatValue()+fChangedMoney;
				if (newMoney>0){
					 topi.setMoney(BigDecimal.valueOf(newMoney));
					 needResort = true;
					break;
				}
			}
		}
		
		if (needResort){
			sortToplist();
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
    	updateToplistVect = new Vector<Toplist>();
    	
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
    	currMonthList = Collections.synchronizedList(new ArrayList<Toplist>());
    	List<Toplist> monlist = toplistService.findCurrMonthToplists();
    	for (int i=0;i<monlist.size();i++){
    		currMonthList.add(monlist.get(i));
    	}
    	
    	
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
    	ToplistService toplistService = new ToplistService();
    	List<Toplist> list = toplistService.findCurrMonthToplists();
    	stmgr.init();
    }
}
