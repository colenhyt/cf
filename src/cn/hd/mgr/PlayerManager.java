package cn.hd.mgr;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cn.hd.cf.model.Insure;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Saving;
import cn.hd.cf.model.Stock;
import cn.hd.cf.service.InsureService;
import cn.hd.cf.service.PlayerService;
import cn.hd.cf.service.SavingService;
import cn.hd.cf.service.StockService;
import cn.hd.cf.service.ToplistService;

public class PlayerManager {
	private int WEEK_UPDATE_PERIOD = 20;		//20*60: 一小时
	private SavingService savingService;
	private StockService stockService;
	private ToplistService toplistService;
	private InsureService insureService;
	private PlayerService playerService;
	private int tick = 0;
	
    private static PlayerManager uniqueInstance = null;  
	
    public static PlayerManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new PlayerManager();  
        }  
        return uniqueInstance;  
     } 
    
    public PlayerManager(){
    	playerService = new PlayerService();
    	savingService = new SavingService();
    	insureService = new InsureService();
    	stockService = new StockService();
    	toplistService = new ToplistService();
    	updateToplist();
     }
  
    public void updateToplist(){
		Date d1 = new Date();
	      Calendar cl = Calendar. getInstance();
	      cl.setTime(d1);
	      
	      System.out.println("更新排行榜财富和数据");
	      
	      int dayOfYear = cl.get(Calendar.DAY_OF_YEAR);
	      int currWeek = dayOfYear/7;
	      if (dayOfYear%7!=0)
	    	  currWeek++;    	
	      //更新最新的财富值:
	      List<PlayerWithBLOBs> all = playerService.findAll();
	      for (int i=0;i<all.size();i++){
    		PlayerWithBLOBs pp = all.get(i);
    		float money = this.calculatePlayerMoney(pp);
    		pp.setMoney(money);
    		toplistService.updateData(pp,money,currWeek);
    	}
    }
    
    private float calculatePlayerMoney(PlayerWithBLOBs player){
    	float amount = 0;
    	List<Saving> saving = savingService.findByPlayerId(player.getPlayerid());
    	for (int i=0;i<saving.size();i++){
    		amount += saving.get(i).getAmount();
    	}
    	List<Insure> insure = insureService.findByPlayerId(player.getPlayerid());
    	for (int i=0;i<insure.size();i++){
    		amount += insure.get(i).getAmount();
    	}
    	List<Stock> stock = stockService.findByPlayerId(player.getPlayerid());
    	for (int i=0;i<stock.size();i++){
    		Stock ps = stock.get(i);
    		if (ps==null) continue;
    		List<Quote> qq = StockManager.getInstance().getLastQuotes(ps.getItemid());
    		if (qq.size()>0)
    			amount += qq.get(0).getPrice()*ps.getQty();
    	}   	
    	
    	return amount;
    }
    public void update(){
    	tick ++;
    	if (tick%WEEK_UPDATE_PERIOD==0){
    		updateToplist();
    	}
	}
    
    public static void main(String[] args) {
    	PlayerManager stmgr = PlayerManager.getInstance();
    	stmgr.updateToplist();
    	ToplistService toplistService = new ToplistService();
    	//int b = toplistService.findCountByGreaterMoney(0,199);
    }
}
