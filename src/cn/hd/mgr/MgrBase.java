package cn.hd.mgr;


import java.net.URL;
import java.util.Vector;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import cn.hd.base.Config;
import cn.hd.util.FileUtil;
import cn.hd.util.RedisClient;
import cn.hd.util.RedisConfig;

import com.alibaba.fastjson.JSON;

public class MgrBase {
	public final static String DATAKEY_PLAYER = "player";
	public final static String DATAKEY_SIGNIN = "signin";
	public final static String DATAKEY_QUEST = "quest";
	public final static String DATAKEY_PLAYER_ID = "playerid";
	public final static String DATAKEY_GUID_PLAYER = "guidplayer";
	public final static String DATAKEY_SAVING = "saving";
	public final static String DATAKEY_INSURE = "insure";
	public final static String DATAKEY_STOCK = "stock";
	public final static String DATAKEY_TOPLIST = "toplist";
	protected Logger  log = Logger.getLogger(getClass()); 
	protected int tick = 0;
	protected final int UPDATE_PERIOD = 20*30;		//20*60: 一小时
	protected final int BATCH_COUNT = 200;
	protected final int UPDATE_PERIOD_BATCH = 40;	//2分钟
	protected Vector<DataThread>	dataThreads;
	protected RedisClient		jedisClient;
	public Config cfg;
	public RedisConfig redisCfg;
	public RedisConfig redisCfg1;
	public RedisConfig redisCfg2;
	public int threadCount;
	public JSONObject cfgObj;
	public String openidurl;
	public String openidparam;

	public MgrBase(){
		String path = "./WEB-INF/classes/";
		URL  res = Thread.currentThread().getContextClassLoader().getResource("/");
		if (res!=null)
			path = res.getPath();
		String cfgstr = FileUtil.readFile(path + "config.properties");
		if (cfgstr == null || cfgstr.trim().length() <= 0) {
			return;
		}
		cfgObj = JSONObject.fromObject(cfgstr);
		
		
		String cfgstr0 = cfgObj.getString("redisCfg");
		redisCfg = JSON.parseObject(cfgstr0, RedisConfig.class);
		
		String cfgstr1 = cfgObj.getString("redisCfg1");
		redisCfg1 = JSON.parseObject(cfgstr1, RedisConfig.class);
		
		String cfgstr2 = cfgObj.getString("redisCfg2");
		redisCfg2 = JSON.parseObject(cfgstr2, RedisConfig.class);

		cfg = (Config) JSON.parseObject(cfgstr, Config.class);
		 
//		 String threadCountStr = cfgObj.getString("threadCount");
//		 threadCount = 3;
//		 if (threadCountStr!=null)
//			 threadCount = Integer.valueOf(threadCountStr);
//		 dataThreads = new Vector<DataThread>();
//		 for (int i=0;i<threadCount;i++){
//				DataThread dataThread = new DataThread(cfg.getRedisCfg());
//				dataThreads.add(dataThread);
//				dataThread.start();
//		 }
		
	}
}
