package cn.hd.mgr;


import java.net.URL;
import java.util.Vector;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.hd.base.Config;
import cn.hd.util.FileUtil;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;

public class MgrBase {
	public final static String DATAKEY_PLAYER = "player";
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
//		openidurl = cfgObj.getString("openidurl");
//		openidparam = cfgObj.getString("openidparam");
	cfg = (Config) JSON.parseObject(cfgstr, Config.class);
		jedisClient = new RedisClient(cfg.getRedisCfg());
		 
		 dataThreads = new Vector<DataThread>();
		 String threadCountStr = cfgObj.getString("threadCount");
		 int threadCount = 3;
		 if (threadCountStr!=null)
			 threadCount = Integer.valueOf(threadCountStr);
		 for (int i=0;i<threadCount;i++){
				DataThread dataThread = new DataThread(cfg.getRedisCfg());
				dataThreads.add(dataThread);
				dataThread.start();
		 }
		
	}
}
