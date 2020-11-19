package com.mhms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class buildingController {
	
	@RequestMapping("/buildingList")
	public String buildingList(Model model) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "건축물 관리");
		
		model.addAttribute("infoVO", map);
		model.addAttribute("pageInfo", "buildingList");
		
		return "buildingList";
	}
}
