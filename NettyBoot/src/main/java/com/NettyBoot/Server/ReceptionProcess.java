package com.NettyBoot.Server;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.NettyBoot.Business.BusinessMain;
import com.NettyBoot.Common.CmmUtil;

/*
 * 수신받은 데이터/전문을 처리하기 위한 class
 */
public class ReceptionProcess {

	/** Logger */
	static Logger logger = LogManager.getLogger(ReceptionProcess.class);
	
	Map<String, Object> globalArg = new HashMap<String, Object>();
	
	public ReceptionProcess(byte[] byteMessage) {
		
		globalArg = new HashMap<String, Object>();
		globalArg.put("SESSION.MSG", byteMessage);
		
		try {
			
			new BusinessMain("SERVER", 1, globalArg);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			CmmUtil.print("w", e.getMessage());
		}
	}
	
	public void printLog(String msg) {
		
		logger.info(msg);
	}
	
	public String getAckMsg() {
		return globalArg.get("SESSION.ACKMSG").toString();
	}
	
}
