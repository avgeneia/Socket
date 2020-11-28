package com.mhms.sqlite.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TB_USER")
public class Account {
	
	@Id
	@Column(name = "uid")  	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int uid;
	
	@OneToMany(mappedBy="account")
	private List<UserRole> userRole;
	
	@Column(name = "usernm")
	private String usernm;
	
	@Column(name = "userpw")
	private String userpw;
	
	@Column(name = "useyn")
	private int useyn;
	
	@Column(name = "role")
	private String role;
	
	@Transient
	private int isupdate;
}
