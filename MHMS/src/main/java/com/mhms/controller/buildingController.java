package com.mhms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mhms.security.UserContext;
import com.mhms.service.BuildingService;

@Controller
public class buildingController {

	@Autowired
	private BuildingService buildingService;
	
	@RequestMapping("/buildingList")
	public String buildingList(Model model, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "건축물 관리");
		
		model.addAttribute("infoVO", map);
		model.addAttribute("buildingList", buildingService.getBuildingList(user.getBid()));
		model.addAttribute("pageInfo", "buildingList");
		
		return "buildingList";
	}
}
