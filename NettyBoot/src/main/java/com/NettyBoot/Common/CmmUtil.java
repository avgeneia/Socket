package com.NettyBoot.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 공통함수를 선언한 클래스
 * @author seolhc
 * @since 2021.03.17
 *
 */
public class CmmUtil {
	
	/**
	 * string 값을 받아서 형변환하기 위한 함수
	 * @author seolhc
	 * @since -
	 * @param expr
	 * @return
	 */
	public String convert(String expr) {
		
		switch(expr) {
			
			case "toInt":
				toInt("");
				break;
		}
		
		return "";
	}
	
	/**
	 * 오브젝트 타입을 받아서 정수로 변환하는 함수
	 * @author seolhc
	 * @since -
	 * @param arg
	 * @return
	 */
	public int toInt(Object arg) {
		
		if(arg instanceof String) {
			
		}
		
		return 0;
	}
	
	/**
	 * 로그 출력을 위한 메소드
	 * @author seolhc
	 * @since 2021.03.17
	 * @param type
	 * @param msg
	 */
	public static void print(String type, String msg) {

		/** Logger */
		Logger logger = LogManager.getLogger(CmmUtil.class);
		
		//현재 메소드명 가져오기
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

		//현재 클래스명 가져오기(패키지명+클래스명)
		String className = Thread.currentThread().getStackTrace()[2].getFileName().replaceAll(".java", "");

		//현재 줄번호 가져오기
		int lineNo = Thread.currentThread().getStackTrace()[2].getLineNumber();
		
		msg = className + "." + methodName + ".L:" + lineNo + " " + msg;
		switch(type.toUpperCase()) {
		
			case "I": //info
				logger.info(msg);
				break;
				
			case "D": //debug
				logger.debug(msg);
				break;
				
			case "W": //Warn
				logger.warn(msg);
				break;
		}
	}
	
	/**
	 * 문자열 추출 함수
	 * @author seolhc
	 * @since 2021.03.17
	 * @param arg : 문자열
	 * @param isp : 시작 포인트
	 * @param iep : 종료 포인트
	 * @return
	 */
	public static String SubStr(String arg, int isp, int iep) {
		
		return arg.substring(isp, iep);
	}
	
	public static byte[] SubByte(byte[] arg, int isp, int iep) {
		
		int start = isp;
		byte[] rtn = new byte[iep - isp];
		
		for(int n = 0; n < iep - isp; n++) {
			rtn[n] = arg[start];
			start++;
		}
		
		return rtn;
	}
	
	/**
	 * 앞뒤 공백 제거
	 * @author seolhc
	 * @since 2021.03.17
	 * @param arg
	 * @return
	 */
	public static String trim(String arg) {
		
		return arg.trim();
	}
	
	/**
	 * 문자열 자르기
	 * #java 에서 split 처리 시 특정 특수문자를 거르지 못하는 문제가 발생.
	 * @author seolhc
	 * @since 2021.03.18
	 * @param arg
	 * @param gbn
	 * @return
	 */
	public static String[] split(String arg, String gbn) {
		
		//추후에 처리못하는 특수문자 발생시 해당 구문을 사용해서 치환처리
		switch(gbn) {
		
			case "":
				break;
				
			default:
				break;
		}
		
		return arg.split(gbn);
	}
	
	/**
	 * 왼쪽 공백제거
	 * @author seolhc
	 * @since 2021.03.18
	 * @param arg
	 * @return
	 */
	public static String ltrim(String arg) {
		
		return arg.replaceAll("^\\s+","");
	}
	
	/**
	 * 오른쪽 공백제거
	 * @author seolhc
	 * @since 2021.03.18
	 * @param arg
	 * @return
	 */
	public static String rtrim(String arg) {
		
		return arg.replaceAll("\\s+$","");
	}
	
	/**
	 * 문자바꾸기
	 * #java 에서 특정 특수문자를 거르지 못하는 문제가 발생.
	 * @author seolhc
	 * @since 2021.03.18
	 * @param arg
	 * @return
	 */
	public static String replace(String arg, String asis, String tobe) {
		
		return arg.replaceAll(asis, tobe);
	}
	
