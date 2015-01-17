package cn.hd.cf.service;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.PlayerMapper;
import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerExample;
import cn.hd.cf.model.PlayerExample.Criteria;
import cn.hd.cf.model.PlayerWithBLOBs;

public class PlayerService extends BaseService {
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
	
	public List<PlayerWithBLOBs> findAll(){
		PlayerExample example = new PlayerExample();
		return playerMapper.selectByExampleWithBLOBs(example);
	}
	
	public PlayerWithBLOBs find(int playerid,String strPwd){
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerid));
		criteria.andPwdEqualTo(strPwd);
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		if (players.size()>0)
			return players.get(0);
		
		return null;
	}
	
	public PlayerWithBLOBs findByPlayerId(int playerid){
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayeridEqualTo(Integer.valueOf(playerid));
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		if (players.size()>0)
			return players.get(0);
		
		return null;
	}
	
	public List<PlayerWithBLOBs> findTopMoneyPlayer(int count){
		PlayerExample example = new PlayerExample();
		example.setOrderByClause("money desc");
		List<PlayerWithBLOBs> players = playerMapper.selectByExampleWithBLOBs(example);
		return players;
	}
	
	public boolean add(PlayerWithBLOBs record)
	{
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
		PlayerExample example = new PlayerExample();
		Criteria criteria=example.createCriteria();
		criteria.andPlayernameEqualTo(playerName);
		List<Player> players = playerMapper.selectByExample(example);
		return players.size()>0;	
	}
}
