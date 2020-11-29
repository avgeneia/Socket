package com.mhms.sqlite.pk;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int uid;
	
	String role;
}
