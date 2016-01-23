package cn.hd.mgr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.tools.InitdataService;

public class DBDataSaver  extends Thread {
	protected Logger log = Logger.getLogger(getClass());
	public Vector<Player> players = new Vector<Player>();
	
	


	private static DBDataSaver uniqueInstance = null;

	public DBDataSaver() {
	}

	public synchronized void add(Player p) {
		players.add(p);
	}
	
	private synchronized void savePlayers(){
		PlayerService service  = new PlayerService();
		if (players.size()>=5){
			service.addPlayers(players);
			log.warn("save pp "+players.size());
			players.clear();
		}		
	}
	
	public void run() {
		while (true){
			try {
			synchronized(this)
			{
				savePlayers();
				super.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			log.error(e.getMessage());
//			if (e instanceof JedisConnectionException) {
//				JedisConnectionException new_name = (JedisConnectionException) e;
//			}else
				e.printStackTrace();
		}
		}
		
	}

	
	public static void main(String[] args) {
		DBDataSaver sync = new DBDataSaver();
		sync.start();
		
		// SavingdataService ss = new SavingdataService();
		// Savingdata dd = ss.findActive();
		// DataThread aa = new DataThread();
		// aa.start();
	}
}
