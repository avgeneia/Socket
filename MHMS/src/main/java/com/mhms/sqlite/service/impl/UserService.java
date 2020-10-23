package com.mhms.sqlite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.sqlite.Repository.UserLoginRepository;
import com.mhms.sqlite.entities.User;
import com.mhms.sqlite.models.LoginModel;
import com.mhms.sqlite.models.UserLoginModel;
import com.mhms.sqlite.service.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserLoginRepository userDao;
	
	@Override
	public UserLoginModel getUser(LoginModel loginModel){
	    UserLoginModel model=new UserLoginModel();
	    User user=userDao.findUserLoginByUserNameAndPassword(loginModel.getUserName(), loginModel.getPassword());
	    if (user !=null) {
		model.setUSER_ID(user.getUSER_ID());
		model.setPASSWORD(user.getPASSWORD());
		model.setUSER_NM(user.getUSER_NM());
		model.setAUTH_ID(user.getAUTH_ID());
		model.setADDRESS_GROUP(user.getADDRESS_GROUP());
	    }
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
