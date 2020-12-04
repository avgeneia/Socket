package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.AuthDto;
import com.mhms.security.UserContext;


public interface AuthService {

    public List<AuthDto> authList(UserContext user);
    
    public int insertAuth(Map<String, String[]> map, int type) throws SQLException;
}
