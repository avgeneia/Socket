package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserPopupDto {
	
	int uid;		//사용자ID
	String type;	//insert, update 구분
	String usernm;	//사용자 이름
	String role;	//권한
	
	@QueryProjection
	public UserPopupDto(int uid, String usernm, String type, String role) {
		this.uid = uid;
		this.type = type;
		this.usernm = usernm;
		this.role = role;
	}
	
}
