package com.NettyBoot.VO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobVO {
	
	String id;
	String redisKey;
	int threadCnt;
	List<Map<String, Object>> rowdata = new ArrayList<Map<String, Object>>();
	
	public int getThreadCnt() {
		return threadCnt;
	}
	public void setThreadCnt(int threadCnt) {
		this.threadCnt = threadCnt;
	}
	public String getRedisKey() {
		return redisKey;
	}
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Map<String, Object>> getRowdata() {
		return rowdata;
	}
	public void setRowdata(List<Map<String, Object>> rowdata) {
		this.rowdata = rowdata;
	}
}
