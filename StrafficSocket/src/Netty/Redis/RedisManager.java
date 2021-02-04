package Netty.Redis;

import java.util.HashMap;
import java.util.Map;

public class RedisManager {
	
	public static RedisManager rm = null;
	
	public static Map<String, RedisComm> rp = new HashMap<String, RedisComm>();
	
	RedisManager() {
		
	}
	
	public static RedisManager getInstance() {
		
		if(rm == null) {
			
			rm = new RedisManager();
		}
		
		return rm;
	}
	
	public void putRedisPool(String userId, RedisComm ctx) {
		rp.put(userId, ctx);
	}
	
	public RedisComm getRedisPool(String userId) {
		return rp.get(userId);
	}
}
