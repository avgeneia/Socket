package com.NettyBoot.Business;

import static com.NettyBoot.Common.TelegramParser.interfaceList;

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

import com.NettyBoot.Common.CmmUtil;
import com.NettyBoot.Common.TelegramParser;
import com.NettyBoot.DataBase.config.DatabaseConfiguration;
import com.NettyBoot.Handler.MessageHandler;
import com.NettyBoot.Redis.RedisComm;
import com.NettyBoot.VO.InterfaceVO;

public class BusinessItem {
	
	/**
	 * 변수를 처리하는 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param data
	 * @param arg
	 * @return
	 */
	public static void Assign(Map<String, String> data, Map<String, Object> arg) {
		
		String key = data.get("Target");
		String pattron = data.get("Value");
		String value = ReadValue(pattron, arg);
		
		int result = -1;
		
		try {
			
			if(value == ""
			|| value == null) {
				throw new Exception();
			}
			
			arg.put(key, value);
			result = 0;
		} catch(Exception e) {
			
			result = -1;
		}
		
		setResult(arg, result);
	}
	
	/**
	 * Redis에 값을 꺼내기 위한 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param data
	 * @param arg
	 * @return int : 음수이면 Error, 양수이면 정상처리
	 */
	public static void RedisRPop(Map<String, String> data, Map<String, Object> arg) {
		
		//Redis 객체
		RedisComm rc = new RedisComm();
		int result = -1;
		
		if(rc.getConnect() == false) {
			CmmUtil.print("w", "Redis 연결 안됨.");
			result = -1;
		}
		
		String redisKey = ReadValue(data.get("Key"), arg);
		
		String value = "";
		String key = data.get("Target");
		
		//Redis에 데이터가 있는지 검사
		if(rc.getlen(redisKey) == 0) {
			CmmUtil.print("w", "처리할 데이터가 없습니다. :: " + redisKey);
			result = -1;
		} else {
			
			try {
				value = rc.rpop(redisKey);
				arg.put(key, value);
				result = 0;
			} catch(Exception e) {
				
				result = -1;
			}
		}
		
		setResult(arg, result);
	}
	
	/**
	 * 조건식을 처리하는 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param data
	 * @param arg
	 * @return int : 음수이면 Error, 양수이면 정상처리
	 */
	public static void If(Map<String, String> data, Map<String, Object> arg) {
		
		//xml에서 읽어온 조건부 String 데이터를 변환하여 진행하기 위한 JS 객체 선언
		ScriptEngineManager mng = new ScriptEngineManager();
		ScriptEngine eng = mng.getEngineByName("js");
		
		// rowData = JOB.xml
		String conditionExpr = data.get("Condition").toString();
		
		conditionExpr = setCondition(conditionExpr, arg);
		
		boolean result = false;
		try {
			 
			result = (boolean) eng.eval(conditionExpr);
		} catch (ScriptException e) {
			
			// TODO Auto-generated catch block
			CmmUtil.print("w", "조건식 오류 확인 필요.!!! :: " + conditionExpr);
		}
		
		setResult(arg, result?0:-1);
	}
	
	/**
	 * 쿼리를 실행하는 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param row
	 * @param data
	 */
	public static void Query(Map<String, String> row, Map<String, Object> data) {

		//쿼리 처리를 위한 기본 pkg
		String defaultSql = "NettyBoot" + ".";
		
		//쿼리 실행을 위한 sql객체
		//SqlSession sqlSession = DatabaseConfiguration.getSqlSessionFactory().openSession(false);
		SqlSession sqlSession = (SqlSession) data.get("SqlSession");
		
		if(sqlSession == null) {
			sqlSession = DatabaseConfiguration.getSqlSessionFactory().openSession(false);
		}
		
		String QueryType = row.get("QueryType");
		
		String sqlId = row.get("SqlID");
		
		String ResultArg = "DS";
		if(row.get("Result") != null) {
			
			ResultArg = row.get("Result").equals("")||row.get("Result")==null?"DS":row.get("Result");
		}
		
		Map<String, Object> map = data;
		//쿼리에 사용할 데이터를 준비.
		if(row.get("Data") != null) {
			String frontStr = row.get("Data").replaceAll("\\[", "\\<").replaceAll("\\]", "\\>").split("\\<")[0];
			String backStr = row.get("Data").replaceAll("\\[", "\\<").replaceAll("\\]", "\\>").split("\\<")[1].replaceAll("\\>", "");
			
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(frontStr);
			map = list.get(Integer.parseInt(data.get(backStr).toString()));
		}
		
		int result = 0;
		try {
			
			switch(QueryType) {
			
				case "select":
					
					CmmUtil.print("i", "query :: " + QueryType + "." + sqlId);
					List<Map<String, String>> list = sqlSession.selectList(defaultSql + sqlId, map);
					data.put(ResultArg, list);
					data.put(ResultArg + ".size", list.size());
					break;
			
				case "insert":
					
					CmmUtil.print("i", "query :: " + QueryType + "." + sqlId);
					sqlSession.insert(defaultSql + sqlId, map);
					break;
				case "update":
					
					CmmUtil.print("i", "query :: " + QueryType + "." + sqlId);
					sqlSession.update(defaultSql + sqlId, map);			
					break;
				case "delete":
					
					CmmUtil.print("i", "query :: " + QueryType + "." + sqlId);
					sqlSession.delete(defaultSql + sqlId, map);
					break;
			}
			
		} catch(Exception e) {
			
			result = MessageHandler.ErrHandler(e);
			CmmUtil.print("w", "query :: " + e.getCause().getMessage().replaceAll("\n", ""));
		}

		data.put("SqlSession", sqlSession);
		setResult(data, result);
	}
	
