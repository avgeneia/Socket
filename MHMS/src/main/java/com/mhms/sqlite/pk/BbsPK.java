package com.mhms.sqlite.pk;

import java.io.Serializable;

public class BbsPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int SID;
	
	private int CID;
	
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

	public int getSID() {
		return SID;
	}

	public void setSID(int sID) {
		SID = sID;
	}

	public int getCID() {
		return CID;
	}

	public void setCID(int cID) {
		CID = cID;
	}

	public int getBID() {
		return BID;
	}

	public void setBID(int bID) {
		BID = bID;
	}
}
