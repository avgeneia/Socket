package com.mhms.sqlite.pk;

import java.io.Serializable;

public class BbsPK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int SID;
	private int CID;
	
	public BbsPK(int sid, int cid) {
		this.SID = sid;
		this.CID = cid;
	}
	
	public BbsPK() {
		
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

}
