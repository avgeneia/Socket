package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.NoticeDto;
import com.mhms.security.UserContext;
import com.mhms.sqlite.entities.Notice;


public interface NoticeService {

	public Notice selectNotice(Map<String, String[]> map) throws SQLException;
	
    public List<NoticeDto> NoticeList(UserContext user);
    
    public int insertNotice(Map<String, String[]> map, boolean authType) throws SQLException;
    
    public long updateNotice(Map<String, String[]> map) throws SQLException;
    
    public long deleteNotice(Map<String, String[]> map) throws SQLException;
}
