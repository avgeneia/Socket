package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.BbsPK;

@Entity
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getViewcnt() {
		return viewcnt;
	}

	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}

	public String getWriter() {
		return Writer;
	}

	public void setWriter(String writer) {
		Writer = writer;
	}

	public String getWriteDate() {
		return WriteDate;
	}

	public void setWriteDate(String writeDate) {
		WriteDate = writeDate;
	}
}
