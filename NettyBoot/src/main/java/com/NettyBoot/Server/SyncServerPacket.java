package com.NettyBoot.Server;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.NettyBoot.Common.LogManager;


/**
 * ArgoSync 서버~클라이언트간 송수신 패킷 클래스
 * 
 * @author jymoon
 *
 */
public class SyncServerPacket {
	/** 패킷 컬럼 구분자 */
	static String delimiter = "\\|";
	/** 패킷의 컬럼 내용을 담을 배열 */
	String[] columns = null;	
	
	/** Logger */
	static Logger logger = LogManager.GetConfiguredLogger(SyncServerPacket.class);
	
	/**
	 * 메세지 파싱(수신메세지 버퍼 파싱)
	 * 
	 * @param msg : 수신 메세지 버퍼
	 * @param offset : 수신 메세지 버퍼의 메세지 파싱 시작 위치
	 * @return : 파싱 결과가 담긴 패킷 클래스 인스턴스
	 */
	public static SyncServerPacket ParseMessage(byte[] msg, int offset) {
		try {
			return new SyncServerPacket(new String(msg));
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
	
	/**
	 * 메세지 파싱(문자열 파싱)
	 * 
	 * @param strMsg : 파싱할 문자열
	 * @return : 파싱 결과가 담긴 패킷 클래스 인스턴스
	 */
	public static SyncServerPacket ParseMessage(String strMsg) {
		logger.debug(strMsg);
		return new SyncServerPacket(strMsg);
	}
	
	/**
	 * 생성자(문자열로부터 패킷 클래스 인스턴스를 생성함)
	 * 
	 * @param strMsg : 메세지 문자열 
	 */
	public SyncServerPacket(String strMsg) {
		if (strMsg.endsWith("|"))
				strMsg += " ";
		columns = strMsg.split(delimiter);
	}
	
	/**
	 * 생성자(columnCnt만큼의 필드를 가진 패킷 클래스 인스턴스를 생성함)
	 * 
	 * @param columnCnt : 생성할 필드수
	 */
	public SyncServerPacket(int columnCnt) {
		columns = new String[columnCnt];
	}
	
	/**
	 * 해당 필드값 추출
	 *  
	 * @param idx : 값을 얻어오고자 하는 필드의 인덱스
	 * @return : 필드값
	 */
	public String GetColumn(int idx) {
		return columns[idx];
	}
	
	/**
	 * 해당 필드값 설정/수정
	 * 
	 * @param idx : 값을 설정하고자 하는 필드의 인덱스
	 * @param val : 설정/수정값
	 */
	public void SetColumn(int idx, String val) {
		if (idx >= 0 && idx < columns.length) 
			columns[idx] = val;
	}
	
	/**
	 * 전체 패킷 내용을 문자열로 반환
	 * 
	 * @return : 패킷 문자열
	 */
	public String GetString() {
		String strMsg = "";
		String strDeli = "";
		for (int i = 0; i < columns.length; i++) {
			strMsg += strDeli + ((columns[i] == null) ? "" : columns[i]);
			strDeli = "|";
		}
		
		return strMsg;
	}

	/**
	 * 전체 패킷 내용dml 사이즈 리턴
	 * 
	 * @return : 패킷 사이즈 문자열
	 */
	public String GetSize() {
		int size = 0;
		String strMsg = "";
		String strDeli = "";
		try{
			for (int i = 0; i < columns.length; i++) {
				strMsg += strDeli + ((columns[i] == null) ? "" : columns[i]);
				strDeli = "|";
			}
			size = strMsg.getBytes("utf-8").length + String.valueOf(strMsg.getBytes("utf-8").length).length();	
		}catch(Exception e) {
			logger.error(e);
		}
				
		return String.valueOf(size);
	}
	

	/**
	 * 전체 패킷 내용을 바이트 배열로 반환
	 * 
	 * @return : 패킷 바이트 배열
	 */
	public byte[] GetBytes() {
		try {
			return GetString().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			return null;
		}
	}
		
	/**
	 * 테스트를 위한 main
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		//System.out.println("AS|BR|CD".split("\\|")[1]);
//	}
}