	/**
	 * 인터페이스 ID를 변환하는 메소드
	 * @author seolhc
	 * @since 2021.03.17
	 * @param row
	 * @param data
	 */
	public static void getIFID(Map<String, String> data, Map<String, Object> arg) {
		
		//PACKET.xml 파싱결과를 가져옴
		TelegramParser.getInstance();
		
		//인터페이스 목록에 대한 정보를 가져옴
		List<InterfaceVO> list = interfaceList;
		
		String jobid = ReadValue(data.get("Value"), arg);
		
		for(int i = 0; i < list.size(); i++) {
			
			if(list.get(i).getCode().equals(jobid)) {
				jobid = list.get(i).getId();
			}
		}
		
		String key = data.get("Target");
		
		arg.put(key, jobid);
	}
	
	/**
	 * Sub Job을 실행하기 위한 메소드
	 * @author seolhc
	 * @since 2021.03.17
	 * @param data
	 * @param arg
	 */
	public static void JobChange(Map<String, String> data, Map<String, Object> arg) {
		
		String jobNm = ReadValue(data.get("Target"), arg);
				
		try {
			
			new BusinessMain(jobNm, 1, arg);
		} catch (Exception e) {
			
			CmmUtil.print("w", e.getMessage());
			// TODO Auto-generated catch block
			setResult(arg, -1);
			return;
		}
		setResult(arg, 0);
	}
	
	/**
	 * 지연 메소드
	 * @author seolhc
	 * @since 2021.03.17
	 * @param data
	 * @param arg
	 */
	public static void Sleep(Map<String, String> data, Map<String, Object> arg) {
		
		int time = Integer.parseInt(ReadValue(data.get("Time"), arg));
		String type = data.get("Type");
		
		switch(type) {
			case "ss":
				time = time * 1000;
				break;
		}
		
		try {
			
			Thread.sleep(time);
		} catch (InterruptedException e) {
			
			// TODO Auto-generated catch block
			CmmUtil.print("w", e.getMessage());
			setResult(arg, -1);
			return;
		}
		setResult(arg, 0);
	}
	
	/**
	 * 파일을 생성하는 메소드
	 * @param pattron
	 * @param data
	 */
	public static void FileCreate(Map<String, String> data, Map<String, Object> arg) {
		
		String filePath = data.get("Path");
		File file = new File(filePath);
		BufferedWriter writer = null;
		
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
		
		String key = data.get("Alias");
		
		arg.put(key, writer);
		
		setResult(arg, 0);
	}
	
	/**
	 * 지정된 파일에 데이터를 쓰는 메소드
	 * @param data
	 * @param arg
	 */
	public static void FileWrite(Map<String, String> data, Map<String, Object> arg) {
		
		String key = data.get("Alias");
		
		BufferedWriter writer = (BufferedWriter) arg.get(key);
		
		String val = ReadValue(data.get("Value"), arg);
		
		if(writer != null) {
			
			try {
				
				writer.write(val);
				writer.newLine();
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				CmmUtil.print("w", e.getMessage());
				setResult(arg, -1);
				return;
			}
		} else {
			
			CmmUtil.print("w", "writer 객체 NPE 발생.");
			setResult(arg, -1);
			return;
		}
		
		setResult(arg, 0);
	}
	
