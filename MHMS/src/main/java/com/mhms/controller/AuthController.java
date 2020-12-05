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
import com.mhms.service.AuthService;
import com.mhms.util.CommUtil;

@Controller
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	/*
	 * 조회 목록(MAIN)
	 */
	@RequestMapping("/authList")
	public String authList(Model model, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "권한 관리");
		
		//model.addAttribute("infoVO", map);
		model.addAttribute("authList", authService.authList(user));
		model.addAttribute("pageInfo", "authList");
		
		return "authList";
	}
	
	/*
	 * 추가
	 */
	@RequestMapping("/insertAuth")
	@ResponseBody
	public Map<String, String> insertAuth(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		boolean auth = CommUtil.getAuth(user);
		
		try {
			
			authService.insertAuth(request.getParameterMap(), 0);
			
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
}
