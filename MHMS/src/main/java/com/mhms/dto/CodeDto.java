package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;

@Data
public class CodeDto {
	
	String upr_cd;
	String cd;
	String cd_nm;
	String comment;
	int useyn;
	int sort;
	
	@QueryProjection
	public CodeDto(String upr_cd, String cd, String cd_nm, String comment, int useyn, int sort) {
		this.upr_cd = upr_cd;
		this.cd = cd;
		this.cd_nm = cd_nm;
		this.comment = comment;
		this.useyn = useyn;
		this.sort = sort;
	}
}
