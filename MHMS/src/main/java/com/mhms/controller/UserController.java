package com.mhms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mhms.dto.CodeDto;
import com.mhms.dto.UserListDto;
import com.mhms.service.CodeService;
import com.mhms.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CodeService codeService;
	
	@RequestMapping("/userList")
	public String userprofile(Model model) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "사용자 관리");
		
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		UserDetails userDetails = (UserDetails)principal;
		//현재 사용자의 정보를 가져옴
		String UserRole = userDetails.getAuthorities().toArray()[0].toString();
		
		//코드 그룹
		List<CodeDto> codeList = codeService.getCode("ROLE");
		
		//사용자 목록
		List<UserListDto> userList = userService.getUser();
		
		for(int i = 0; i < userList.size(); i++) {
			
			//관리자의 경우 모든 사용자의 수정이 가능.
			if(userDetails.getAuthorities().iterator().next().getAuthority().equals("ROLE_ADMIN")) {
				userList.get(i).setIsUpdate(1);
			}
		}
		
		model.addAttribute("infoVO", map);
		model.addAttribute("userVO", userList);
		model.addAttribute("pageInfo", "userList");
		
		return "userList";
		
	}
	
	@RequestMapping("/userModify")
	@ResponseBody
	public UserListDto userModify(@RequestParam(value = "uid") int uid) throws JsonProcessingException {

		List<UserListDto> result = userService.getModifyUser(uid);
		
		return result.get(0);
	}
	
}
