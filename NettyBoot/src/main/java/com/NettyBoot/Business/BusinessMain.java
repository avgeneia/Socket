package com.NettyBoot.Business;

import java.util.HashMap;
import java.util.Map;

import com.NettyBoot.Common.CmmUtil;
import com.NettyBoot.VO.JobVO;

/*
 * Business 로직을 처리하기 위한 Main Class
 * JOB.xml을 읽어들여 정의된 로직을 처리하도록...
 * 
 */
public class BusinessMain {
	
	Map<String, Object> globalArg = new HashMap<String, Object>();
	
	//로그 출력용 전역변수
	String gJobNm = "";
	int gIndex = -1;
	
	public BusinessMain(String jobNm, int startIdx, Map<String, Object> arg) throws Exception {
		
		gJobNm = jobNm;
		gIndex = startIdx;
		
		CmmUtil.print("i", "Run :: " + jobNm + " // " + startIdx);
		
		JobTemplateParser jp = JobTemplateParser.getInstance();
		
		JobVO job = jp.getJobSchedule(jobNm);
		
		globalArg = arg;
		
		//int jobIndex = Integer.parseInt(job.getRowdata().get(0).get("Index"));
		
		//boot loader : MAIN job의 첫번째 프로세스를 실행, 나머지 후속 처리는 재귀처리.
		MainProcess(job, startIdx);
	}
	
	public void MainProcess(JobVO job, int index) {
		
		gIndex = index;
		
		Map<String, String> data = new HashMap<String, String>();
		for(int i = 0; i < job.getRowdata().size(); i++) {
			
			if(Integer.parseInt(job.getRowdata().get(i).get("Index")) == index) {
				data = job.getRowdata().get(i); 
				break;
			}
		}
		
		String item = data.get("Item");

		CmmUtil.print("i", index + " :: " + item + " :: " + gJobNm + " :: " + gIndex);
		
		switch(item) {
		
			case "initialize":
				globalArg = new HashMap<String, Object>();
				globalArg.put("JobResult", 0);
				break;
			
			case "Assign":
				BusinessItem.Assign(data, globalArg);
				break;
			
			case "RedisRPop":
				BusinessItem.RedisRPop(data, globalArg);
				break;
				
			case "If":
				BusinessItem.If(data, globalArg);
				break;
				
			case "GetIFID":
				BusinessItem.getIFID(data, globalArg);
				break;
			
			case "JobChange":
				BusinessItem.JobChange(data, globalArg);
				break;
			
			case "Sleep": //무한루프가 시작되므로 일단 주석
				BusinessItem.Sleep(data, globalArg);
				break;
			
			case "Query":
				BusinessItem.Query(data, globalArg);
				break;
			
			case "FileCreate":
				BusinessItem.FileCreate(data, globalArg);
				break;
			
			case "FileWrite":
				BusinessItem.FileWrite(data, globalArg);
				break;
			
			case "FileClose":
				BusinessItem.FileClose(data, globalArg);
				break;
			
			case "Commit":
				BusinessItem.Commit(globalArg);
				break;
				
			default:
				//피시 부하가.....
				CmmUtil.print("i", item + " :: 미구현 부분으로 강제종료, 피시부하가 많아서....");
				globalArg.put("JobResult", (int)0);
				return;
		}
		
		int result = (int) globalArg.get("JobResult");
		
		if(item.equals("If")) {
			
			int next = -1; 
			if(result == 0) {
				
				next = Integer.parseInt(data.get("TruePath"));
			} else {
				
				next = Integer.parseInt(data.get("FalsePath"));
			}
			
			MainProcess(job, next);
		} else {
			
			int next = Integer.parseInt(data.get("Next"));
			
			if(next > 0) {
				
				MainProcess(job, next);
			}
		}
	}
}
