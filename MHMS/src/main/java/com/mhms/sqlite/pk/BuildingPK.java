package com.mhms.sqlite.pk;

import java.io.Serializable;

public class BuildingPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int RID;
	
	private int BID;
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public int getRID() {
		return RID;
	}

	public void setRID(int rID) {
		RID = rID;
	}

	public int getBID() {
		return BID;
	}

	public void setBID(int bID) {
		BID = bID;
	}
	
	
}
