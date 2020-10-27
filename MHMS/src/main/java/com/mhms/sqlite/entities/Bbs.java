package com.mhms.sqlite.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.BbsPK;

@Entity
@Table(name = "TB_BBS")
@IdClass(BbsPK.class)
public class Bbs implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private int SID;

	@Id
	private int CID;
	
	private String TITLE;
	private String COMMENT;
	private String LINK;
	private int VIEWCNT;
	private int NOTICE;
	private String WRITER;
	private String WRITERDATE;
	
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
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getCOMMENT() {
		return COMMENT;
	}
	public void setCOMMENT(String cOMMENT) {
		COMMENT = cOMMENT;
	}
	public String getLINK() {
		return LINK;
	}
	public void setLINK(String lINK) {
		LINK = lINK;
	}
	public int getVIEWCNT() {
		return VIEWCNT;
	}
	public void setVIEWCNT(int vIEWCNT) {
		VIEWCNT = vIEWCNT;
	}
	public int getNOTICE() {
		return NOTICE;
	}
	public void setNOTICE(int nOTICE) {
		NOTICE = nOTICE;
	}
	public String getWRITER() {
		return WRITER;
	}
	public void setWRITER(String wRITER) {
		WRITER = wRITER;
	}
	public String getWRITERDATE() {
		return WRITERDATE;
	}
	public void setWRITERDATE(String wRITERDATE) {
		WRITERDATE = wRITERDATE;
	}
}
