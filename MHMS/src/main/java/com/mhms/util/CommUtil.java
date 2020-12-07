package com.mhms.util;

import java.util.Iterator;

import org.springframework.security.core.GrantedAuthority;

import com.mhms.security.UserContext;

public class CommUtil {
	
	public static boolean getAuth(UserContext user) {
		//관리자 권한을 확인해서 전체를 조회하게
		boolean auth = false;
		Iterator<GrantedAuthority> itertor = user.getAuthorities().iterator();
		while(itertor.hasNext()) {
			GrantedAuthority arg = itertor.next();
			if(arg.getAuthority().equals("ROLE_ADMIN")) {
				auth = true;
				break;
			}
		}
		
		return auth;
	}
}
