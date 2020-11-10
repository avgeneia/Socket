package com.mhms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mhms.sqlite.Repository.UserRepository;
import com.mhms.sqlite.entities.User;
import com.mhms.util.SessionInfo;
import com.mhms.vo.LoginVO;

@Controller
public class MainController {
	
	@Resource
	private SessionInfo userInfo;
	
	@Autowired
	private UserRepository usersRepo;

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
	private ModelAndView doLogin(@Validated LoginVO loginVO, HttpServletRequest request) throws Exception {
		String userId = loginVO.getUser_nm();
		String userPw = loginVO.getUser_pw();
		User doc = usersRepo.findByUSERNMAndUSERPW(userId, userPw);
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(doc == null) {
			System.out.println("로그인실패");
			
			//로그인 성공 시 메인화면 연결
			modelAndView.setViewName("login");
			
			return modelAndView;
		}
		
		userInfo.setUser_Nm(doc.getUSERNM());
		userInfo.setSessionId(request.getSession().getId());
		userInfo.setDeviceType("1");
		
		//로그인 성공 시 메인화면 연결
		modelAndView.setViewName("default");
      
		return modelAndView;
    }
	
	@RequestMapping("/logout.do")
	private String doLogout(HttpServletRequest request) throws Exception {
		
		request.getSession().invalidate();
		
		return "redirect:/";
		
    }
	
}
