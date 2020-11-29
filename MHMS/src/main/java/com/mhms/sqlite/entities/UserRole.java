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
	@JoinColumns({
		@JoinColumn(name = "uid",
			referencedColumnName = "uid",
			insertable = false, updatable = false),
		@JoinColumn(name = "role",
    		referencedColumnName = "role",
    		insertable = false, updatable = false)
	})
	private Account account;
	
	@Id
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "bid",
			referencedColumnName = "bid"),
		@JoinColumn(name = "rid",
	    	referencedColumnName = "rid")
	})
	private Building building;
	
	@Column(name="role")
	private String role;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="writer")
	private String writer;
	
	@Column(name="writerdate")
	private String writerdate;
}
