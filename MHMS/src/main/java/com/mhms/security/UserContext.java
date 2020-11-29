package com.mhms.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString 
public class UserContext extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int uid;
	private ArrayList<Integer> bid;
	
	public UserContext(String username, String password
			         , boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked
			         , Collection<? extends GrantedAuthority> authorities, int uid, ArrayList<Integer> bid) {
        super(username, password, authorities);
        this.uid = uid;
        this.bid = bid;
    }
	
}
