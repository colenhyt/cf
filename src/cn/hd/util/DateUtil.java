package cn.hd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String format(Date date,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		if (date!=null) {
			return sdf.format(date);
		}
		return "";
	}
	public static Date parse(String date,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		if (date!=null) {
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 计算两个时间的时间差(毫秒)
	 * @param data1
	 * @param date2
	 * @return
	 */
	public static long differ(Date date1,Date date2){
		if (date1!=null && date2!=null) {
			return date2.getTime()-date1.getTime();
		}
		return 0;
	}
	
	public static boolean isToday(Date date1){
		if (date1==null)
			return false;
		
		Date now = new Date();
	    Calendar cCurr = Calendar.getInstance(); 
	    cCurr.setTime(now);
	    int nowYear = cCurr.get(Calendar.YEAR);
	    int nowDay = cCurr.get(Calendar.DAY_OF_YEAR);
	    cCurr.setTime(date1);		
	    int dYear = cCurr.get(Calendar.YEAR);
	    int dDay = cCurr.get(Calendar.DAY_OF_YEAR);
	    
	    return (nowYear==dYear&&nowDay==dDay);
	}
}
