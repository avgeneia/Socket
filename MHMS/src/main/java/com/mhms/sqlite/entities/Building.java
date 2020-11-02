package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.BuildingPK;

@Entity
@IdClass(BuildingPK.class)
@Table(name = "TB_BUILDING")
public class Building {
	
	@Id
	@Column(name = "BID")
	private int BID;
	
	@Id
	@Column(name = "RID")
	private int RID;

	@Column(name = "BNM")
	private String BNM;
	
	@Column(name = "RNM")
	private String RNM;

	public int getBID() {
		return BID;
	}

	public void setBID(int bID) {
		BID = bID;
	}

	public int getRID() {
		return RID;
	}

	public void setRID(int rID) {
		RID = rID;
	}

	public String getBNM() {
		return BNM;
	}

	public void setBNM(String bNM) {
		BNM = bNM;
	}

	public String getRNM() {
		return RNM;
	}

	public void setRNM(String rNM) {
		RNM = rNM;
	}
}
