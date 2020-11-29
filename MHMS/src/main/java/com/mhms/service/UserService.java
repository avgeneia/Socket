package com.mhms.service;

import java.util.List;

import com.mhms.dto.UserListDto;
import com.mhms.security.UserContext;


public interface UserService {

    public List<UserListDto> getUser(UserContext user);
    
    public List<UserListDto> getModifyUser(int uid);
}
