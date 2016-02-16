package cn.hd.mgr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class LogMgr {
	private Vector<String> addedLogs;
	SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

	public LogMgr(){
		addedLogs = new Vector<String>();
	}
	
	public void log(String desc){
		String log = formatter.format(new Date());
		log += ":"+(desc);
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();	
			log += ",ip:"+(ip);
//			log.warn("ip address "+ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		System.out.println(log);
		
		addedLogs.add(log);
	}
	
	public static void main(String[] args){
		LogMgr l = new LogMgr();
		l.log("aaa");
	}
}
