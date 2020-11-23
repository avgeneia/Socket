package com.mhms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mhms.sqlite.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/userList")
	public String userprofile(Model model) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "사용자 관리");
		
		model.addAttribute("infoVO", map);
		model.addAttribute("userVO", userService.getUser());
		model.addAttribute("pageInfo", "userList");
		
		return "userList";
		
	}
}
