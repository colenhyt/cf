package cn.hd.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import cn.hd.mgr.DataManager;
import cn.hd.mgr.EventManager;

public class InitServlet extends HttpServlet{
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public void init() throws ServletException {
		DataManager.getInstance().init();
		EventManager.getInstance().start();
		log.info("pingan moneylife init successful!");
	}
}
