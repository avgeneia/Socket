package com.NettyBoot.Business;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.NettyBoot.Common.LogManager;
import com.NettyBoot.DataBase.config.DatabaseConfiguration;
import com.NettyBoot.Redis.RedisComm;

public class BusinessThread extends Thread {

	/** Logger */
	private Logger logger = LogManager.GetConfiguredLogger(BusinessMain.class);

	int sleep = 0;
	String redisKey = "";

	//redis 접근을 위한 객체 선언
	private RedisComm rc = null;
	
	SqlSession sqlSession = null;
	
	public BusinessThread(String key, int sleep) {
		// TODO Auto-generated constructor stub
		this.redisKey = key;
		this.sleep = sleep;
		this.rc = new RedisComm();
		
		//openSession(false) : autoCommit - false
		sqlSession = DatabaseConfiguration.getSqlSessionFactory().openSession(false);
	}
	
	public void run(){
		logger.info("Redis Run");
        try {
        	
        	int len = (int) rc.getlen(redisKey);
        	logger.info("Redis len :: " + len);
        	
        	for(int i = 0; i < len; i++) {
        		
        		String value = rc.pop(redisKey);
        		logger.info("Redis pop :: " + value);
        		
        		DataParser dp = new DataParser(value);
        		Map<String, String> row = dp.getDataSet("IF0001");
        		sqlSession.insert("com.NettyBoot.DataBase.dao.MainDao" + "insertTest", row);
        	}
        	
        	//commit 단위 고려....
        	sqlSession.commit();
        	Thread.sleep(sleep);
        	run();
        	
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
