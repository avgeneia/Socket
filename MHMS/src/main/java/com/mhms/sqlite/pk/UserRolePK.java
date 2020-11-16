package com.mhms.sqlite.pk;

import java.io.Serializable;

import com.mhms.sqlite.entities.Account;
import com.mhms.sqlite.entities.Building;

public class UserRolePK implements Serializable {

	private Account user;
	
	private Building building;
}
