package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mhms.sqlite.pk.NoticePK;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(NoticePK.class)
@Table(name = "tb_notice")
public class Notice {
	
	@Id
	@Column(name="sid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sid;
	
	@Id
	@Column(name="cid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	
	@Id
	@Column(name="bid")
	private int bid;
	
	@Column(name="title")
	private String title;
	
	@Column(name="content")
	private String content;
	
	@Column(name="link")
	private String link;
	
	@Column(name="viewcnt")
	private int viewcnt;
	
	@Column(name="notice")
	private int notice_lv;
	
	@Column(name="filename")
	private String filename;
	
//	@Column(name="filesize")
//	@ColumnDefault("0") //default 0
//	private int filesize;
	
	@Column(name="Writer")
	private String writer;
	
	@Column(name="WriteDate")
	private String writedate;

	@Transient
	private int isupdate;

	@Transient
	private int no;
}
