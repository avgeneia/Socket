package com.mhms.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mhms.dto.AuthDto;
import com.mhms.dto.BuildingDto;
import com.mhms.dto.CodeDto;
import com.mhms.dto.RoomDto;
import com.mhms.dto.UserDto;
import com.mhms.security.UserContext;
import com.mhms.service.AuthService;
import com.mhms.service.BuildingService;
import com.mhms.service.CodeService;
import com.mhms.service.UserService;
import com.mhms.util.CommUtil;

@Controller
public class AuthController {
	
	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CodeService codeService;

	@Autowired
	private BuildingService buildingService;
	
	/*
	 * 조회 단건
	 */
	@RequestMapping("/selectAuth")
	@ResponseBody
	public Map<String, Object> selectAuth(@AuthenticationPrincipal UserContext user, HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
		    map.put("CODE", "0");
		    map.put("DATA", authService.selectAuth(request.getParameterMap()));
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
	@RequestMapping("/authList")
	public String authList(Model model, @AuthenticationPrincipal UserContext user) {
		
		//model.addAttribute("infoVO", map);
//		List<CodeDto> codeList = codeService.getCode("ROLE", false, 0);
//		
//		boolean auth = CommUtil.isRole(user, "ROLE_ADMIN");
//		
//		//관리자권한 체크
//		for(int i = 0; i < codeList.size(); i++) {
//			
//			if(!auth && codeList.get(i).getCd().equals("ROLE_ADMIN")) {
//				codeList.remove(i);
//			}
//		}
		
//		CodeDto codeDto = new CodeDto("-1", "-1", "선택", "", 1, 0);
//		codeList.add(0, codeDto);
		
		List<UserDto> userList = userService.getUserList(user);
		UserDto userDto = new UserDto(-1, "선택");
		userList.add(0, userDto);
		
		List<BuildingDto> builList = buildingService.initBuild(user);
		BuildingDto buildDto = new BuildingDto(-1, "선택");
		builList.add(0, buildDto);
		
		List<RoomDto> roomList = buildingService.initRoom(user);
		RoomDto roomDto = new RoomDto(-1, -1, "", "선택");
		roomList.add(0, roomDto);
		
		List<AuthDto> authList = authService.authList(user);
		
		//생각해보니 권한의 경우 이미 사용자 등록 때 등록되어있음 수정불가해야하는 항목... 아 빼버릴까...
		//model.addAttribute("initRoleVO", codeList);
		model.addAttribute("initBuildVO", builList);
		model.addAttribute("initRoomVO", roomList);
		model.addAttribute("initUserVO", userList);
		model.addAttribute("authList", authList);
		model.addAttribute("pageInfo", "authList");
		
		return "authList";
	}
	
	/*
	 * 추가
	 */
	@RequestMapping("/insertAuth")
	@ResponseBody
	public Map<String, String> insertAuth(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			authService.insertAuth(request.getParameterMap(), user);
			
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
	@RequestMapping("/updateAuth")
	@ResponseBody
	public Map<String, String> updateAuth(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			long result = authService.updateAuth(request.getParameterMap());
			
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
	@RequestMapping("/deleteAuth")
	@ResponseBody
	public Map<String, String> deleteAuth(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			long result = authService.deleteAuth(request.getParameterMap());
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		return map;
	}
}
