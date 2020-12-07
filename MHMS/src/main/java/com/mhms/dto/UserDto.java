package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;

@Data
public class UserDto {
	
	int uid;
	String usernm;
	
	@QueryProjection
	public UserDto(int uid, String usernm) {
		this.uid = uid;
		this.usernm = usernm;
	}
	
}
