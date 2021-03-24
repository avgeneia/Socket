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
import com.NettyBoot.VO.DataSetVO;
import com.NettyBoot.VO.InterfaceVO;
import com.NettyBoot.VO.RowVO;

public class BusinessItem {
	
	/**
	 * 변수를 처리하는 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param data
	 * @param arg
	 * @return
	 */
	public static void Assign(Map<String, Object> data, Map<String, Object> arg) {
		
		String key = (String) data.get("Target");
		String pattron = (String) data.get("Value");
		
		/*
		 * 예외사항을 추가.
		 * 내부함수사용에 대한.
		 */
		if(pattron.indexOf("(") > -1 
		&& pattron.lastIndexOf(")") == pattron.length() - 1) {
			
			Object obj = CallFuntion(pattron, arg);
			arg.put(key, obj);
			setResult(arg, 0);
			return;
		}
		
		Object value = ReadValue(pattron, arg);
		
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
	public static void RedisRPop(Map<String, Object> data, Map<String, Object> arg) {
		
		//Redis 객체
		RedisComm rc = RedisComm.getInstance();
		int result = -1;
		
		if(rc.getConnect() == false) {
			
			CmmUtil.print("w", "Redis 연결 안됨. 재시도....");
			rc = new RedisComm();
		}
		
		if(rc.getConnect() == false) {
			
			CmmUtil.print("w", "Redis 연결 안됨. 종료");
			result = -1;
			setResult(arg, result);
			return;
		}
		
		String redisKey = (String) ReadValue((String) data.get("Key"), arg);
		
		String value = "";
		String key = (String) data.get("Target");
		
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
	 * Redis에 값을 저장하기 위한 메소드
	 * @param data
	 * @param arg
	 */
	public static void RedisLPush(Map<String, Object> data, Map<String, Object> arg) {
		
		//Redis 객체
		RedisComm rc = RedisComm.getInstance();
		int result = -1;
		
		if(rc.getConnect() == false) {
			
			CmmUtil.print("w", "Redis 연결 안됨. 재시도....");
			rc = new RedisComm();
		}
		
		if(rc.getConnect() == false) {
			
			CmmUtil.print("w", "Redis 연결 안됨. 종료");
			result = -1;
			setResult(arg, result);
			return;
		}
		
		String redisKey = (String) ReadValue((String) data.get("Key"), arg);
		String value = (String) ReadValue((String) data.get("Value"), arg);
		rc.set(redisKey, value);
		setResult(arg, 0);
	}
	
	/**
	 * Redis에서 데이터 개수를 가져오는 메소드
	 * @param data
	 * @param arg
	 */
	public static void RedisLLen(Map<String, Object> data, Map<String, Object> arg) {
		
		//Redis 객체
		RedisComm rc = RedisComm.getInstance();
		int result = -1;
		
		if(rc.getConnect() == false) {
			
			CmmUtil.print("w", "Redis 연결 안됨. 재시도....");
			rc = new RedisComm();
		}
		
		if(rc.getConnect() == false) {
			
			CmmUtil.print("w", "Redis 연결 안됨. 종료");
			result = -1;
			setResult(arg, result);
			return;
		}
		
		String redisKey = (String) ReadValue((String) data.get("Key"), arg);
		String key = (String) data.get("Target");
		int len = (int) rc.getlen(redisKey);
		
		arg.put(key, len);
		
		setResult(arg, 0);
	}
	
	/**
	 * 조건식을 처리하는 메소드
	 * @author seolhc
	 * @since 2021.03.16
	 * @param data
	 * @param arg
	 * @return int : 음수이면 Error, 양수이면 정상처리
	 */
	public static void If(Map<String, Object> data, Map<String, Object> arg) {
		
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
	public static void Query(Map<String, Object> row, Map<String, Object> data) {

		//쿼리 처리를 위한 기본 pkg
		String defaultSql = "NettyBoot" + ".";
		
		//쿼리 실행을 위한 sql객체
		//SqlSession sqlSession = DatabaseConfiguration.getSqlSessionFactory().openSession(false);
		SqlSession sqlSession = (SqlSession) data.get("SqlSession");
		
		if(sqlSession == null) {
			sqlSession = DatabaseConfiguration.getSqlSessionFactory().openSession(false);
		}
		
		String QueryType = (String) row.get("QueryType");
		
		String sqlId = (String) row.get("SqlID");
		
		String ResultArg = "DS";
		if(row.get("Result") != null) {
			
			ResultArg = row.get("Result").equals("")||row.get("Result")==null?"DS":(String) row.get("Result");
		}
		
		Map<String, Object> map = data;
		//쿼리에 사용할 데이터를 준비.
		if(row.get("Data") != null) {
			String frontStr = ((String) row.get("Data")).replaceAll("\\[", "\\<").replaceAll("\\]", "\\>").split("\\<")[0];
			String backStr = ((String) row.get("Data")).replaceAll("\\[", "\\<").replaceAll("\\]", "\\>").split("\\<")[1].replaceAll("\\>", "");
			
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
	public static void getIFID(Map<String, Object> data, Map<String, Object> arg) {
		
		//PACKET.xml 파싱결과를 가져옴
		TelegramParser.getInstance();
		
		//인터페이스 목록에 대한 정보를 가져옴
		List<InterfaceVO> list = interfaceList;
		
		String jobid = (String) ReadValue((String) data.get("Value"), arg);
		
		for(int i = 0; i < list.size(); i++) {
			
			if(list.get(i).getCode().equals(jobid)) {
				jobid = list.get(i).getId();
			}
		}
		
		String key = (String) data.get("Target");
		
		arg.put(key, jobid);
	}
	
	public static void getKey(Map<String, Object> data, Map<String, Object> arg) {
		
		//PACKET.xml 파싱결과를 가져옴
		TelegramParser.getInstance();
		
		//인터페이스 목록에 대한 정보를 가져옴
		List<InterfaceVO> list = interfaceList;
		
		String jobid = (String) ReadValue((String) data.get("Value"), arg);
		
		for(int i = 0; i < list.size(); i++) {
			
			if(list.get(i).getCode().equals(jobid)) {
				jobid = list.get(i).getKey();
			}
		}
		
		String key = data.get("Target").toString();
		
		arg.put(key, jobid);
		setResult(arg, 0);
	}
	
	/**
	 * Sub Job을 실행하기 위한 메소드
	 * @author seolhc
	 * @since 2021.03.17
	 * @param data
	 * @param arg
	 */
	public static void JobChange(Map<String, Object> data, Map<String, Object> arg) {
		
		String jobNm = (String) ReadValue((String) data.get("Target"), arg);
				
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
	public static void Sleep(Map<String, Object> data, Map<String, Object> arg) {
		
		int time = Integer.parseInt((String) ReadValue((String) data.get("Time"), arg));
		String type = (String) data.get("Type");
		
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
	public static void FileCreate(Map<String, Object> data, Map<String, Object> arg) {
		
		String filePath = (String) data.get("Path");
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
		
		String key = (String) data.get("Alias");
		
		arg.put(key, writer);
		
		setResult(arg, 0);
	}
	
	/**
	 * 지정된 파일에 데이터를 쓰는 메소드
	 * @param data
	 * @param arg
	 */
	public static void FileWrite(Map<String, Object> data, Map<String, Object> arg) {
		
		String key = (String) data.get("Alias");
		
		BufferedWriter writer = (BufferedWriter) arg.get(key);
		
		String val = (String) ReadValue((String) data.get("Value"), arg);
		
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
	public static void FileClose(Map<String, Object> data, Map<String, Object> arg) {
		
		String key = (String) data.get("Alias");
		
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
	 * 문자열을 패킷 구조체로 생성
	 * @author seolhc
	 * @since 2021.03.22
	 * @param data
	 * @param arg
	 */
	public static void StrToPkt(Map<String, Object> data, Map<String, Object> arg) {
		
		//PACKET.xml 파싱결과를 가져옴
		TelegramParser.getInstance();
		
		//인터페이스 목록에 대한 정보를 가져옴
		List<DataSetVO> dsList = TelegramParser.dataSetList;

		String key = (String) data.get("DataSet");
		
		byte[] target = null;
		String str = "";
		
		for(int i = 0; i < dsList.size(); i++) {
		//SMS_HEADER, SOX017A
			
			if(dsList.get(i).getId().equals(key) == false) {
				continue;
			}
			
			String dsType = dsList.get(i).getType();
			List<RowVO> row = dsList.get(i).getRow();
			
			if(dsType.equals("Byte")) {
				
				target = (byte[]) ReadValue((String) data.get("Value"), arg);
				packetToByte(row, key, target, arg);
			} else {
				
				str = ReadValue((String) data.get("Value"), arg).toString();
				packetToStr(row, key, str, arg);
			}
		}		
		
		setResult(arg, 0);
	}
	
	/**
	 * 패킷구조체를 문자열로 생성
	 * @author seolhc
	 * @since 2021.03.16
	 * @param data
	 * @param globalArg
	 */
	public static void PktToStr(Map<String, Object> data, Map<String, Object> globalArg) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @param globalArg
	 */
	public static void Format(Map<String, Object> data, Map<String, Object> globalArg) {
		// TODO Auto-generated method stub
		String format = (String) data.get("Format");
		String[] valueArr = data.get("ValueArray").toString().split("\\|");
		Object[] valObj = new Object[valueArr.length];
		String Key = data.get("Target").toString();
		
		for(int i = 0; i < valueArr.length; i++) {
			valObj[i] = ReadValue(valueArr[i], globalArg);
		}
		
		globalArg.put(Key, String.format(format, valObj));
		setResult(globalArg, 0);
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
	private static Object ReadValue(String pattron, Map<String, Object> data) {
		
		/*
		 * 예외사항을 추가.
		 * 내부함수사용에 대한.
		 */
		if(pattron.indexOf("(") > -1 
		&& pattron.lastIndexOf(")") == pattron.length() - 1) {
			
			Object obj = CallFuntion(pattron, data);
			
			return obj;
		}
		
		//특수문자 제약이 걸려있어서 해당 특수문자를 치환해서 처리
		String[] arg = pattron//.replaceAll("\\.", ",")
				              .replaceAll("\\[", "\\<")
				              .replaceAll("\\]", "\\>")
				              .split("`");
		
		Object result = "";
				
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
						
						//list구조의 변수인지 확인.
						if(front.indexOf("<") > -1) {
							
							String b = front.split("\\<")[0];
							int c = Integer.parseInt(data.get(front.split("<")[1].replaceAll(">", "")).toString());
							
							@SuppressWarnings("unchecked")
							List<Map<String, String>> list = (List<Map<String, String>>) data.get(b);
							Map<String, String> row = list.get(c);
							result += row.get(back);
						} else {
							
							result = (String) data.get(front + "." + back);
						}
						
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
							result = data.get(arg[i]);
						}
					}
					
					break;
			}
		}
		
//		if(result.indexOf("+") > -1
//		|| result.indexOf("-") > -1
//		|| result.indexOf("*") > -1
//		|| result.indexOf("/") > -1) {
//			
//			ScriptEngineManager mng = new ScriptEngineManager();
//			ScriptEngine eng = mng.getEngineByName("js");
//			
//			try {
//				result = eng.eval(result).toString();
//			} catch (ScriptException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
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
						if(data.get(arg[i]) instanceof Integer) {
							
							result += data.get(arg[i]);
						} else {
							
							result += "\'" + data.get(arg[i]) + "\'";
						}						
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
	 * Byte Array(hex) To Int
	 * @author seolhc
	 * @since 2021.03.22
	 * @param hexbyte
	 * @return
	 */
	private static int toInt(byte[] hexbyte) {
		
		/* hexbyte에 16진수가 들어가 있음 */

		StringBuffer sb = new StringBuffer(hexbyte.length * 2);
		String hexaDecimal;

		/* Hex byte[] to  Hex String */
		for(int x = 0; x < hexbyte.length; x++)
		{
			hexaDecimal = "0" + Integer.toHexString(0xff & hexbyte[x]);
			sb.append( hexaDecimal.substring(hexaDecimal.length()-2));
		}

		/* Hex String  to   Decimal int */
		int decimal = Integer.parseInt(sb.toString(),16);
		
		return decimal;
	}
	
	/**
	 * packet 구조체에 문자열 데이터를 분할해서 체운다.
	 * @param row
	 * @param key
	 * @param target
	 * @param arg
	 */
	private static void packetToStr(List<RowVO> row, String key, String Str, Map<String, Object> arg) {
		
		for(int i = 0; i < row.size(); i++) {
			
			RowVO rowData = row.get(i);
			int poz = rowData.getPoz();
			int size = rowData.getSize();
			String id = key + "." + rowData.getId();
			
			String value = Str.substring(poz, poz + size);
			CmmUtil.print("d", " ID :: " + id + " // VALUE :: " + value);
			arg.put(id, value);
		}
	}
	
	/**
	 * *NOT ITEM*
	 * packet 구조체를 byte데이터를 가지고 구성한다.
	 * @param row
	 * @param key
	 * @param target
	 * @param arg
	 */
	private static void packetToByte(List<RowVO> row, String key, byte[] target, Map<String, Object> arg) {
		
		for(int n = 0; n < row.size(); n++) {
			
			RowVO rowData = row.get(n);
			int poz = rowData.getPoz();
			int size = rowData.getSize();
			String id = key + "." + rowData.getId();
			
			if(target.length == poz) {
				break;
			}
			
			RowVO result = new RowVO();
			
			switch(rowData.getExpr()) {
				
				case "HexToBigInt":
					
					int iHexTBI = CmmUtil.hexToBigInt(target, poz, poz + size);
					
					result.setId(id);
					result.setValue(iHexTBI);
					break;
				
				case "HexToStr":
					
					String sHexTS = CmmUtil.hexToStr(target, poz, poz + size);
					
					result.setId(id);
					result.setValue(sHexTS);
					break;
				
				case "HexToBig":
					
					String sHexToB = CmmUtil.hexToBig(target, poz, poz + size);
					       
					result.setId(id);
					result.setValue(sHexToB);
					break;
				
				case "HexToBigChar":
					
					char cHexTBC = CmmUtil.hexToBigChar(target, poz, poz + size);

					result.setId(id);
					result.setValue(cHexTBC);
					break;
				
				case "StrToBCD":
					String sStrToBCD = CmmUtil.strToBCD(target, poz, poz + size);
					
					result.setId(id);
					result.setValue(sStrToBCD);
					break;
				
				case "HexToAscii":
					
					String sHexToAscii = CmmUtil.hexToAscii(target, poz, poz + size);
					
					result.setId(id);
					result.setValue(sHexToAscii);
					break;
					
				default:
					break;
			}
			
			CmmUtil.print("d", " ID :: " + result.getId() + " // VALUE :: " + result.getValue());
			arg.put(result.getId(), result.getValue());
		}
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
	private static Object CallFuntion(String pattron, Map<String, Object> gArg) {
		
		//이유는 모르겠으나 특정 특수문자에 대한 split 처리가 안되어 1차로 변환하여 처리해야함.
		pattron = pattron.replaceAll("\\(", "\\<")
	                     .replaceAll("\\)", "\\>");
		
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
				
				arg[i] = gArg.get(args[i]);
			}
		}
		
		//step3
		try {
			
			switch(function.toLowerCase()) {
			
				case "substr":
					return CmmUtil.SubStr((String)arg[0], (int)arg[1], (int)arg[2]);

				case "subbyte":
					return CmmUtil.SubByte((byte[])arg[0], (int)arg[1], (int)arg[2]);
					
				case "trim":
					return CmmUtil.trim((String) arg[0]);
				
				case "split":
					return CmmUtil.split((String)arg[0], (String)arg[1]);
				
				case "ltrim":
					return CmmUtil.ltrim((String)arg[0]);
				
				case "rtrim":
					return CmmUtil.rtrim((String)arg[0]);
				
				case "replace":
					return CmmUtil.replace((String)arg[0], (String)arg[1], (String)arg[2]);
				
				case "lpad":
					return CmmUtil.lpad((String)arg[0], (int)arg[1], (String)arg[2]);
				
				case "rpad":
					return CmmUtil.rpad((String)arg[0], (int)arg[1], (String)arg[2]);
				
				case "atoi":
					int i = CmmUtil.atoi((String)arg[0]);
					
					if(i == -999) {
						throw new Exception();
					}
					
					return i;
				
				case "atodbl":
					double dbl = CmmUtil.atodbl((String)arg[0]);
					
					if(dbl == -999) {
						throw new Exception();
					}
					
					return dbl;
				
				case "itoa":
					String str = CmmUtil.itoa((int)arg[0]);
					
					if(str == "") {
						throw new Exception();
					}
					
					return str;
				
				case "dbltoa":
					String dbltos = CmmUtil.dbltoa((double)arg[0]);
					
					if(dbltos == "") {
						throw new Exception();
					}
					
					return dbltos;
					
				case "ftostr":
					return CmmUtil.ftostr((float)arg[0]);
					
				case "atof":
					float atof = CmmUtil.atof((String)arg[0]);
					
					if(atof == 0l) {
						throw new Exception();
					}
					
					return atof;
				
				case "getdatetime":
					return CmmUtil.getdatetime();
				
				case "getdate":
					String sDate = CmmUtil.getdate((int)arg[0]);
					
					if(sDate == "") {
						throw new Exception();
					}
					
					return sDate;
					
				case "bcdtochar":
					return "";
				
				case "gettime":
					return CmmUtil.gettime();
				
				case "hextostr":
					return CmmUtil.hexToStr((byte[])arg[0], 0, new String((byte[])arg[0]).length());
					
				default:
					throw new Exception();
			}
			
		} catch(Exception e) {
			CmmUtil.print("w", "함수 처리 오류 발생.");
			return null;
		}
	}

	
}
