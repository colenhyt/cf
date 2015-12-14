package cn.hd.cf.service;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import cn.hd.base.BaseService;
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
	
	public List<Player> findAll(){
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		return playerMapper.selectByExample(example);		
	}
	
	public Player findByName(String playerName){
		Player player = null;
	
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayernameEqualTo(playerName);
//		criteria.andPwdEqualTo(strPwd);
		List<Player> players = playerMapper.selectByExample(example);
		if (players.size()>0)
			player = players.get(0);
		
		return player;
	}
	
	public Player findByKey(String playerName,String playerTel){
		Player player = null;
	
		synchronized(this){
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayernameEqualTo(playerName);
		if (playerTel!=null)
			criteria.andTelEqualTo(playerTel);
		List<Player> players = playerMapper.selectByExample(example);
		if (players.size()>0)
			player = players.get(0);
		}
		return player;
	}	
	public Player findByPlayerId(int playerid){
		Player player = null;
		
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerid));
		List<Player> players = playerMapper.selectByExample(example);
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
	
	public boolean addDoneQuests(Vector<Integer> playerids){
		try {
			for (int i=0;i<playerids.size();i++){
			Quest record = new Quest();
			record.setPlayerid(playerids.get(i));
			record.setCrdate(new Date());
			questMapper.insertSelective(record);
			}
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

	public Player find(int playerid){
			Player player = null;
			PlayerExample example = new PlayerExample();
			Criteria criteria=example.createCriteria();
			criteria.andPlayeridEqualTo(Integer.valueOf(playerid));
	//		criteria.andPwdEqualTo(strPwd);
			List<Player> players = playerMapper.selectByExample(example);
			if (players.size()>0)
				player = players.get(0);
			
			return player;
		}

	public boolean addSignins(Vector<Integer> playerids){
		try {
			for (int i=0;i<playerids.size();i++){
				Signin record = new Signin();
				record.setPlayerid(playerids.get(i));
				record.setCrdate(new Date());
			signinMapper.insertSelective(record);				
			}
		DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return true;
	}
}
