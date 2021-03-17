package com.NettyBoot.Common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.NettyBoot.Business.BusinessThread;

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
		Logger logger = LogManager.getLogger(BusinessThread.class);
		
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
	 * @param arg : 문자열
	 * @param isp : 시작 포인트
	 * @param iep : 종료 포인트
	 * @return
	 */
	public static String SubStr(String arg, int isp, int iep) {
		
		return arg.substring(isp, iep);
	}
}
