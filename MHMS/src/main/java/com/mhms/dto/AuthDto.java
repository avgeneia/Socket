package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AuthDto {
	
	int uid;
	String usernm;
	int bid;
	String bnm;
	int rid;
	String rnm;
	String role;
	String comment;
	
	@QueryProjection
	public AuthDto(int uid,	String usernm, int bid, String bnm, int rid, String rnm, String role, String comment) {
		this.uid = uid;
		this.usernm = usernm;
		this.bid = bid;
		this.bnm = bnm;
		this.rid = rid;
		this.rnm = rnm;
		this.role = role;
		this.comment = comment;
	}
	
}
