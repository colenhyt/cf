package cn.hd.cf.service;

import java.util.ArrayList;
import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.base.Bean;
import cn.hd.cf.dao.PlayerMapper;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerExample;
import cn.hd.cf.model.PlayerExample.Criteria;
import cn.hd.cf.model.PlayerWithBLOBs;

public class PlayerService extends BaseService {
	public static String ITEM_KEY = "player";
	private PlayerMapper playerMapper;
	private int nextPlayerId;
	
	public PlayerMapper getPlayerMapper() {
		return playerMapper;
	}

	public void setPlayerMapper(PlayerMapper playerMapper) {
		this.playerMapper = playerMapper;
	}

	public PlayerService()
	{
		nextPlayerId = 0;
		
		initMapper("playerMapper");
		
		initData();
	}
	
	public void initData(){
		PlayerExample example = new PlayerExample();
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		for (int i=0; i<players.size();i++){
			PlayerWithBLOBs player = players.get(i);
			if (player.getPlayerid()>nextPlayerId)
				nextPlayerId = player.getPlayerid();
			
			jedis.hset(ITEM_KEY, player.getPlayerid().toString(), player.toString());
		}
		jedis.close();
	}
	
	public int assignNextId(){
		nextPlayerId++;
		return nextPlayerId;
	}
	public List<PlayerWithBLOBs> findAll(){
		PlayerExample example = new PlayerExample();
		return playerMapper.selectByExampleWithBLOBs(example);
	}
	
	public PlayerWithBLOBs find(int playerid,String strPwd){
		PlayerWithBLOBs player = null;
		String jsonObj = jedis.hget(ITEM_KEY,Integer.valueOf(playerid).toString());
		if (jsonObj!=null){
			player = (PlayerWithBLOBs)Bean.toBean(jsonObj, PlayerWithBLOBs.class);
			if (!player.getPwd().equals(strPwd))
				return null;
		}
		jedis.close();
//		PlayerExample example = new PlayerExample();
//		Criteria criteria=example.createCriteria();
//		criteria.andPlayeridEqualTo(Integer.valueOf(playerid));
//		criteria.andPwdEqualTo(strPwd);
//		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
//		if (players.size()>0)
//			player = players.get(0);
		
		return player;
	}
	
	public PlayerWithBLOBs findByPlayerId(int playerid){
		PlayerWithBLOBs player = null;
		String jsonObj = jedis.hget(ITEM_KEY,Integer.valueOf(playerid).toString());
		if (jsonObj!=null){
			player = (PlayerWithBLOBs)Bean.toBean(jsonObj, PlayerWithBLOBs.class);
		}
		jedis.close();
		
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
		record.setPlayerid(assignNextId());
		jedis.hset(ITEM_KEY,Integer.valueOf(record.getPlayerid()).toString(),record.toString());
		jedis.close();
		
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
		jedis.hset(ITEM_KEY,Integer.valueOf(record.getPlayerid()).toString(),record.toString());
		jedis.close();
		
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
		if (playerName==null)
			System.out.println("客户端提交用户名为空!!");
		else
			System.out.println("客户端提交:"+playerName);
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		
		criteria.andPlayernameEqualTo(playerName);
		List<Player> players = playerMapper.selectByExample(example);
		return players.size()>0;	
	}
}
