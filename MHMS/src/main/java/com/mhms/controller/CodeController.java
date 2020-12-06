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

import com.mhms.dto.CodeDto;
import com.mhms.security.UserContext;
import com.mhms.service.CodeService;
import com.mhms.sqlite.entities.Code;

@Controller
public class CodeController {

	@Autowired
	private CodeService codeService;
	
	/*
	 * 조회 단건
	 */
	@RequestMapping("/selectCode")
	@ResponseBody
	public Map<String, Object> selectCode(@AuthenticationPrincipal UserContext user, HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			Code dto = codeService.selectCode(request.getParameterMap());
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
	@RequestMapping("/codeList")
	public String codeList(Model model, @AuthenticationPrincipal UserContext user, HttpServletRequest request) {
		
		
		CodeDto dto = new CodeDto("선택안됨", "", "", "", 1, 0);
		model.addAttribute("cdVO", dto);
		model.addAttribute("gbn", false);
		model.addAttribute("uprVO", codeService.uprCodeList(user));
		model.addAttribute("pageInfo", "codeList");
		
		return "codeList";
	}
	
	/*
	 * 조회 하위코드/상세코드
	 */
	@RequestMapping("/code")
	public String code(Model model, @AuthenticationPrincipal UserContext user, HttpServletRequest request) {
		
		String cd = request.getParameterMap().get("cd")[0];
		model.addAttribute("cdVO", codeService.codeList(cd));
		model.addAttribute("uprCd", "[" + cd + "]");
		
		return "codeList :: #table_code";
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
			
			if(request.getParameter("gbn") == "code") {
				request.setAttribute("status_code", "CodeInsert");
				request.setAttribute("status_value", request.getParameter("upr_cd"));
			} else {
				request.setAttribute("status", "UprCodeInsert");
			}
			
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
	@RequestMapping("/updateCode")
	@ResponseBody
	public Map<String, String> updateCode(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			long result = codeService.updateCode(request.getParameterMap());
			
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
	
	/*
	 * 수정 : 사용유무
	 */
	@RequestMapping("/updateCodeUseYn")
	@ResponseBody
	public Map<String, String> updateCodeUseyn(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			long result = codeService.updateCodeUseyn(request.getParameterMap());
			
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
	
	/*
	 * 삭제
	 */
	@RequestMapping("/deleteCode")
	@ResponseBody
	public Map<String, String> deleteBuild(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			long result = codeService.deleteCode(request.getParameterMap());
			
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
