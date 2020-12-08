package com.mhms.sqlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

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
	
	@Column(name="Writer")
	private String writer;
	
	@Column(name="WriteDate")
	private String writedate;

}
