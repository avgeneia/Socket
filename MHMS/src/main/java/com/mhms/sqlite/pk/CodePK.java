package com.mhms.sqlite.pk;

import java.io.Serializable;

public class CodePK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String UPR_CD;
	private String CD;
	
	public CodePK(String uprCd, String cd) {
		this.UPR_CD = uprCd;
		this.CD = cd;
	}
	
	public CodePK() {
		
	}

	public String getUPR_CD() {
		return UPR_CD;
	}

	public void setUPR_CD(String uPR_CD) {
		UPR_CD = uPR_CD;
	}

	public String getCD() {
		return CD;
	}

	public void setCD(String cD) {
		CD = cD;
	}
}
