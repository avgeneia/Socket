package com.NettyBoot.Business;

import com.NettyBoot.Common.IniFile;
import com.NettyBoot.Common.TelegramParser;

/*
 * Business 로직을 처리하기 위한 Main Class
 * JOB.xml을 읽어들여 정의된 로직을 처리하도록...
 * 
 */
public class BusinessMain {
	
	IniFile ini = IniFile.getInstance();
	
	BusinessThread[] bt = null;
	
	public BusinessMain() throws Exception {
		
		TelegramParser.getInstance();
		
		int sleep = Integer.parseInt(ini.getIni("Business", "ThreadSleep"));
		
		String[] redisKey = ini.getIni("Business", "Keys").split(",");

		bt = new BusinessThread[redisKey.length];
		
		//단위테스트용
		bt[0] = new BusinessThread(redisKey[0], sleep);
		bt[0].test();
		
//		for(int i = 0; i < redisKey.length; i++) {
//		
//			int threadCnt = Integer.parseInt(ini.getIni("Business", redisKey[i]));
//			
//			for(int n = 0; n < threadCnt; n++) {
//				
//				bt[n] = new BusinessThread(redisKey[i], sleep);
//				bt[n].setDaemon(true);
//				bt[n].start();
//				
//			}
//		}
	}
}