	/**
	 * 파일에 쓰기를 종료하는 메소드
	 * @param data
	 * @param arg
	 */
	public static void FileClose(Map<String, String> data, Map<String, Object> arg) {
		
		String key = data.get("Alias");
		
		BufferedWriter writer = (BufferedWriter) arg.get(key);
		
		if(writer != null) {
			
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				CmmUtil.print("w", e.getMessage());
				setResult(arg, -1);
				return;
			}
		} else {
			
			CmmUtil.print("w", "writer 객체 NPE 발생.");
			setResult(arg, -1);
			return;
		}

		setResult(arg, 0);
	}
	
	/**
	 * DB Commit 메소드
	 */
	public static void Commit(Map<String, Object> arg) {
		
		SqlSession sqlSession = (SqlSession) arg.get("SqlSession");
		
		if(sqlSession == null) {
			
			CmmUtil.print("w", "sqlSession 객체 NPE 발생.");
			setResult(arg, -1);
			return;
		}
		
		sqlSession.commit();
		sqlSession.close();
		setResult(arg, 0);
	}
	
	/**
	 * *NOT ITEM*
	 * 수식을 읽기위한 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param pattron
	 * @param data
	 * @return
	 */
	private static String ReadValue(String pattron, Map<String, Object> data) {
		
		//특수문자 제약이 걸려있어서 해당 특수문자를 치환해서 처리
		String[] arg = pattron.replaceAll("\\.", ",")
				              .replaceAll("\\[", "\\<")
				              .replaceAll("\\]", "\\>")
				              .split("`");
		
		String result = "";
		
		/*
		 * 예외사항을 추가.
		 * 내부함수사용에 대한.
		 */
		if(pattron.indexOf("(") > -1 
		&& pattron.lastIndexOf(")") == pattron.length() - 1) {
			
			return CallFuntion(pattron, data);
		}
		
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
						
						@SuppressWarnings("unchecked")
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
	
	/**
	 * *NOT ITEM*
	 * 조건식에 대한 내부처리 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param cond
	 * @param data
	 * @return
	 */
	private static String setCondition(String cond, Map<String, Object> data) {
		
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
	
	/**
	 * *NOT ITEM* 
	 * 각 ITEM의 결과를 처리하는 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param arg
	 * @param result
	 */
	private static void setResult(Map<String, Object> arg, int result) {
		
		arg.put("JobResult", result);
	}
	
	/**
	 * *NOT ITEM* 
	 * 내부함수를 호출하기 위한 메소드
	 * @author seolhc
	 * @since 2021.03.17 : 현재는 단일 함수에 대한 처리만 가능
	 * @param pattron
	 * @param data
	 * @return
	 */
	private static String CallFuntion(String pattron, Map<String, Object> data) {
		
		//이유는 모르겠으나 특정 특수문자에 대한 split 처리가 안되어 1차로 변환하여 처리해야함.
		pattron = pattron.replaceAll("\\(", "\\<")
	                     .replaceAll("\\)", "\\>");
		
		String result = ""; //반환변수
		
		/*
		 * step 1 : 함수와 입력값을 구분
		 * step 2 : 입력값에 변수가 사용되었는지 확인 및 처리
		 * step 3 : 함수 호출 및 반환처리
		 */
		//step1
		String function = pattron.split("<")[0]; //함수 분리
		String[] args = pattron.split("<")[1].replace(">", "").split(","); //입력값 1차 가공
		Object[] arg = new Object[args.length]; //입력값의 변수처리를 위한 선언
		
		//step2
		for(int i = 0; i < args.length; i++) {
			
			//숫자확인 정규식
			String regExp = "^[0-9]+$";
			
			//변수가 아닌 일반 string
			if(args[i].indexOf("'") == 0
			&& args[i].lastIndexOf("'") == args[i].length() - 1) {
				
				arg[i] = args[i].replaceAll("'", ""); 
			} else if(args[i].matches(regExp)) { //숫자
				
				arg[i] = Integer.parseInt(args[i]);
			} else { //변수
				
				arg[i] = data.get(args[i]);
			}
		}
		
		//step3
		try {
			
			switch(function.toLowerCase()) {
			
				case "substr":
					result = CmmUtil.SubStr((String)arg[0], (int)arg[1], (int)arg[2]);
					break;
			}
		} catch(Exception e) {
			CmmUtil.print("w", "함수 처리 오류 발생.");
			return null;
		}
		
		return result;
	}
}
