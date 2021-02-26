package com.NettyBoot.VO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobVO {
	
	String id;
	String redisKey;
	List<Map<String, String>> rowdata = new ArrayList<Map<String, String>>();
	
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
	public List<Map<String, String>> getRowdata() {
		return rowdata;
	}
	public void setRowdata(List<Map<String, String>> rowdata) {
		this.rowdata = rowdata;
	}
}
