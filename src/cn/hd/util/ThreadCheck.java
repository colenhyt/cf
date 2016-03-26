package cn.hd.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

public class ThreadCheck {

	public void check(){
		ThreadMXBean tm = ManagementFactory.getThreadMXBean();
		long[] tid = tm.getAllThreadIds();
		ThreadInfo[] tia = tm.getThreadInfo(tid,Integer.MAX_VALUE);
		long[][] threadArray = new long[tia.length][2];
		for (int i=0;i<tia.length;i++){
			long threadId = tia[i].getThreadId();
			long cputime = tm.getThreadCpuTime(tia[i].getThreadId())/(1000*1000*1000);
			threadArray[i][0] = threadId;
			threadArray[i][1] = cputime;
		}
	}
	
	public static java.util.List<Thread> list_threads(){
	    int tc = Thread.activeCount();
	    Thread[] ts = new Thread[tc];
	    Thread.enumerate(ts);
	    return java.util.Arrays.asList(ts);
	}

	public static Map<Long, Long> cputime(){
	    ThreadMXBean tm = ManagementFactory.getThreadMXBean();
	    tm.setThreadContentionMonitoringEnabled(true);
	    long [] tid = tm.getAllThreadIds();
	    ThreadInfo [] tia = tm.getThreadInfo(tid, Integer.MAX_VALUE);

	    long [][] threadArray = new long[tia.length][2];

	    Map<Long, Long> map = new HashMap<Long, Long>();

	    for (int i = 0; i < tia.length; i++) {
	        long threadId = tia[i].getThreadId();

	        long cpuTime = tm.getThreadCpuTime(tia[i].getThreadId())/(1000*1000*1000);
	        //threadArray[i][0] = threadId;
	        //threadArray[i][1] = cpuTime;
	        map.put(threadId, cpuTime);
	    }
	    return map;
	}	
}
