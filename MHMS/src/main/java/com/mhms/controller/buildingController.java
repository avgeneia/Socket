package com.mhms.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
		model.addAttribute("initbuildVO", buildingService.initBuild(user.getBid()));
		model.addAttribute("pageInfo", "buildingList");
		
		return "buildingList";
	}
	
	@RequestMapping("/buildModify")
	@ResponseBody
	public String buildModify(Model model, @AuthenticationPrincipal UserContext user) {
				
		return "buildingList";
	}
	
	@RequestMapping("/addbuild")
	@ResponseBody
	public Map<String, String> addBuild(HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			buildingService.insertBuild(request.getParameterMap());
			
			map.put("CODE", "0");
			map.put("MSG", "완료되었습니다.");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException :: " + e.getErrorCode());
			
			if(e.getErrorCode() == 19) {
				map.put("CODE", "-1");
				map.put("MSG", "중복된 데이터가 존재합니다.");
			}
		}
		
		return map;
	}
}