	/**
	 * 문자열 왼쪽 채우기
	 * @author seolhc
	 * @since 2021.03.18
	 * @param str
	 * @param len
	 * @param addStr
	 * @return
	 */
	public static String lpad(String str, int len, String addStr) {
		
        String result = str;
        
        int templen   = len - result.length();

        for (int i = 0; i < templen; i++){
              result = addStr + result;
        }
        
        return result;
    }
	
	/**
	 * 문자열  오른쪽 채우기
	 * @author seolhc
	 * @since 2021.03.18
	 * @param str
	 * @param len
	 * @param addStr
	 * @return
	 */
	public static String rpad(String str, int len, String addStr) {
		
        String result = str;
        
        int templen   = len - result.length();

        for (int i = 0; i < templen; i++){
              result = result + addStr;
        }
        
        return result;
    }
	
	/**
	 * 문자열 > 숫자 변환
	 * @author seolhc
	 * @since 2021.03.18
	 * @param arg
	 * @return
	 */
	public static int atoi(String arg) {
		
		int result = -999;
		
		try {
			
			result = Integer.parseInt(arg);			
		} catch(Exception e) {
			
			return result;
		}
		
		return result;
	}
	
	/**
	 * 문자열 > double 변환
	 * @author seolhc
	 * @since 2021.03.18
	 * @param arg
	 * @return
	 */
	public static double atodbl(String arg) {
		
		double result = -999;

		try {
			
			result = Double.parseDouble(arg);
		} catch(Exception e) {
			
			return result;
		}
		
		return result;
	}
	
	/**
	 * 숫자 > 문자열 변환
	 * @author seolhc
	 * @since 2021.03.18
	 * @param i
	 * @return
	 */
	public static String itoa(int i) {
		
		String str = "";
		
		try {
			
			str = String.valueOf(i);
		} catch(Exception e) {
			
			return str;
		}
		
		return str;
	}
	
	/**
	 * double > 문자열 변환
	 * @author seolhc
	 * @since 2021.03.18
	 * @param d
	 * @return
	 */
	public static String dbltoa(double d) {
		
		String str = "";
		
		try {
			
			str = String.valueOf(d);
		} catch(Exception e) {
			
			return str;
		}
		
		return str;
	}
	
	/**
	 * float > 문자열 변환(소수점 이하 버림)
	 * @author seolhc
	 * @since 2021.03.18
	 * @param f
	 * @return
	 */
	public static String ftostr(float f) {
		
		int i = Math.round(f);
		
		String str = String.valueOf(i);
		
		return str;
	}
	
	/**
	 * float형 문자열 > float 변환
	 * @author seolhc
	 * @since 2021.03.18
	 * @param str
	 * @return
	 */
	public static float atof(String str) {
		
		float f = 0l;
		
		try {
			
			f = Float.parseFloat(str);
		} catch(Exception e) {
			
			return f;
		}
		
		return f;
	}
	
	/**
	 * 현재 날짜 + 시간 구하기(YYYYMMDDHH24MISS)
	 * @author seolhc
	 * @since 2021.03.18
	 * @return
	 */
	public static String getdatetime() {

		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String datestr = format.format(Calendar.getInstance().getTime());

		return datestr;
	}
	
	/**
	 * 날짜 구하기(YYYYMMDD)
	 * @author seolhc
	 * @since 2021.03.18
	 * @param day 0:당일/음수:전일/양수:익일 
	 * @return
	 */
	public static String getdate(int day) {
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
		
		String date = "";
		
		if(day == 0) { //오늘 날짜
			
			date = dtFormat.format(Calendar.getInstance().getTime());
		} else { //양수, 음수
			
			Calendar cal = Calendar.getInstance();
			date = dtFormat.format(Calendar.getInstance().getTime());
			
			try {
				
				Date dt = dtFormat.parse(date);
				cal.setTime(dt);
				cal.add(Calendar.DATE, day);
				
				date = dtFormat.format(cal.getTime());
			} catch (ParseException e) {
				
				// TODO Auto-generated catch block
				return date;
			}

		} 
		
		return date;
	}
	
