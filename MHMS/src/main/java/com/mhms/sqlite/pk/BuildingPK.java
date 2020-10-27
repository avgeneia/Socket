package com.mhms.sqlite.pk;

import java.io.Serializable;

public class BuildingPK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int rid;
	private int bid;
	
	public BuildingPK(int rid, int bid) {
		this.rid = rid;
		this.bid = bid;
	}
	
	public BuildingPK() {
		
	}
	
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
}
