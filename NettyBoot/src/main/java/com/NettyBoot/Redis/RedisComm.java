package com.NettyBoot.Redis;

import org.apache.log4j.Logger;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.LogManager;

public class RedisComm {
	 
	public static RedisComm redisComm = null;
	
	public static RedisClient redisClient = null;
	public static StatefulRedisConnection<String, String> connection = null;
	 
	// Sync용 command
	public static RedisCommands<String, String> syncCmd = null;
	
	/** Logger */
	static Logger logger = LogManager.GetConfiguredLogger(RedisComm.class);
	 
	public RedisComm() {
		 
		 IniFile ini = IniFile.getInstance();
		 		 
		 String ip = ini.getIni("Redis", "IP");
		 int port = Integer.parseInt(ini.getIni("Redis", "PORT"));
		 //int timeOut = Integer.parseInt(ini.getIni("Redis", "TIMEOUT"));
		 
		 String url = "redis://" + ip + ":" + port + "/0"; 
		 System.out.println(url);
		 redisClient = RedisClient.create(RedisURI.create(url));
		 
		 connection = redisClient.connect();
		 syncCmd = connection.sync();
		 
		 if(connection.isOpen()) {
			 logger.info("REDIS CONNECT!!!");
		 } else {
			 //예외처리
			 logger.error("REDIS NOT CONNECT!!!");			 
		 }
	}
	
	public boolean getConnect() {
		return connection.isOpen();
	} 
	
	public void set(String key, String value) {
		
		IniFile ini = IniFile.getInstance();
		
		boolean skip = Boolean.valueOf(ini.getIni("Redis", "SKIP"));
		
		if(skip != false) {
			syncCmd.lpush(key, value);
			logger.info("REDIS lpush :: " + key + " // " + value);			
		}
	}
	 
}
