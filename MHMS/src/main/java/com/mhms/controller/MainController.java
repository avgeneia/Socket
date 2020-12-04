package com.mhms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mhms.security.UserContext;
import com.mhms.vo.LoginVO;

@Controller
public class MainController {

	@RequestMapping("/index")
	public String Main(@ModelAttribute LoginVO loginVO, HttpServletRequest request, Model model, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "메인");
		
		model.addAttribute("infoVO", user);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", "dashboard");
		
		return "index";
	}
	
	@RequestMapping("/icons")
	public String icon(Model model) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "아이콘");
		
		model.addAttribute("infoVO", map);
		model.addAttribute("pageInfo", "icons");
		
		return "icons";
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
