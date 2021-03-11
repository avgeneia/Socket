package com.NettyBoot.Business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.NettyBoot.Common.IniFile;
import com.NettyBoot.DataBase.config.DatabaseConfiguration;
import com.NettyBoot.Handler.MessageHandler;
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
	
	String defaultSql = "MainDao" + ".";
	
	//파일 처리를 위한 전역변수
	File file = null;
	BufferedWriter writer = null;
	
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
	    		
	    		Map<String, Object> row = dp.getDataSet(interfaceId);
	    		
	    		if(row.size() == 0) {
	    			continue;
	    		}
	    		
	    		JobVO vo = jp.getJobSchedule(interfaceId);
	    		
	    		//스케줄이 널이 아닌경우 진행
	    		if(vo != null) {
	    			
	    			printLog("Run Process :: " + interfaceId);
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
	
	public void process(JobVO vo, Map<String, Object> data, int index) {
		
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
		
		printLog(index + " :: " + Type + " :: " + data);
		switch(Type) {
		
			case "Query":
				
				data.put("JobResult", String.valueOf(query(rowData.get(row), data)));
				
				next = Integer.parseInt(rowData.get(row).get("Next"));					
				break;
			
			case "If":
				
				//xml에서 읽어온 조건부 String 데이터를 변환하여 진행하기 위한 JS 객체 선언
				ScriptEngineManager mng = new ScriptEngineManager();
				ScriptEngine eng = mng.getEngineByName("js");
				
				// rowData = JOB.xml
				String conditionExpr = rowData.get(row).get("Condition").toString();
				
				conditionExpr = setCondition(conditionExpr, data);
				
				boolean result = false;
				try {
					
					result = (boolean) eng.eval(conditionExpr);
				} catch (ScriptException e) {
					
					// TODO Auto-generated catch block
					printLog("조건식 오류 확인 필요.!!! :: " + conditionExpr);
				}
				
				//결과에 따라 다음 프로세스를 진행한다.
				if(result) {
					
					next = Integer.parseInt(rowData.get(row).get("TruePath"));
				} else {
					
					next = Integer.parseInt(rowData.get(row).get("FalsePath"));
				}
				
				printLog("::::::::::::::::::::::: conditionExpr :: " + conditionExpr);
				break;
				
			case "FileCreate":
				
				String filePath = rowData.get(row).get("Path");
				file = new File(filePath);
				
				//파일 생성
				if (!file.exists()) {
					
				    try {

				        file.createNewFile();			
				        
				    } catch (IOException e) {
				    	
				        e.printStackTrace();
				    }
				}
				
				try {
					
					writer = new BufferedWriter(new FileWriter(file));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				next = Integer.parseInt(rowData.get(row).get("Next"));
				break;
				
			case "FileWrite":
				
				String val = data.get(rowData.get(row).get("Value")).toString();
				
				try {
					
					if(writer != null) {
						
						writer.write(val);
						writer.newLine();
					} else {
						
						writer = new BufferedWriter(new FileWriter(file));
						writer.write(val);
						writer.newLine();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}
				
				next = Integer.parseInt(rowData.get(row).get("Next"));
				break;
				
			case "FileClose":
				
				if(writer != null) {
					
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				next = Integer.parseInt(rowData.get(row).get("Next"));
				break;
				
			case "Assign":
				
				String target = rowData.get(row).get("Target");
				String value = ReadValue(rowData.get(row).get("Value"), data);
				
				data.put(target, value);
				
				next = Integer.parseInt(rowData.get(row).get("Next"));
				break;
		}

		printLog("::::::::::::::::::::::: next :: " + next);
		//Next 값이 "0" 이면 process를 종료한다.
		if(next > 0) {
			
			process(vo, data, next);
		} else {
			
			return;
		}
	}
	
	public int query(Map<String, String> row, Map<String, Object> data) {
		
		String QueryType = row.get("QueryType");
		
		String sqlId = row.get("SqlID");
		
		String ResultArg = "DS";
		if(row.get("Result") != null) {
			
			ResultArg = row.get("Result").equals("")||row.get("Result")==null?"DS":row.get("Result");
		}
		
		int result = 0;
		try {
			
			switch(QueryType) {
			
				case "select":
					
					printLog("::::::::::::::::::::::: " + QueryType + "." + sqlId);
					List<Map<String, String>> list = sqlSession.selectList(defaultSql + sqlId, data);
					data.put(ResultArg, list);
					data.put(ResultArg + ".size", list.size());
					break;
			
				case "insert":
					
					printLog("::::::::::::::::::::::: " + QueryType + "." + sqlId);
					sqlSession.insert(defaultSql + sqlId, data);
					break;
				case "update":
					
					printLog("::::::::::::::::::::::: " + QueryType + "." + sqlId);
					sqlSession.update(defaultSql + sqlId, data);					
					break;
				case "delete":
					
					printLog("::::::::::::::::::::::: " + QueryType + "." + sqlId);
					sqlSession.delete(defaultSql + sqlId, data);
					break;
			}
			
		} catch(Exception e) {
			
			result = MessageHandler.ErrHandler(e);
			printLog("::::::::::::::::::::::: " + e.getCause().getMessage().replaceAll("\n", ""));
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
	
	//조건식을 변환하기 위한 함수
	public String setCondition(String cond, Map<String, Object> data) {
		
		// rowData = JOB.xml
		String[] arg = cond.split("`");
		
		String result = "";
		
		for(int i = 0; i < arg.length; i++) {
			
			switch(arg[i]) {
			
				case "<": //&lt;
				case "<=":
				case ">": //&gt;
				case ">=":
				case "!=":
				case "==":
					
					result += " " + arg[i]; 
					break;
					
				default: //부등호가 아님, 변수 혹은 값
					
					//숫자확인 정규식
					String regExp = "^[0-9]+$";
					
					//값이 single quote에 감싸져 있는경우 : 값
					if(arg[i].indexOf("'") == 0
					&& arg[i].lastIndexOf("'") == arg[i].length() - 1) {
						
						result += " " + arg[i];
					} else if(arg[i].matches(regExp)) { //정규식으로 값을 확인 : 숫자
						
						result += " " + arg[i];
					} else { //그 외 값 : 변수
						
						/*
						 * 다양한 분기가 추가되어야 함.
						 * dataSet의 경우 '.'(dot)으로 하위 속성을 표현한다. -> select 후에 size값을 미리 개별변수로 설정하자.(완료)
						 */
						
						//정규식의로 값을 확인하여 숫자의 경우 형번환하여 처리한다. ex) 00013 => 13
						result += data.get(arg[i]);
					}
					
					break;
			}
		}
		
		return result;
	}
	
	//단위 테스트용 함수
	public void test() {
		
		// 설정파일 관리자 선언
		IniFile ini = IniFile.getInstance();

		String value = ini.getIni("Business", "TestMSG");

		DataParser dp = new DataParser(value); //인출한 데이터를 가공처리
		
		interfaceId = dp.getInterfaceList().get(0);
		
		Map<String, Object> row = dp.getDataSet(interfaceId);
							row.put("JobResult", "0");
		
		JobVO vo = jp.getJobSchedule(interfaceId);
		
		//스케줄이 널이 아닌경우 진행
		printLog("Run Process :: " + interfaceId);
		process(vo, row, 1);		
	}
	
	//select 결과값을 틀에 맞게 재구성하여 리턴.
	public String ReadValue(String pattron, Map<String, Object> data) {
		
		//특수문자 제약이 걸려있어서 해당 특수문자를 치환해서 처리
		String[] arg = pattron.replaceAll("\\.", ",")
				              .replaceAll("\\[", "\\<")
				              .replaceAll("\\]", "\\>")
				              .split("`");
		
		String result = "";
		
		/*
		 * CASE 1 : 일반 계산식, +1, -1(완료)
		 * CASE 2 : 그 외, 조회 결과를 원하는 패턴으로 조립
		 *          switch에서 default path로 빠진 후, .(dot) 여부를 확인하여 새로운 분기를 처리
		 */
		for(int i = 0; i < arg.length; i++) {
			
			switch(arg[i]) {
			
				case "+":
				case "-":
				case "*":
				case "/":
					
					result += arg[i];
					break;
				
				default:
					
					/*
					 * 세부 속성여부를 .으로 체크.
					 * 주의) 해당 경우 '앞'과 '뒤'로 구분하여 앞데이터를 복수의 변수로 뒤데이터를 속성으로 처리해야한다.
					 */
					if(arg[i].indexOf(",") > -1) {
						
						String front = arg[i].split(",")[0];
						String back = arg[i].split(",")[1];
						
						String b = front.split("\\<")[0];
						int c = Integer.parseInt(data.get(front.split("<")[1].replaceAll(">", "")).toString());
						
						List<Map<String, String>> list = (List<Map<String, String>>) data.get(b);
						Map<String, String> row = list.get(c);
						result += row.get(back);
						
					} else if(arg[i].indexOf("'") == 0
						   && arg[i].lastIndexOf("'") == arg[i].length() - 1) {
						
						String out = arg[i].replaceAll("'", "");
						result += out;
						
					} else {
						
						//숫자확인 정규식
						String regExp = "^[0-9]+$";
						
						if(arg[i].matches(regExp)) {
							result += arg[i];
						} else {
							result += data.get(arg[i]);
						}
					}
					
					break;
			}
		}
		
		if(result.indexOf("+") > -1
		|| result.indexOf("-") > -1
		|| result.indexOf("*") > -1
		|| result.indexOf("/") > -1) {
			
			ScriptEngineManager mng = new ScriptEngineManager();
			ScriptEngine eng = mng.getEngineByName("js");
			
			try {
				result = eng.eval(result).toString();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
