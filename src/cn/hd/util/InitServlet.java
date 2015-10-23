package cn.hd.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import cn.hd.mgr.DataManager;
import cn.hd.mgr.EventManager;
import cn.hd.mgr.InsureManager;
import cn.hd.mgr.SavingManager;
import cn.hd.mgr.StockManager;
import cn.hd.mgr.ToplistManager;

public class InitServlet extends HttpServlet{
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public void init() throws ServletException {
		DataManager.getInstance().init();
		SavingManager.getInstance().init();
		InsureManager.getInstance().init();
		StockManager.getInstance().init();
		ToplistManager.getInstance().init();
		EventManager.getInstance().start();
		
		log.info("pingan moneylife init successful!");
	}
}
