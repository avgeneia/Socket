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
	private String USER_NM;
	
	@Column(name = "USER_PW")
	private String USER_PW;
	
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

	public String getUSER_NM() {
		return USER_NM;
	}

	public void setUSER_NM(String uSER_NM) {
		USER_NM = uSER_NM;
	}

	public String getUSER_PW() {
		return USER_PW;
	}

	public void setUSER_PW(String uSER_PW) {
		USER_PW = uSER_PW;
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
