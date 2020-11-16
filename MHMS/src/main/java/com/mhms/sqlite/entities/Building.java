package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mhms.sqlite.pk.BuildingPK;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(BuildingPK.class)
@Table(name = "TB_BUILDING")
public class Building {
	
	@Id
	@Column(name = "BID")
	private int BID;
	
	@Id
	@Column(name = "RID")
	private int RID;

	@Column(name = "BNM")
	private String BNM;
	
	@Column(name = "RNM")
	private String RNM;

}
