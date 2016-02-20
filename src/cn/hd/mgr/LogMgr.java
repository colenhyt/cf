package cn.hd.mgr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import cn.hd.util.RedisClient;

public class LogMgr  extends MgrBase {
	private Vector<String> addedLogs;
	SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	protected Logger  log = Logger.getLogger(getClass()); 
	String ipAddStr;

	private static LogMgr uniqueInstance = null;

	public static LogMgr getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new LogMgr();
		}
		return uniqueInstance;
	}
	
	public LogMgr(){
		addedLogs = new Vector<String>();
		
		dataThreads = new Vector<DataThread>();
		
		for (int i=0;i<redisCfg4.getThreadCount();i++){
			 //read:
			
			 //write:
			 DataThread dataThread = new DataThread(redisCfg4);
			dataThreads.add(dataThread);
			dataThread.setUpdateDuration(2000);
			dataThread.start();
		 }		
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ipAddStr =addr.getHostAddress().toString();	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			log.warn(e.getMessage());;
		}		
	}
	
	public void log(int playerid,String desc){
		String str = "ip:"+(ipAddStr)+" "+(desc);
		log.warn(str);
		String logstr = formatter.format(new Date());
		logstr += " "+str;
		int index = playerid%dataThreads.size();
		//dataThreads.get(index).addLog(playerid,logstr);
	}
	
	public static void main(String[] args){
		LogMgr l = new LogMgr();
		l.log(1,"aaa");
	}
}
