package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.CodePK;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

}
