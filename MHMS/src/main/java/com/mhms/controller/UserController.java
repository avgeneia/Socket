package com.mhms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhms.sqlite.entities.UserRole;
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
	
	@RequestMapping("/userModify")
	@ResponseBody
	public String userModify(@RequestParam(value = "uid") int uid) throws JsonProcessingException {
		
		ObjectMapper om = new ObjectMapper();
		
		List<UserRole> result = userService.getModifyUser(uid);
		System.out.println(result);
		
		String jsonStr = om.writeValueAsString(result);

		return jsonStr;
	}
	
}
