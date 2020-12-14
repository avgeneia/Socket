package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.UserDto;
import com.mhms.dto.UserListDto;
import com.mhms.security.UserContext;


public interface UserService {
	
	public List<UserDto> getUserList(UserContext user);
	
    public List<UserListDto> userList(UserContext user);
    
    public List<UserListDto> selectUser(int uid);
    
    public int insertUser(Map<String, String[]> map) throws SQLException;
    
    public long updateUser(Map<String, String[]> map) throws SQLException;
    
    public long changePassword(Map<String, String[]> map, UserContext user) throws SQLException;
    
    public long resetUser(Map<String, String[]> map, UserContext user) throws SQLException;
    
    public long updateUserUseyn(Map<String, String[]> map) throws SQLException;
    
    public long deleteUser(Map<String, String[]> map) throws SQLException;
}
