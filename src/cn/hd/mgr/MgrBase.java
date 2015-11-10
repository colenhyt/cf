package cn.hd.mgr;

import org.apache.log4j.Logger;

import cn.hd.util.RedisClient;

public class MgrBase {
	public final String DATAKEY_PLAYER = "player";
	public final String DATAKEY_SAVING = "saving";
	public final String DATAKEY_INSURE = "insure";
	public final String DATAKEY_STOCK = "stock";
	public final String DATAKEY_TOPLIST = "toplist";
	protected Logger  log = Logger.getLogger(getClass()); 
	protected int tick = 0;
	protected final int UPDATE_PERIOD = 20*30;		//20*60: 一小时
	protected final int BATCH_COUNT = 200;
	protected final int UPDATE_PERIOD_BATCH = 40;	//2分钟
	protected DataThread dataThread;
	protected RedisClient		jedisClient;

	public MgrBase(){
		jedisClient = new RedisClient();
		dataThread = new DataThread();
		 dataThread.start();
		
	}
}
