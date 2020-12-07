package com.mhms.sqlite.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mhms.sqlite.pk.AccountPK;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(AccountPK.class)
@Table(name = "TB_USER")
public class Account {
	
	@Id
	@Column(name = "uid")  	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int uid;
	
	@Column(name = "role")
	private String role;
	
	@OneToMany(mappedBy="account")
	private List<UserRole> userRole;
	
	@Column(name = "usernm")
	private String usernm;
	
	@Column(name = "userpw")
	private String userpw;
	
	@Column(name = "useyn")
	private int useyn;
	
	@Transient
	private int isupdate;
}
