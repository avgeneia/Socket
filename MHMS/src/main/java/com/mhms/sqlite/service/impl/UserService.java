package com.mhms.sqlite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.sqlite.Repository.UserRepository;
import com.mhms.sqlite.models.LoginModel;
import com.mhms.sqlite.models.UserLoginModel;
import com.mhms.sqlite.service.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userDao;
	
	@Override
	public UserLoginModel getUser(LoginModel loginModel){
	    UserLoginModel model=new UserLoginModel();
	    
	    return model;
	}
	
	/*public List<UserLogin> getAllUsers() {
		List<UserLogin> persons = (List<UserLogin>) userDao.findAll();
		return persons;
	}

	public void save(UserLogin person) {
		userDao.save(person);
	}*/

}
