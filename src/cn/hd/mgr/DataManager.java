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
import cn.hd.base.BaseService;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.Insure;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Savingdata;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.ToplistService;
import cn.hd.cf.tools.InitdataService;
import cn.hd.cf.tools.SavingdataService;
import cn.hd.util.RedisClient;

public class DataManager {
	private int UPDATE_PERIOD = 20*30;		//20*60: 一小时
	public boolean useJedis;
	public Jedis jedis;
	public RedisClient redisClient;	
	private Init	init;
	private Map<Integer,Saving>		savingData;
	private Map<Integer,String>	insureMap;
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
    	insureMap = new HashMap<Integer,String>();
     }
	
    public synchronized boolean addInsure(int playerId,Insure record){
    	String instr = getInsures(playerId);
    	List<Insure> list = BaseService.jsonToBeanList(instr, Insure.class);
    	boolean found = false;
    	for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().equals(record.getItemid())){
    			found = true;
    			break;
    		}
    	}
    	if (found){
    		return false;
    	}
    	list.add(record);
		instr = BaseService.beanListToJson(list,Insure.class);
		insureMap.put(playerId, instr);
    	return true;
    }
	
    public synchronized boolean deleteInsure(int playerId,Insure record){
    	String instr = getInsures(playerId);
    	List<Insure> list = BaseService.jsonToBeanList(instr, Insure.class);
    	boolean found = false;
    	for (int i=0;i<list.size();i++){
			if (list.get(i).getItemid().equals(record.getItemid())){
    			list.remove(i);
    			found = true;
    			break;
    		}
    	}
    	if (found){
    		instr = BaseService.beanListToJson(list,Insure.class);
    		insureMap.put(playerId, instr);
    		return true;
    	}
    	return false;
    }
    
    public synchronized String getInsures(int playerId){
    	String instr = null;
    	if (insureMap.containsKey(playerId))
    	instr = insureMap.get(playerId);
    	else {
    		InsureService inser = new InsureService();
    		List<Insure> list = inser.getDBInsures(playerId);
    		instr = BaseService.beanListToJson(list, Insure.class);
    		insureMap.put(playerId, instr);
    	}
    	System.out.println("找这里::::");
    	return instr;
    }
    
	public int assignNextId(){
		nextPlayerId++;
		return nextPlayerId;
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
    	SavingdataService ss = new SavingdataService();
    	Savingdata dd = ss.findActive();
    }
}
