package com.mhms.dto;

import com.mhms.sqlite.entities.Building;
import com.mysema.query.annotations.QueryProjection;

import lombok.Data;

@Data
public class UserListDto {
	
	int uid;
	String usernm;
	int useyn;
	String role;
	String bnm;
	String rnm;
	Building building;
	int isUpdate;
	
	@QueryProjection
	public UserListDto(String usernm, int useyn, String role, String bnm, String rnm, Building building) {
		this.usernm = usernm;
		this.useyn = useyn;
		this.role = role;
		this.bnm = bnm;
		this.rnm = rnm;
		this.building = building;
		//this.isUpdate = isupdate;
	}
	
}
