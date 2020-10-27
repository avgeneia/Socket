package com.mhms.sqlite.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.CodePK;

@Entity
@Table(name = "TB_CODE")
@IdClass(CodePK.class)
public class Code implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String UPR_CD;

	@Id
	private String CD;
	
	private String CD_NM;
	private String COMMENT;
	private int SORT;
	private int ISDEL;
	
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
	public String getCD_NM() {
		return CD_NM;
	}
	public void setCD_NM(String cD_NM) {
		CD_NM = cD_NM;
	}
	public String getCOMMENT() {
		return COMMENT;
	}
	public void setCOMMENT(String cOMMENT) {
		COMMENT = cOMMENT;
	}
	public int getSORT() {
		return SORT;
	}
	public void setSORT(int sORT) {
		SORT = sORT;
	}
	public int getISDEL() {
		return ISDEL;
	}
	public void setISDEL(int iSDEL) {
		ISDEL = iSDEL;
	}
}
