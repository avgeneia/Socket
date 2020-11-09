package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_USER")
public class User {
	
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

//	@OneToMany
//	@JoinColumn(name = "UID")
//	private Set<Userrole> userrole;
	
	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public String getUSERNM() {
		return USERNM;
	}

	public void setUSERNM(String uSERNM) {
		USERNM = uSERNM;
	}

	public String getUSERPW() {
		return USERPW;
	}

	public void setUSERPW(String uSERPW) {
		USERPW = uSERPW;
	}

	public int getISDEL() {
		return ISDEL;
	}

	public void setISDEL(int iSDEL) {
		ISDEL = iSDEL;
	}

//	public Set<Userrole> getUserrole() {
//		return userrole;
//	}
//
//	public void setUserrole(Set<Userrole> userrole) {
//		this.userrole = userrole;
//	}
}
