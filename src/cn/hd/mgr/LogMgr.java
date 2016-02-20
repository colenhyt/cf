package cn.hd.mgr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

public class LogMgr {
	private Vector<String> addedLogs;
	SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	protected Logger  log = Logger.getLogger(getClass()); 

	private static LogMgr uniqueInstance = null;

	public static LogMgr getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new LogMgr();
		}
		return uniqueInstance;
	}
	
	public LogMgr(){
		addedLogs = new Vector<String>();
	}
	
	public void log(String desc){
		String logstr = formatter.format(new Date());
		logstr += " "+(desc);
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();	
			logstr += ",ip:"+(ip);
//			log.warn("ip address "+ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		log.warn(logstr);
		
		addedLogs.add(logstr);
	}
	
	public static void main(String[] args){
		LogMgr l = new LogMgr();
		l.log("aaa");
	}
}
