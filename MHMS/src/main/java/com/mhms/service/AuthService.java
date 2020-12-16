package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.AuthDto;
import com.mhms.security.UserContext;


public interface AuthService {

  AuthDto selectAuth(Map<String, String[]> map) throws SQLException;
  
  List<AuthDto> authList(UserContext user);
  
  int insertAuth(Map<String, String[]> map, UserContext user) throws SQLException;
  
  long updateAuth(Map<String, String[]> map) throws SQLException;
  
  long deleteAuth(Map<String, String[]> map) throws SQLException;
}
