package com.mhms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
	
	@RequestMapping("/authList")
	public String authList(Model model) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "권한 관리");
		
		model.addAttribute("infoVO", map);
		model.addAttribute("pageInfo", "authList");
		
		return "authList";
	}

}
