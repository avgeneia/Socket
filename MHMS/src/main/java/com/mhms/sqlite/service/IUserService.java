package com.mhms.sqlite.service;

import com.mhms.sqlite.models.LoginModel;
import com.mhms.sqlite.models.UserLoginModel;


public interface IUserService {

	/*public List<UserLogin> getAllUsers();
	
	public void save(UserLogin person);*/
    
    public UserLoginModel getUser(LoginModel loginModel);
}
