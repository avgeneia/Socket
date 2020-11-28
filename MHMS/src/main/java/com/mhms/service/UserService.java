package com.mhms.service;

import java.util.List;

import com.mhms.dto.UserListDto;


public interface UserService {

    public List<UserListDto> getUser();
    
    public List<UserListDto> getModifyUser(int uid);
}
