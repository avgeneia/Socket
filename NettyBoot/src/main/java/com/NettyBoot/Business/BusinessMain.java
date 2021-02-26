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
	
	public BusinessMain() throws Exception {
		
		//job을 읽어오는 class 실행.
		JobTemplateParser jp = new JobTemplateParser();
		
		TelegramParser.getInstance();
		
		List<JobVO> jobList = jp.getJobTemplate();
		
		int sleep = Integer.parseInt(ini.getIni("Business", "ThreadSleep"));
		
		//interface 별 Thread 처리
		for(int i = 0; i < jobList.size(); i++) {
			
			//interface_id
			String interfaceId = jobList.get(i).getId();
			String redisKey = jobList.get(i).getRedisKey();
			
			BusinessThread bt = new BusinessThread(interfaceId, redisKey, sleep);
			bt.run();
		}
	}
}
