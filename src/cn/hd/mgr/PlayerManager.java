package cn.hd.mgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static int DAY_SECOND = 24;
	private int STOCK_UPDATE_PERIOD = 1;
	private int STOCK_SAVE_PERIOD = 3600;
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
     }
  
    public void updateMoney(){
    	List<PlayerWithBLOBs> all = playerService.findAll();
    	for (int i=0;i<all.size();i++){
    		PlayerWithBLOBs pp = all.get(i);
    		float money = this.calculatePlayerMoney(pp);
    		pp.setMoney(money);
    		toplistService.updateData(pp,money);
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
    	updateMoney();
	}
    
    public static void main(String[] args) {
    	PlayerManager stmgr = PlayerManager.getInstance();
    	stmgr.updateMoney();

    	int a = 10;
    }
}
