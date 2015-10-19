package cn.hd.cf.service;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import redis.clients.jedis.Jedis;
import cn.hd.base.BaseService;
import cn.hd.base.Bean;
import cn.hd.cf.dao.PlayerMapper;
import cn.hd.cf.dao.QuestMapper;
import cn.hd.cf.dao.SigninMapper;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerExample;
import cn.hd.cf.model.PlayerExample.Criteria;
import cn.hd.cf.model.PlayerWithBLOBs;
import cn.hd.cf.model.Quest;
import cn.hd.cf.model.Signin;

public class PlayerService extends BaseService {
	public static String ITEM_KEY = "player";
	private PlayerMapper playerMapper;
	private SigninMapper signinMapper;
	private QuestMapper questMapper;
	
	public QuestMapper getQuestMapper() {
		return questMapper;
	}

	public void setQuestMapper(QuestMapper questMapper) {
		this.questMapper = questMapper;
	}

	public SigninMapper getSigninMapper() {
		return signinMapper;
	}

	public void setSigninMapper(SigninMapper signinMapper) {
		this.signinMapper = signinMapper;
	}

	public PlayerMapper getPlayerMapper() {
		return playerMapper;
	}

	public void setPlayerMapper(PlayerMapper playerMapper) {
		this.playerMapper = playerMapper;
	}

	public PlayerService()
	{
		initMapper("playerMapper","signinMapper","questMapper");
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
	
	public PlayerWithBLOBs findByKey(String playerName,String playerTel){
		PlayerWithBLOBs player = null;
	
		synchronized(this){
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayernameEqualTo(playerName);
		if (playerTel!=null)
			criteria.andTelEqualTo(playerTel);
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		if (players.size()>0)
			player = players.get(0);
		}
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
	
	public synchronized boolean addPlayers(Vector<PlayerWithBLOBs> records)
	{
		try {
		for (int i=0;i<records.size();i++){
			playerMapper.insert(records.get(i));
		}			
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addDoneQuest(int playerId){
		try {
			Quest record = new Quest();
			record.setPlayerid(playerId);
			record.setCrdate(new Date());
			questMapper.insertSelective(record);
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public boolean updatePlayers(List<PlayerWithBLOBs> records)
	{
		try {
			for (int i=0;i<records.size();i++){
				PlayerWithBLOBs record = records.get(i);
				playerMapper.updateByPrimaryKeySelective(record);
			}
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
	
	public boolean have(String playerName,String playerTel){	
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		
		criteria.andPlayernameEqualTo(playerName);
		criteria.andTelEqualTo(playerTel);
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

	public boolean addSignin(int playerId){
		try {
			Signin record = new Signin();
			record.setPlayerid(playerId);
			record.setCrdate(new Date());
		signinMapper.insertSelective(record);
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return true;
	}
}
