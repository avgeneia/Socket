package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TB_USER")
public class Account {
	
	@Id
	@Column(name = "UID")  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int UID ;
	

	@Column(name = "USER_NM")
	private String USERNM;
	
	@Column(name = "USER_PW")
	private String USERPW;
	
	@Column(name = "ISDEL")
	private int ISDEL;
	
	@Column(name = "ROLE")
	private String ROLE;
}
