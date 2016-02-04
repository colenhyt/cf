package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import cn.hd.base.BaseService;
import cn.hd.base.Config;
import cn.hd.cf.action.LoginAction;
import cn.hd.cf.model.Init;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Saving;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.tools.InitdataService;

import com.alibaba.fastjson.JSON;

public class DataSync extends MgrBase {
	protected Logger log = Logger.getLogger(getClass());
	public List<String> pps = new ArrayList<String>();

	private LoginAction loginAction;
	private Init init;
	public Config cfg;

	public Map<String, Integer> playerIdMaps;
	public Map<Integer, PlayerWithBLOBs> playerMaps;
	private int nextPlayerId;

	private static DataSync uniqueInstance = null;

	public static DataSync getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new DataSync();
		}
		return uniqueInstance;
	}

	public DataSync() {
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
	}

	public static void main(String[] args) {
		DataSync sync = DataSync.getInstance();
		
		// SavingdataService ss = new SavingdataService();
		// Savingdata dd = ss.findActive();
		// DataThread aa = new DataThread();
		// aa.start();
	}
}
