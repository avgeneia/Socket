package com.mhms.sqlite.service;

import java.util.List;

import com.mhms.sqlite.entities.Account;


public interface UserService {

	/*public List<UserLogin> getAllUsers();
	
	public void save(UserLogin person);*/
    
    public List<Account> getUser();
    
    public Account getUser(String username, String password);
}
