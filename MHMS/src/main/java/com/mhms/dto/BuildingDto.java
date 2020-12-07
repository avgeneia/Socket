package com.mhms.dto;

import java.util.List;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BuildingDto {
	
	int bid;
	int rid;
	String bnm;
	String rnm;
	List<RoomDto> listDto;
	
	@QueryProjection
	public BuildingDto(int bid,	int rid, String bnm, String rnm) {
		this.bid = bid;
		this.rid = rid;
		this.bnm = bnm;
		this.rnm = rnm;
	}
	
	@QueryProjection
	public BuildingDto(int bid, String bnm) {
		this.bid = bid;
		this.bnm = bnm;
	}
	
	@QueryProjection
	public BuildingDto(int bid, String bnm, List<RoomDto> listDto) {
		this.bid = bid;
		this.bnm = bnm;
		this.listDto = listDto;
	}
}
