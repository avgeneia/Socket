package com.mhms.sqlite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.sqlite.Repository.UserRepository;
import com.mhms.sqlite.entities.Account;
import com.mhms.sqlite.service.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userDao;
	
	@Override
	public List<Account> getUser(){
	    
		List<Account> resultList = (List<Account>) userDao.findAll();
	    
	    return resultList;
	}

	@Override
	public Account getUser(String username, String password) {
		// TODO Auto-generated method stub
		
		Account resultMap = userDao.findByUSERNMAndUSERPW(username, password);
		
		return resultMap;
	}
	
	/*public List<UserLogin> getAllUsers() {
		List<UserLogin> persons = (List<UserLogin>) userDao.findAll();
		return persons;
	}

	public void save(UserLogin person) {
		userDao.save(person);
	}*/

}
