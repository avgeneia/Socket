package com.NettyBoot.Redis;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.NettyBoot.Common.CmmUtil;
import com.NettyBoot.Common.IniFile;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

public class RedisComm {
	 
	public static RedisComm redisComm = null;
	
	public static RedisClient redisClient = null;
	public static StatefulRedisConnection<String, String> connection = null;
	 
	// Sync용 command
	public static RedisCommands<String, String> syncCmd = null;
	
	/** Logger */
	static Logger logger = LogManager.getLogger(RedisComm.class);
	 
	public RedisComm() {
		 
		 IniFile ini = IniFile.getInstance();
		 		 
		 String ip = ini.getIni("Redis", "IP");
		 int port = Integer.parseInt(ini.getIni("Redis", "PORT"));
		 //int timeOut = Integer.parseInt(ini.getIni("Redis", "TIMEOUT"));
		 
		 String url = "redis://" + ip + ":" + port + "/0"; 
		 redisLog(url);
		 redisClient = RedisClient.create(RedisURI.create(url));
		 
		 connection = redisClient.connect();
		 syncCmd = connection.sync();
		 
		 if(connection.isOpen()) {
			 
			 redisLog("REDIS CONNECT!!!");
		 } else {
			 
			 //예외처리
			 redisLog("REDIS NOT CONNECT!!!");			 
		 }
	}
	
	public boolean getConnect() {
		
		return connection.isOpen();
	} 
	
	public void set(String key, String value) {
		
		IniFile ini = IniFile.getInstance();
		
		boolean skip = Boolean.valueOf(ini.getIni("Redis", "SKIP"));
		
		if(skip != true) {
			
			syncCmd.lpush(key, value);
			redisLog("REDIS lpush :: " + key + " // " + value);			
		}
	}
	
	public String rpop(String key) {
		redisLog("REDIS pop :: " + key);
		String lpop = syncCmd.rpop(key);
		
		return lpop;
	} 
	
	public List<String> getKeys() {
		
		return syncCmd.keys("*");
	}
	
	public long getlen(String key) {
		// TODO Auto-generated method stub
		return syncCmd.llen(key);
	}
	
	public void redisLog(String msg) {
		
		CmmUtil.print("i", msg);
	}
}
