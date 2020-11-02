package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.CodePK;

@Entity
@IdClass(CodePK.class)
@Table(name = "TB_CODE")
public class Code {
	
	@Id
	@Column(name="UPR_CD")
	private String upr_cd;
	
	@Id
	@Column(name="CD")
	private String cd;
	
	@Column(name="CD_NM")
	private String cd_nm;
	
	@Column(name="COMMENT")
	private String comment;
	
	@Column(name="SORT")
	private int sort;
	
	@Column(name="ISDEL")
	private int isdel;

	public String getUPR_CD() {
		return upr_cd;
	}

	public void setUPR_CD(String uPR_CD) {
		upr_cd = uPR_CD;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
}
