package com.mhms.sqlite.pk;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
