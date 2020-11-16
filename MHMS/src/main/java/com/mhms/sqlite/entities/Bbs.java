package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.BbsPK;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(BbsPK.class)
@Table(name = "TB_BBS")
public class Bbs {
	
	@Id
	@Column(name="SID")
	private int SID;
	
	@Id
	@Column(name="CID")
	private int CID;
	
	@Id
	@Column(name="BID")
	private int BID;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="CONTENT")
	private String content;
	
	@Column(name="LINK")
	private String link;
	
	@Column(name="VIEWCNT")
	private int viewcnt;

	@Column(name="WRITER")
	private String Writer;
	
	@Column(name="WRITEDATE")
	private String WriteDate;

}
