package com.mhms.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mhms.sqlite.entities.Account;

public class UserContext extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Account user;
	
	public UserContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsernm(), account.getUserpw(), authorities);
        this.user = account;
    }
	
	public Account getAccount() {
        return user;
    }
}
