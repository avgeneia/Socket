package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.AuthDto;
import com.mhms.security.UserContext;


public interface AuthService {

	public AuthDto selectAuth(Map<String, String[]> map) throws SQLException;
	
    public List<AuthDto> authList(UserContext user);
    
    public int insertAuth(Map<String, String[]> map, UserContext user) throws SQLException;
    
    public long updateAuth(Map<String, String[]> map) throws SQLException;
    
    public long deleteAuth(Map<String, String[]> map) throws SQLException;
}
