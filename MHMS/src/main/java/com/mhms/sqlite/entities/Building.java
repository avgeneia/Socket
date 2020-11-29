package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@Column(name = "bid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bid;
	
	@Id
	@Column(name = "rid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int rid;
	
	@Column(name = "bnm")
	private String bnm;
	
	@Column(name = "rnm")
	private String rnm;
	
}
