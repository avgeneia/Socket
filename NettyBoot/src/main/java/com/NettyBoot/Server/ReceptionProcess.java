package com.NettyBoot.Server;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.NettyBoot.Redis.RedisComm;

/*
 * 수신받은 데이터/전문을 처리하기 위한 class
 */
public class ReceptionProcess {

	/** Logger */
	static Logger logger = LogManager.getLogger(ReceptionProcess.class);
	
	public ReceptionProcess(String msg) {
		
		
		//1. packet.xml load
		
		
		
		RedisComm rc = new RedisComm();
		
		if(!rc.getConnect()) {

			printLog("REDIS 상태 점검 필요. 접속불가.");
			return;
		} 
		
		/*
		 * 전문이 복수일 경우를 상정하여 packet의 hearder를 읽어서 전문을 분할, 저장 하도록 구성한다.
		 */
		TelegramSpliter ts = new TelegramSpliter();
		List<Map<String, String>> list = ts.getParser(msg);
		
		loopOut: //예외 상황 발생 시 강제종료하는 시점.
		for(int i = 0; i < list.size(); i++) {
			
			String type = list.get(i).get("TYPE");
			String key = list.get(i).get("KEY");
			String arg = list.get(i).get("MSG");
			
			switch(type) {
			
				case "REDIS":
					
					if(key.equals("")) {
						printLog("REDIS KEY 누락으로 인한 강제종료.");
						break loopOut;
					}
					
					rc.set(key, arg);
					break;
			}
		}
	}
	
	public void printLog(String msg) {
		
		logger.info(msg);
	}
}