	/**
	 * 현재시간 구하기 (HH24MISS)
	 * @author seolhc
	 * @since 2021.03.18
	 * @return
	 */
	public static String gettime() {
		
		DateFormat format = new SimpleDateFormat("HHmmss");
		String datestr = format.format(Calendar.getInstance().getTime());
		
		return datestr;
	}

	/**
	 * *NOT ITEM*
	 * Hex String to Byte Array
	 * @author seolhc
	 * @since 2021.03.22
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
	      
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
									+ Character.digit(s.charAt(i+1), 16));
       	}
       	return data;
	}
	
	/**
	 * *NOT ITEM*
	 * Byte Array To Hex String 
	 * @author seolhc
	 * @since 2021.03.22
	 * @param bytes
	 * @param si
	 * @param len
	 * @return
	 */
	public static String byteArrayToHexString(byte[] bytes, int si, int len){
		
		StringBuilder sb = new StringBuilder(); 
		
		for(int i = si; i < len; i++) {
			sb.append(String.format("%02X", bytes[i]&0xff)); 
		} 
  
		return sb.toString(); 
	} 
	
	/**
	 * *NOT ITEM*
	 * Hex String To BigEndian 
	 * @author seolhc
	 * @since 2021.03.22
	 * @param a
	 * @return
	 */
	public static String hexToBigEndian(String a) {
		
		int n = a.length() / 2;
		String[] temp = new String[n];
		
		if(a.length() == 2) {
			return a;
		}
		
		int k = a.length();
		int j = a.length() - 2;
		for(int i = n; i > 0; i--) {
			temp[n-1] = a.substring(j, k);
			j = j - 2;
			k = k - 2;
			n--;
		}
		
		String result = "";
		for(int i = temp.length; i > 0; i--) {
			result += temp[i-1];
		}
		
		return result;
	}
	
	/**
	 * Hex To Big Int
	 * @param target
	 * @param poz
	 * @param size
	 * @return
	 */
	public static int hexToBigInt(byte[] target, int poz, int size) {
		
		String sHexTBI = byteArrayToHexString(target, poz, size);
		int iHexTBI = Integer.parseInt(hexToBigEndian(sHexTBI), 16);
		
		return iHexTBI;
	}
	
	/**
	 * Hex To Str
	 * @param target
	 * @param poz
	 * @param size
	 * @return
	 */
	public static String hexToStr(byte[] target, int poz, int size) {
		
		return byteArrayToHexString(target, poz, size);
	}
	
	/**
	 * Hex To Big
	 * @param target
	 * @param poz
	 * @param size
	 * @return
	 */
	public static String hexToBig(byte[] target, int poz, int size) {
		
		String result = byteArrayToHexString(target, poz, size);
		
		return hexToBigEndian(result);
	}
	
	/**
	 * Hex To Big Char
	 * @param target
	 * @param poz
	 * @param size
	 * @return
	 */
	public static char hexToBigChar(byte[] target, int poz, int size) {
		
		String sHexTBC = byteArrayToHexString(target, poz, size);
		
		return (char)Integer.parseInt(hexToBigEndian(sHexTBC),16);
	}
	
	/**
	 * Str To BCD
	 * @param target
	 * @param poz
	 * @param size
	 * @return
	 */
	public static String strToBCD(byte[] target, int poz, int size) {
		
		return byteArrayToHexString(target, poz, size);
	}
	
	/**
	 * Hex To Ascii Str
	 * @param hexStr
	 * @return
	 */
	public static String hexToAscii(byte[] target, int poz, int size) {
		
		String hexStr = byteArrayToHexString(target, poz, size);
	    StringBuilder output = new StringBuilder("");
	    
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    
	    return output.toString();
	}
}
