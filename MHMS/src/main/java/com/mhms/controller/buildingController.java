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
import com.mhms.sqlite.entities.Building;
import com.mhms.util.CommUtil;

@Controller
public class buildingController {

	@Autowired
	private BuildingService buildingService;
	
	/*
	 * 조회 1건
	 */
	@RequestMapping("/selectBuild")
	@ResponseBody
	public Map<String, Object> selectBuild(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
		    Building dto = buildingService.selectBuild(request.getParameterMap());
		    map.put("CODE", "0");
			map.put("DATA", dto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("CODE", e.getErrorCode());
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 조회 목록(MAIN)
	 */
	@RequestMapping("/buildingList")
	public String buildingList(Model model, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "건축물 관리");
		
		
		//관리자 권한을 확인해서 전체를 조회하게
		boolean auth = CommUtil.getAuth(user);
		
		//관리자 권한일 때 건물관리만 가능 호수는 매니저에서 관리
		if(auth) {
			Building dto = new Building();
			dto.setBid(0);
			dto.setBnm("신규");
			model.addAttribute("initbuildVO", dto);
		} else {
			model.addAttribute("initbuildVO", buildingService.initBuild(user));
		}
		
		//model.addAttribute("infoVO", map);
		model.addAttribute("buildingList", buildingService.buildingList(user));
		model.addAttribute("pageInfo", "buildingList");
		
		return "buildingList";
	}

	/*
	 * 추가
	 */
	@RequestMapping("/insertBuild")
	@ResponseBody
	public Map<String, String> insertBuild(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		boolean auth = CommUtil.getAuth(user);
		
		try {
			
			buildingService.insertBuild(request.getParameterMap(), auth);
			
			map.put("CODE", "0");
			map.put("MSG", "완료되었습니다.");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException :: " + e.getErrorCode());
			
			//if(e.getErrorCode() == 19) {
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
			//}
		}
		
		return map;
	}
	
	/*
	 * 수정
	 */
	@RequestMapping("/updateBuild")
	@ResponseBody
	public Map<String, String> updateBuild(HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			long result = buildingService.updateBuild(request.getParameterMap());
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("updateBuild SQLException :: " + e.getErrorCode());
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 삭제
	 */
	@RequestMapping("/deleteBuild")
	@ResponseBody
	public Map<String, String> deleteBuild(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		try {
			long result = buildingService.deleteBuild(request.getParameterMap());
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("deleteBuild SQLException :: " + e.getErrorCode());
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		return map;
	}
	
}
