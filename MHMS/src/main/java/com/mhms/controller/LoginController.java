package com.mhms.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mhms.security.UserContext;

@Controller
public class LoginController {
	
	@RequestMapping("/login")
	public String login(Model model) {
		// View attribute
		return "login";
	}
	
}