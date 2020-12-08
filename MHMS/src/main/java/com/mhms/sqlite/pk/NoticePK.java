package com.mhms.sqlite.pk;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int sid;
	
	private int cid;
	
	private int bid;
	
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
