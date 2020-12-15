package com.mhms.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		if(exception.getMessage() == null) {
			request.setAttribute("errorMsg", "사용자ID 또는 비밀번호를 확인바랍니다.");
		} else if(exception.getLocalizedMessage().equals("Bad credentials")) {
			request.setAttribute("errorMsg", "사용자ID 또는 비밀번호를 확인바랍니다.");
		} else {
			request.setAttribute("errorMsg", exception.getMessage());
		}
		request.getRequestDispatcher("/login?error=true").forward(request, response);
		
	}
}
