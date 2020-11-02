package com.mhms.sqlite.pk;

import java.io.Serializable;

public class CodePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String upr_cd;
	
	private String cd;
	
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

	public String getUPR_CD() {
		return upr_cd;
	}

	public void setUPR_CD(String uPR_CD) {
		upr_cd = uPR_CD;
	}

	public String getCD() {
		return cd;
	}

	public void setCD(String cD) {
		cd = cD;
	}
}
