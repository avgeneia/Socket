package com.mhms.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NotificationsController {

	@RequestMapping(path="/notifications.do")
	public ModelAndView notifications(HttpServletRequest request) {
		
		/* Main Controller로서 호출 시 세션을 확인해서
		 * 세션이 있으면 : Main
		 * 세션이 없으면 : Login 화면으로 보내줘야 한다.
		 * */
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("notifications");
		
		return modelAndView;
	}
	
}
