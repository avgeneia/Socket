package com.NettyBoot.Business;

import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.NettyBoot.DataBase.config.DatabaseConfiguration;
import com.NettyBoot.Redis.RedisComm;
import com.NettyBoot.VO.JobVO;

public class BusinessThread extends Thread {

	/** Logger */
	private final Logger logger = LogManager.getLogger(BusinessThread.class);

	int sleep = 0;
	String interfaceId = "";
	String redisKey = "";
	int index = -1;

	//redis 접근을 위한 객체 선언
	private RedisComm rc = null;
	
	SqlSession sqlSession = null;
	
	JobTemplateParser jp = JobTemplateParser.getInstance();
	
	String defaultSql = "com.NettyBoot.DataBase.dao.MainDao" + ".";

	public BusinessThread(String redisKey, int sleep) {
		// TODO Auto-generated constructor stub
		printLog("Business init : " + redisKey);
		this.redisKey = redisKey;
		this.sleep = sleep;
		this.rc = new RedisComm();
		
		//openSession(false) : autoCommit - false
		sqlSession = DatabaseConfiguration.getSqlSessionFactory().openSession(false);
	}
	
	public void run(){
		
		printLog("Redis Run : " + this.redisKey);
		
		while(true) {
			
			int len = (int) rc.getlen(redisKey);
			
			printLog("Redis len :: " + len + " :: " + this.redisKey);
			
			for(int i = 0; i < len; i++) {
	    		
				printLog("BusinessThread.run :: " + i);
	    		
	    		String value = rc.pop(redisKey); //queue 인출
	    		
	    		if(value == null) {
	    			continue;
	    		}
	    		
	    		DataParser dp = new DataParser(value); //인출한 데이터를 가공처리
	    		
	    		interfaceId = dp.getInterfaceList().get(0);
	    		
	    		Map<String, String> row = dp.getDataSet(interfaceId);
	    		
	    		if(row.size() == 0) {
	    			continue;
	    		}
	    		
	    		JobVO vo = jp.getJobSchedule(interfaceId);
	    		
	    		//스케줄이 널이 아닌경우 진행
	    		if(vo != null) {
	    			
	    			process(vo, row, 1);
	    		} else {
	    			
	    			printLog("잘못된 Interface ID 참조 발생, Thread를 종료합니다.");
	    			this.interrupt();
	    		}
	    	}
			
	    	//commit 단위 고려....
	    	sqlSession.commit();
	    	
			try {
				
				Thread.sleep(this.sleep * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
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
		
		//다음에 실행해야하는 Process 세팅
		int next = -1; 
		
		switch(Type) {
		
			case "Query":
				
				printLog(index + " :: " + data);
				int rtn = query(rowData.get(row), data);
				
				next = Integer.parseInt(rowData.get(row).get("Next"));					
				break;
			
			case "If":
				
				//xml에서 읽어온 조건부 String 데이터를 변환하여 진행하기 위한 JS 객체 선언
				ScriptEngineManager mng = new ScriptEngineManager();
				ScriptEngine eng = mng.getEngineByName("js");
				
				// rowData = JOB.xml
				String leftValue = rowData.get(row).get("Input").toString().split(",")[0];
				String condition = rowData.get(row).get("Condition").toString();
				String rightValue = rowData.get(row).get("Input").toString().split(",")[1];
				
				leftValue = data.get(leftValue);
				rightValue = data.get(rightValue);
				
				boolean result = false;
				try {
					result = (boolean) eng.eval(Long.parseLong(leftValue) + condition + Long.parseLong(rightValue));
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//결과에 따라 다음 프로세스를 진행한다.
				if(result) {
					next = Integer.parseInt(rowData.get(row).get("TruePath"));
				} else {
					next = Integer.parseInt(rowData.get(row).get("FalsePath"));
				}
				
				break;
				
			case "Log":
				
				break;
		}
		
		//Next 값이 "0" 이면 process를 종료한다.
		if(next > 0) {
			
			process(vo, data, next);
		} else {
			
			return;
		}
	}
	
	public int query(Map<String, String> row, Map<String, String> data) {
		
		String QueryType = row.get("QueryType");
		
		String sqlId = row.get("SqlID");
		
		int result = -1;
		try {
			
			switch(QueryType) {
			
				case "insert":
					
					printLog(QueryType + sqlId);
					result = sqlSession.insert(defaultSql + sqlId, data);
					break;
				case "update":
					
					printLog(QueryType + sqlId);
					result = sqlSession.update(defaultSql + sqlId, data);					
					break;
				case "delete":
					
					printLog(QueryType + sqlId);
					result = sqlSession.delete(defaultSql + sqlId, data);
					break;
			}
			
		} catch(Exception e) {
			result = -1;
			printLog("잘못된 SQL ID 참조 발생.");
		}
		
		return result;
	}
	
	public static Object eval(String pkg) throws Exception {
		
		Class<?> clazz = Class.forName(pkg);
		return clazz.newInstance();
	}
	
	public void printLog(String msg) {
		
		logger.info(msg);
		//System.out.println(msg);
	}
}
