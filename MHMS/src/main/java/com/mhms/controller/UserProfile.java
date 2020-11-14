package com.mhms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserProfile {
	
	@RequestMapping("/userProfile")
	public String userprofile(Model model) {
		
		return "userprofile";
	}
}
