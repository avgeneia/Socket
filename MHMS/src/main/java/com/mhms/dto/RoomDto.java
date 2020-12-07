package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RoomDto {
	
	int bid;
	int rid;
	String bnm;
	String rnm;
	
	@QueryProjection
	public RoomDto(int bid,	int rid, String bnm, String rnm) {
		this.bid = bid;
		this.rid = rid;
		this.bnm = bnm;
		this.rnm = rnm;
	}
	
}
