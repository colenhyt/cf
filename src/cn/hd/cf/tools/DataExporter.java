package cn.hd.cf.tools;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import cn.hd.base.Config;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.Toplist;
import cn.hd.cf.service.PlayerService;
import cn.hd.mgr.DataManager;
import cn.hd.mgr.MgrBase;
import cn.hd.mgr.ToplistManager;
import cn.hd.util.FileUtil;
import cn.hd.util.RedisClient;

import com.alibaba.fastjson.JSON;

public class DataExporter {
	Jedis jedis = null;
	
	public DataExporter(){
		String cfgstr = FileUtil.readFile("./WEB-INF/classes/"+ "config.properties");
		if (cfgstr == null || cfgstr.trim().length() <= 0) {
			return;
		}
		Config cfg = (Config) JSON.parseObject(cfgstr, Config.class);
		RedisClient jedisClient = new RedisClient(cfg.getRedisCfg());	
		jedis = jedisClient.getJedis();		
	}
	public List<String> getRedis(String itemkey){
		if (jedis==null){
			System.out.println("jedis init failed");
		}
		return jedis.hvals(itemkey);
	}
	
	public void flushPlayerDB(){
		List<String> itemstrs = getRedis(MgrBase.DATAKEY_PLAYER);
		PlayerService  service = new PlayerService();
		List<Player>  newP = new ArrayList<Player>();
		List<Player>  updateP = new ArrayList<Player>();
    	for (String str:itemstrs){
			Player item = (Player)JSON.parseObject(str, Player.class);
			Player p = service.findByName(item.getPlayername());
			if (p==null){
				newP.add(item);
			}else {
				updateP.add(item);
			}
			System.out.println("add new players:"+newP.size());
			service.addPlayers(newP);
			System.out.println("update players:"+updateP.size());
			service.updatePlayers(updateP);
    	}		
	}
	public void exportToplist(){
		ToplistManager.getInstance().init();
		DataManager.getInstance().init();
		String monthStr = "2015-11-12";
		List<Toplist> items = ToplistManager.getInstance().getMonthItems(monthStr);
		System.out.println("get month("+monthStr+") toplist:"+items.size());
		String content = new String();
		for (Toplist item:items){
			Player p = DataManager.getInstance().findPlayer(item.getPlayerid());
			if (p!=null){
				item.setOpenid(p.getOpenid());
			}
			content += JSON.toJSONString(item)+"\n";
		}
		FileUtil.writeFile("./toplist.data", content);;
	}
	
	public void exportItems(String itemkey){
		List<String> items = getRedis(MgrBase.DATAKEY_PLAYER);
		System.out.println(itemkey+" size:"+items.size());
		for (int i=0;i<items.size();i++){
			System.out.println("player:"+items.get(i));
		}		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataExporter exporter = new DataExporter();
		exporter.exportToplist();
//    	for (String str:itemstrs){
//    		Toplist item = (Toplist)JSON.parseObject(str, Toplist.class);
//    		System.out.println("toplist:"+str);
//    	}		
	}

}
