package com.mhms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mhms.sqlite.models.LoginModel;
import com.mhms.sqlite.service.IUserService;

@RestController
public class HelloController {
	
	@Autowired
    private IUserService userService;
	
	@GetMapping(path="/hello")
	public String HelloSpringWorld() {
		
	    LoginModel loginModel =new LoginModel();
	    loginModel.setPassword("dbase123");
	    loginModel.setUserName("bharat0126");
	    System.out.println(userService.getUser(loginModel).toString());
		
		return "hello, spring world!!!";
	}
}
