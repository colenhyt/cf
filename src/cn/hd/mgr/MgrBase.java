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
	public final static String DATAKEY_DATA_INIT = "initdata";
	public final static String DATAKEY_DATA_SAVING = "savingdata";
	public final static String DATAKEY_DATA_INSURE = "insuredata";
	public final static String DATAKEY_DATA_STOCK = "stockdata";
	public final static String DATAKEY_DATA_QUEST = "questdata";
	protected Logger  log = Logger.getLogger(getClass()); 
	protected int tick = 0;
	protected final int UPDATE_PERIOD = 20*30;		//20*60: 一小时
	protected final int BATCH_COUNT = 200;
	protected final int UPDATE_PERIOD_BATCH = 40;	//2分钟
	protected Vector<DataThread>	dataThreads;
	protected long toplistTime = 600;
	protected RedisClient		jedisClient;
	protected RedisClient		jedisClient3;
	public Config cfg;
	public RedisConfig redisCfg;
	public RedisConfig redisCfg1;
	public RedisConfig redisCfg2;
	public RedisConfig redisCfg3;
	public int threadCount;
	public JSONObject cfgObj;
	public String openidurl;
	public String openidparam;

	public MgrBase(){
		String path = "/root/";
		URL  res = Thread.currentThread().getContextClassLoader().getResource("/");
		String cfgstr = FileUtil.readFile(path + "config.properties");
		if (cfgstr == null || cfgstr.trim().length() <= 0) {
			cfgstr = FileUtil.readFile(res.getPath() + "config.properties");
			if (cfgstr==null|| cfgstr.trim().length() <= 0){
			log.warn("game start failed: "+path);
			return;
			}
		}
		cfgObj = JSONObject.fromObject(cfgstr);
		
		log.warn("get cfg file "+path);
		
		String cfgstr0 = cfgObj.getString("redisCfg");
		redisCfg = JSON.parseObject(cfgstr0, RedisConfig.class);
		
		String cfgstr1 = cfgObj.getString("redisCfg1");
		redisCfg1 = JSON.parseObject(cfgstr1, RedisConfig.class);
		
		String cfgstr2 = cfgObj.getString("redisCfg2");
		redisCfg2 = JSON.parseObject(cfgstr2, RedisConfig.class);
		
		String cfgstr3 = cfgObj.getString("redisCfg3");
		redisCfg3 = JSON.parseObject(cfgstr3, RedisConfig.class);

		if (cfgObj.containsKey("toplistTime")){
			toplistTime = Long.valueOf(cfgObj.getString("toplistTime"));
		}
		jedisClient3 = new RedisClient(redisCfg3);
		
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
