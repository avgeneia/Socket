package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.UserDto;
import com.mhms.dto.UserListDto;
import com.mhms.security.UserContext;


public interface UserService {
	
	List<UserDto> getUserList(UserContext paramUserContext);
	  
	List<UserListDto> userList(UserContext paramUserContext);
  
	List<UserListDto> selectUser(int paramInt);
  
	int insertUser(Map<String, String[]> paramMap) throws SQLException;
  
	long updateUser(Map<String, String[]> paramMap) throws SQLException;
  
	long changePassword(Map<String, String[]> paramMap, UserContext paramUserContext) throws SQLException;
  
	long resetUser(Map<String, String[]> paramMap, UserContext paramUserContext) throws SQLException;
  
	long updateUserUseyn(Map<String, String[]> paramMap) throws SQLException;
  
	long deleteUser(Map<String, String[]> paramMap) throws SQLException;
}
