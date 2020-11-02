package com.mhms.sqlite.pk;

import java.io.Serializable;

import com.mhms.sqlite.entities.Building;
import com.mhms.sqlite.entities.User;

public class UserRolePK implements Serializable {

	private User user;
	
	private Building building;
}
