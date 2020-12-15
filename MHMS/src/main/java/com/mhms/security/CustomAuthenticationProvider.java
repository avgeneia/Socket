package com.mhms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private CustomUserDetailsService userDeSer;
 
    @Override
    public Authentication authenticate(Authentication authentication) throws UsernameNotFoundException, AccountExpiredException {
        
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        
        UserContext user = (UserContext) userDeSer.loadUserByUsername(username);
        
        if(!matchPassword(password, user.getPassword())) {
            throw new BadCredentialsException("사용자ID 또는 비밀번호를 확인바랍니다.");
        }
 
        if(!user.isEnabled()) {
            throw new BadCredentialsException("사용자ID 또는 비밀번호를 확인바랍니다.");
        }

        if(user.getBid().isEmpty()) {
        	throw new AccountExpiredException("사용자 권한이 없습니다.");
        }
        
        return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
    
    private boolean matchPassword(String loginPwd, String password) {
        return loginPwd.equals(password);
    }
 
}