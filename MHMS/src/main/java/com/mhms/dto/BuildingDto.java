package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;

@Data
public class BuildingDto {
	
	int bid;
	int rid;
	String bnm;
	String rnm;
	int isUpdate;
	
	@QueryProjection
	public BuildingDto(int bid,	int rid, String bnm, String rnm, int isUpdate) {
		this.bid = bid;
		this.rid = rid;
		this.bnm = bnm;
		this.rnm = rnm;
		this.isUpdate = isUpdate;
	}
	
	@QueryProjection
	public BuildingDto(int bid, String bnm) {
		this.bid = bid;
		this.bnm = bnm;
	}
}
