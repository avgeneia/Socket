package com.mhms.sqlite.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(BuildingPK.class)
public class Building implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private int bid;
	private String BNM;

	@Id
	private int rid;
	private String RNM;
	
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public String getBNM() {
		return BNM;
	}
	public void setBNM(String bNM) {
		BNM = bNM;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getRNM() {
		return RNM;
	}
	public void setRNM(String rNM) {
		RNM = rNM;
	}
}
