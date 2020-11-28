package com.mhms.sqlite.pk;

import java.io.Serializable;

import com.mhms.sqlite.entities.Building;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRolePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Building building;
}
