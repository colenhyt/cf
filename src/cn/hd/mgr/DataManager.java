package cn.hd.mgr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.tools.InitdataService;

public class DataManager extends MgrBase{
	protected Logger  log = Logger.getLogger(getClass()); 
	
	private LoginAction loginAction;
	private Init	init;
	public Init getInit() {
		return init;
	}

	public Map<String,Integer> playerIdMaps;
	public Map<Integer,PlayerWithBLOBs> playerMaps;
	private int nextPlayerId;
	private DataThread dataThread;
	
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
	
    public synchronized int assignNextId(){
		nextPlayerId++;
		return nextPlayerId;
	}
	
	public synchronized String login(String playerName,String tel,String sex){
		PlayerWithBLOBs pp = new PlayerWithBLOBs();
		pp.setPlayername(playerName);
		pp.setTel(tel);
		pp.setSex(Byte.valueOf(sex));
		loginAction.setPlayer(pp);
		return loginAction.login();
	}
	
	public synchronized boolean addPlayer(PlayerWithBLOBs player){
		if (playerMaps.containsKey(player.getPlayerid()))
			return false;
		
		playerMaps.put(player.getPlayerid(), player);
		playerIdMaps.put(player.getPlayername(), player.getPlayerid());
		dataThread.push(player);
//		newPlayersVect.add(player);
		return true;
	}
	
	public synchronized PlayerWithBLOBs findPlayer(String playerName){
		Integer playerid = playerIdMaps.get(playerName);
		if (playerid!=null){
			return playerMaps.get(playerid);
		}
		return null;
	}
	
	public synchronized PlayerWithBLOBs findPlayer(int playerid){
		return playerMaps.get(playerid);
	}
	
	public synchronized boolean updatePlayer(PlayerWithBLOBs player){
		PlayerWithBLOBs pp = playerMaps.get(player.getPlayername());
		if (pp!=null){
			pp.setQuest(player.getQuest());
			pp.setOpenstock(player.getOpenstock());
			pp.setExp(player.getExp());
			dataThread.updatePlayer(pp);
			return true;
		}
		return false;
	}
	
	public synchronized void addSignin(int playerid){
		dataThread.addSignin(playerid);
	}
	
	public synchronized void addDoneQuest(int playerid){
		dataThread.addDoneQuest(playerid);
	}
	
	public synchronized boolean updateZan(int playerid,int zan){
		PlayerWithBLOBs pp = findPlayer(playerid);
		if (pp!=null){
			pp.setZan(zan);
			dataThread.updatePlayer(pp);
			return true;
		}
		return false;
	}
	
	public synchronized void updateLogin(String playerName)
	{
		PlayerWithBLOBs player = playerMaps.get(playerName);
		if (player==null){
			return;
		}
		player.setLastlogin(new Date());
		dataThread.updatePlayer(player);
	}
	
    public void init(){

    	loginAction = new LoginAction();
    	
    	InitdataService initdataService = new InitdataService();
    	init = initdataService.findInit();
    	
    	dataThread = new DataThread();
    	dataThread.start();    	
		
    	playerIdMaps = Collections.synchronizedMap(new HashMap<String,Integer>());
    	playerMaps = Collections.synchronizedMap(new HashMap<Integer,PlayerWithBLOBs>());
    	PlayerService playerService = new PlayerService();
		List<PlayerWithBLOBs> players = playerService.findAll();
		for (int i=0; i<players.size();i++){
			PlayerWithBLOBs player = players.get(i);
			playerMaps.put(player.getPlayerid(), player);
			playerIdMaps.put(player.getPlayername(), player.getPlayerid());
			if (player.getPlayerid()>nextPlayerId)
				nextPlayerId = player.getPlayerid();			
		}
		System.out.println("load all players :"+players.size());
		
    }
    public static void main(String[] args) {
    	DataManager stmgr = DataManager.getInstance();
    	SavingManager.getInstance().init();
		InsureManager.getInstance().init();
		StockManager.getInstance().init();
		ToplistManager.getInstance().init();    	
    	stmgr.init();
    	float count = 500;
    	long s = System.currentTimeMillis();
    	for (int i=0;i<count;i++){
    		String s2 = String.valueOf(i);
    		String str = stmgr.login(s2, s2, "1");
    	}
    	float e = System.currentTimeMillis()-s;
    	System.out.println("run "+count+", cost time : "+e+"ms,"+(e/count)+"s/1000");
//    	SavingdataService ss = new SavingdataService();
//    	Savingdata dd = ss.findActive();
//    	DataThread aa = new DataThread();
//    	aa.start();
    }
}
