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

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(UserRolePK.class)
@Table(name = "TB_USERROLE")
public class UserRole {
	
	@Id
	@ManyToOne
	@JoinColumn(name="UID")
	private Account user;
	
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
	
}
