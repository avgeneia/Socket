package com.NettyBoot.Business;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.NettyBoot.Common.LogManager;
import com.NettyBoot.DataBase.config.DatabaseConfiguration;
import com.NettyBoot.Redis.RedisComm;
import com.NettyBoot.VO.JobVO;

public class BusinessThread extends Thread {

	/** Logger */
	private Logger logger = LogManager.GetConfiguredLogger(BusinessMain.class);

	int sleep = 0;
	String interfaceId = "";
	String redisKey = "";
	int index = -1;

	//redis 접근을 위한 객체 선언
	private RedisComm rc = null;
	
	SqlSession sqlSession = null;
	
	JobTemplateParser jp = JobTemplateParser.getInstance();
	
	public BusinessThread(String interfaceId, String redisKey, int sleep) {
		// TODO Auto-generated constructor stub
		this.interfaceId = interfaceId;
		this.redisKey = redisKey;
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
        		
        		logger.info("BusinessThread.run :: " + i);
        		
        		String value = rc.pop(redisKey); //queue 인출

        		DataParser dp = new DataParser(value); //인출한 데이터를 가공처리
        		
        		Map<String, String> row = dp.getDataSet(interfaceId);
        		
        		JobVO vo = jp.getJobSchedule(interfaceId);
        		
        		//스케줄이 널이 아닌경우 진행
        		if(vo != null) {
        			
        			process(vo, row, 1);
        		} else {
        			
        			logger.info("잘못된 Interface ID 참조 발생, Thread를 종료합니다.");
        			this.interrupt();
        		}
        	}
        	
        	//commit 단위 고려....
        	sqlSession.commit();
        	Thread.sleep(sleep * 1000);
        	run();
        	
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void process(JobVO vo, Map<String, String> data, int index) {
		
		this.index = index;
		
		//sqlSession.insert("com.NettyBoot.DataBase.dao.MainDao" + "insertTest", row);
		List<Map<String, String>> rowData = vo.getRowdata();
		
		int row = -1;
		for(int n = 0; n < rowData.size(); n++) {
			
			if(Integer.parseInt(rowData.get(n).get("Index")) == index) {
				
				row = n;
				break;
			}
		}
		
		String Type = rowData.get(row).get("Type");
		
		
		switch(Type) {
		
			case "Query":
				
				logger.info(index + " :: " + data);
				query(rowData.get(row), data);
				break;
			case "Log":
				
				break;
		}
		
		//Next 값이 "0" 이면 process를 종료한다.
		int next = Integer.parseInt(rowData.get(row).get("Next"));
		if(next > 0) {
			
			process(vo, data, next);
		} else {
			
			return;
		}
	}
	
	public void query(Map<String, String> row, Map<String, String> data) {
		
		String QueryType = row.get("QueryType");
		
		String sqlId = row.get("SqlID");
		
		switch(QueryType) {
			
			case "insert":
				
				logger.info(QueryType + sqlId);
				//sqlSession.insert("com.NettyBoot.DataBase.dao.MainDao" + sqlId, data);
				break;
			case "update":
				
				logger.info(QueryType + sqlId);
				//sqlSession.update("com.NettyBoot.DataBase.dao.MainDao" + sqlId, data);
				break;
			case "delete":
				
				logger.info(QueryType + sqlId);
				//sqlSession.delete("com.NettyBoot.DataBase.dao.MainDao" + sqlId, data);
				break;
		}
	}
}
