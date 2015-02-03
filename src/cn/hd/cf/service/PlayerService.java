package cn.hd.cf.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import redis.clients.jedis.Jedis;
import cn.hd.base.BaseService;
import cn.hd.base.Bean;
import cn.hd.cf.dao.PlayerMapper;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerExample;
import cn.hd.cf.model.PlayerExample.Criteria;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.mgr.DataManager;

public class PlayerService extends BaseService {
	public static String ITEM_KEY = "player";
	private PlayerMapper playerMapper;
	
	public PlayerMapper getPlayerMapper() {
		return playerMapper;
	}

	public void setPlayerMapper(PlayerMapper playerMapper) {
		this.playerMapper = playerMapper;
	}

	public PlayerService()
	{
		initMapper("playerMapper");
	}
	
	public int initData(Jedis jedis2){
		int nextPlayerId = 0;
		PlayerExample example = new PlayerExample();
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		for (int i=0; i<players.size();i++){
			PlayerWithBLOBs player = players.get(i);
			if (player.getPlayerid()>nextPlayerId)
				nextPlayerId = player.getPlayerid();
			
		}
		return nextPlayerId;
	}
	
	public List<PlayerWithBLOBs> findAll(){
		PlayerExample example = new PlayerExample();
		return playerMapper.selectByExampleWithBLOBs(example);		
	}
	
	public PlayerWithBLOBs findByName(String playerName){
		PlayerWithBLOBs player = null;
	
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayernameEqualTo(playerName);
//		criteria.andPwdEqualTo(strPwd);
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		if (players.size()>0)
			player = players.get(0);
		
		return player;
	}
	
	public PlayerWithBLOBs findByPlayerId(int playerid){
		PlayerWithBLOBs player = null;
		if (jedis!=null){
		String jsonObj = jedis.hget(ITEM_KEY,Integer.valueOf(playerid).toString());
		if (jsonObj!=null){
			player = (PlayerWithBLOBs)Bean.toBean(jsonObj, PlayerWithBLOBs.class);
		}
		jedis.close();
		return player;
		}
		
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerid));
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		if (players.size()>0)
			player = players.get(0);
		
		return player;
	}
	
	public boolean add(PlayerWithBLOBs record)
	{
		if (jedis!=null){
		jedis.hset(ITEM_KEY,Integer.valueOf(record.getPlayerid()).toString(),record.toString());
		jedis.close();
		}
		
		try {
		playerMapper.insertSelective(record);
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateByKey(PlayerWithBLOBs record)
	{
		try {
			playerMapper.updateByPrimaryKeySelective(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean have(String playerName){
		if (jedis!=null){
		Collection<String> l = jedis.hgetAll(ITEM_KEY).values();
		boolean exist = false;
		for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
			  String str = (String)iter.next();
			  PlayerWithBLOBs player = (PlayerWithBLOBs)Bean.toBean(str,PlayerWithBLOBs.class);
			  if (player.getPlayername().equals(playerName)){
				  exist = true;
				  break;
			  }
		}		
		jedis.close();
		return exist;
		}
		
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		
		criteria.andPlayernameEqualTo(playerName);
		List<Player> players = playerMapper.selectByExample(example);
		return players.size()>0;	
	}

	public PlayerWithBLOBs find(int playerid){
			PlayerWithBLOBs player = null;
			if (jedis!=null){
			String jsonObj = jedis.hget(ITEM_KEY,Integer.valueOf(playerid).toString());
			if (jsonObj!=null){
				player = (PlayerWithBLOBs)Bean.toBean(jsonObj, PlayerWithBLOBs.class);
	//			if (!player.getPwd().equals(strPwd))
	//				return null;
			}
			jedis.close();
			return player;
			}
			
			PlayerExample example = new PlayerExample();
			Criteria criteria=example.createCriteria();
			criteria.andPlayeridEqualTo(Integer.valueOf(playerid));
	//		criteria.andPwdEqualTo(strPwd);
			List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
			if (players.size()>0)
				player = players.get(0);
			
			return player;
		}
}
