package cn.hd.mgr;

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

import org.apache.log4j.Logger;

import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Savingdata;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.ToplistService;
import cn.hd.cf.tools.InitdataService;
import cn.hd.cf.tools.SavingdataService;

public class DataManager extends MgrBase{
	protected Logger  log = Logger.getLogger(getClass()); 
	
	private LoginAction loginAction;
	private Init	init;
	public Init getInit() {
		return init;
	}

	public Map<String,PlayerWithBLOBs> playerMaps;
	private List<Toplist>			currMonthList;
	private Vector<PlayerWithBLOBs> newPlayersVect;
	private Vector<PlayerWithBLOBs>	updatePlayersVect;
	private Vector<Toplist>			updateToplistVect;
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
		if (playerMaps.containsKey(player.getPlayername()))
			return false;
		
		playerMaps.put(player.getPlayername(), player);
		dataThread.push(player);
//		newPlayersVect.add(player);
		return true;
	}
	
	public synchronized PlayerWithBLOBs findPlayer(String playerName){
		return playerMaps.get(playerName);
	}
	
	public synchronized PlayerWithBLOBs findPlayer(int playerid){
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
//		updatePlayersVect.add(player);
	}
	
    public void init(){

    	loginAction = new LoginAction();
    	
    	newPlayersVect = new Vector<PlayerWithBLOBs>();
       	updatePlayersVect = new Vector<PlayerWithBLOBs>();
    	updateToplistVect = new Vector<Toplist>();
    	
    	
    	InitdataService initdataService = new InitdataService();
    	init = initdataService.findInit();
    	
    	dataThread = new DataThread();
    	dataThread.start();    	
		
    	playerMaps = Collections.synchronizedMap(new HashMap<String,PlayerWithBLOBs>());
    	PlayerService playerService = new PlayerService();
		List<PlayerWithBLOBs> players = playerService.findAll();
		for (int i=0; i<players.size();i++){
			PlayerWithBLOBs player = players.get(i);
			playerMaps.put(player.getPlayername(), player);
			if (player.getPlayerid()>nextPlayerId)
				nextPlayerId = player.getPlayerid();			
		}
		System.out.println("load all players :"+players.size());
		
    	ToplistService toplistService = new ToplistService();
    	currMonthList = Collections.synchronizedList(new ArrayList<Toplist>());
    	List<Toplist> monlist = toplistService.findCurrMonthToplists();
    	for (int i=0;i<monlist.size();i++){
    		currMonthList.add(monlist.get(i));
    	}
    	
    	
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

	public synchronized void update(){
    	tick ++;
    	if (newPlayersVect.size()>BATCH_COUNT||tick%UPDATE_PERIOD_BATCH==0){
    		PlayerService service= new PlayerService();
    		service.addPlayers(newPlayersVect);
    		log.warn("batch add players:"+newPlayersVect.size());
    		newPlayersVect.clear();
    	}    	
//    	
    	if (updatePlayersVect.size()>BATCH_COUNT||tick%UPDATE_PERIOD_BATCH==0){
    		PlayerService service= new PlayerService();
    		service.updatePlayers(updatePlayersVect);
    		log.warn("batch update players:"+updatePlayersVect.size());
    		updatePlayersVect.clear();
    	}     	
    	
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
    	DataThread aa = new DataThread();
    	aa.start();
    }
}
