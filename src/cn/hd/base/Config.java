package cn.hd.base;

import cn.hd.util.RedisConfig;

public class Config {
	private int serverid = 0;
	private int count = 0;
	private RedisConfig redisCfg = null;
	public RedisConfig getRedisCfg() {
		return redisCfg;
	}
	public void setRedisCfg(RedisConfig redisCfg) {
		this.redisCfg = redisCfg;
	}
	public int getServerid() {
		return serverid;
	}
	public void setServerid(int serverid) {
		this.serverid = serverid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
