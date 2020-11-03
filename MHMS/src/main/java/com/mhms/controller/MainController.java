package com.mhms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mhms.util.vo.LoginVO;

@Controller
public class MainController {
	
	@RequestMapping(path="/")
	public ModelAndView Main(@ModelAttribute LoginVO loginVO, HttpServletRequest request) {
		
		/* Main Controller로서 호출 시 세션을 확인해서
		 * 세션이 있으면 : Main
		 * 세션이 없으면 : Login 화면으로 보내줘야 한다.
		 * */
		
		ModelAndView modelAndView = new ModelAndView();
		
		HttpSession httpSession = request.getSession();
		
		System.out.println(httpSession.getId());
		
		modelAndView.setViewName("login");
		
		return modelAndView;
	}
	
	@RequestMapping("/login.do")
	private ModelAndView doLogin(@Validated LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("---------------------> login!!!!!!!!!!!");
        
        ModelAndView modelAndView = new ModelAndView();
        
        modelAndView.setViewName("dashboard");
        
        return modelAndView;
    }

}
