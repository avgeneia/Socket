package com.mhms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mhms.dto.NoticeDto;
import com.mhms.security.UserContext;
import com.mhms.service.NoticeService;
import com.mhms.vo.LoginVO;

@Controller
public class MainController {
	
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping("/index")
	public String Main(@ModelAttribute LoginVO loginVO, HttpServletRequest request, Model model, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "메인");
		
		model.addAttribute("infoVO", user);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", "dashboard");
		
		return "index";
	}
	
	@RequestMapping("/selectBBStop5")
	public String selectBBStop5(Model model, @AuthenticationPrincipal UserContext user) {
		
		List<NoticeDto> dto = this.noticeService.BBSList(user);
		
		for (int i = dto.size() - 1; i > 4; i--) {
			dto.remove(i);
		}
	       
	    model.addAttribute("bbsList", dto);
	    
	    return "index :: #table_index_bbs";
	}
	
	@RequestMapping("/selectNoticetop5")
	public String selectNoticetop5(Model model, @AuthenticationPrincipal UserContext user) {
		
		List<NoticeDto> dto = this.noticeService.noticeList(user);
		
		for (int i = dto.size() - 1; i > 4; i--) {
			dto.remove(i); 			
		}
		
	    model.addAttribute("noticeList", dto);
	    return "index :: #table_index_notice";
	}
	
}
