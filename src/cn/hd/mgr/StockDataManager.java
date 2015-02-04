package cn.hd.mgr;

import java.util.Vector;

import net.sf.json.JSONObject;
import cn.hd.cf.action.RetMsg;
import cn.hd.cf.action.StockAction;
import cn.hd.cf.model.Message;
import cn.hd.cf.model.Stock;
import cn.hd.util.HdTimer;

public class StockDataManager extends HdTimer{
	private int ADD_PERIOD = 20*30;		//20*60: 一小时
	private Vector<Stock>	addStockVect;
	private int tick = 0;
	
    private static StockDataManager uniqueInstance = null;  
	
    public static StockDataManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new StockDataManager();  
        }  
        return uniqueInstance;  
     } 
    
    public StockDataManager(){
    	try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	addStockVect = new Vector<Stock>();
     }
	
	public String addStock(Stock stock){
		addStockVect.add(stock);
		Message msg = new Message();
		msg.setCode(RetMsg.MSG_OK);		//不存在
		JSONObject obj = JSONObject.fromObject(msg);
		return obj.toString();			
	}
	
	private void addStocks(){
		StockAction stockAction = new StockAction();
		for (int i=0;i<addStockVect.size();i++){
			Stock stock = addStockVect.get(i);
			stockAction.addStock(stock);
		}
		System.out.println("购买股票:"+addStockVect.size());
		addStockVect.clear();
	}
		
	public void run(){
    	tick ++;
    	if (addStockVect.size()>0||tick%ADD_PERIOD==0){
    		//addStocks();
    	}
    	//System.out.println("stock run");
	}
    
    public static void main(String[] args) {
    	StockDataManager stmgr = StockDataManager.getInstance();
    }
}
