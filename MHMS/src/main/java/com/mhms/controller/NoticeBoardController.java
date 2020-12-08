package com.mhms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NoticeBoardController {
	
	@RequestMapping("/noticeBoard")
	public String noticeBoard(Model model) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "게시판");
		
		//model.addAttribute("infoVO", map);
		model.addAttribute("pageInfo", "noticeBoard");
		
		return "noticeBoard";
	}
}
