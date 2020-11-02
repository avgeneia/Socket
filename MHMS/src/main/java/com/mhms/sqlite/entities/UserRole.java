package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mhms.sqlite.pk.UserRolePK;

@Entity
@IdClass(UserRolePK.class)
@Table(name = "TB_USERROLE")
public class UserRole {
	
	@Id
	@ManyToOne
	@JoinColumn(name="UID")
	private User user;
	
	@Id
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="BID", referencedColumnName="BID"),
		@JoinColumn(name="RID", referencedColumnName="RID")
	})
	private Building building;
	
	@Column(name="LEVEL")
	private int level;
	
	@Column(name="COMMENT")
	private String comment;
	
	@Column(name="WRITER")
	private String Writer;
	
	@Column(name="WRITEDATE")
	private String WriteDate;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
