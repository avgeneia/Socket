package com.mhms.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mhms.vo.LoginVO;

@Controller
public class MainController {

	@RequestMapping(path="/main")
	public String Main(@ModelAttribute LoginVO loginVO, HttpServletRequest request) {
		
		return "main";
	}
	
//	@RequestMapping("/login.do")
//	private ModelAndView doLogin(@Validated LoginVO loginVO, HttpServletRequest request) throws Exception {
//		String userId = loginVO.getUser_nm();
//		String userPw = loginVO.getUser_pw();
//		User doc = usersRepo.findByUSERNMAndUSERPW(userId, userPw);
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if(doc == null) {
//			System.out.println("로그인실패");
//			
//			//로그인 성공 시 메인화면 연결
//			modelAndView.setViewName("login");
//			
//			return modelAndView;
//		}
//		
//		userInfo.setUser_Nm(doc.getUSERNM());
//		userInfo.setSessionId(request.getSession().getId());
//		userInfo.setDeviceType("1");
//		
//		//로그인 성공 시 메인화면 연결
//		modelAndView.setViewName("default");
//      
//		return modelAndView;
//    }
//	
//	@RequestMapping("/logout.do")
//	private String doLogout(HttpServletRequest request) throws Exception {
//		
//		request.getSession().invalidate();
//		
//		return "redirect:/";
//		
//    }
	
}
