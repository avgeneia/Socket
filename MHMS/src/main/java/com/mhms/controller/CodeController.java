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
import com.mhms.service.CodeService;

@Controller
public class CodeController {

	@Autowired
	private CodeService codeService;
	
	/*
	 * 조회 목록(MAIN)
	 */
	@RequestMapping("/codeList")
	public String codeList(Model model, @AuthenticationPrincipal UserContext user, HttpServletRequest request) {
		
		if(request.getParameter("cd") != null) {
			
			String cd = request.getParameterMap().get("cd")[0];
			model.addAttribute("cdVO", codeService.codeList(cd));
			model.addAttribute("gbn", true);
		} else {
			model.addAttribute("gbn", false);
		}
		//model.addAttribute("infoVO", map);
		model.addAttribute("uprVO", codeService.uprCodeList(user));
		model.addAttribute("pageInfo", "codeList");
		
		return "codeList";
	}
	
	/*
	 * 추가
	 */
	@RequestMapping("/insertCode")
	@ResponseBody
	public Map<String, String> insertBuild(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			codeService.insertCode(request.getParameterMap());
			
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
