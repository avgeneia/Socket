package com.NettyBoot.VO;

import java.util.List;

public class DataSetVO {
	
	String id; //interfaceID
	String type;
	List<RowVO> row;	//rowdata
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<RowVO> getRow() {
		return row;
	}

	public void setRow(List<RowVO> row) {
		this.row = row;
	}
	
}
