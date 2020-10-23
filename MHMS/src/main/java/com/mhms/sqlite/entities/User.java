package com.mhms.sqlite.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String USER_ID ;
	
	private String USER_NM;
	private String PASSWORD;
	
	private String AUTH_ID;
	private String ADDRESS_GROUP;
	
	
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getUSER_NM() {
		return USER_NM;
	}
	public void setUSER_NM(String uSER_NM) {
		USER_NM = uSER_NM;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public String getAUTH_ID() {
		return AUTH_ID;
	}
	public void setAUTH_ID(String aUTH_ID) {
		AUTH_ID = aUTH_ID;
	}
	public String getADDRESS_GROUP() {
		return ADDRESS_GROUP;
	}
	public void setADDRESS_GROUP(String aDDRESS_GROUP) {
		ADDRESS_GROUP = aDDRESS_GROUP;
	}
	
}
