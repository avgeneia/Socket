package com.NettyBoot.Business;

import java.util.List;

import com.NettyBoot.Common.IniFile;
import com.NettyBoot.VO.JobVO;

/*
 * Business 로직을 처리하기 위한 Main Class
 * JOB.xml을 읽어들여 정의된 로직을 처리하도록...
 * 
 */
public class BusinessMain {
	
	IniFile ini = IniFile.getInstance();
	
	BusinessThread[] bt = null;
	
	public BusinessMain() throws Exception {
		
		//job을 읽어오는 class 실행.
		JobTemplateParser jp = null;
		try {
			jp = JobTemplateParser.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TelegramParser.getInstance();
		
		bt = new BusinessThread[jp.getThreadCnt()];
		
		int sleep = Integer.parseInt(ini.getIni("Business", "ThreadSleep"));

		String[] redisKey = ini.getIni("Business", "Keys").split(",");
		
		for(int i = 0; i < redisKey.length; i++) {
		
			int threadCnt = Integer.parseInt(ini.getIni("Business", redisKey[i]));
			
			for(int n = 0; n < threadCnt; n++) {
				
				bt[n] = new BusinessThread(redisKey[i], sleep);
				bt[n].setDaemon(true);
				bt[n].start();
			}
		}
	}
}
