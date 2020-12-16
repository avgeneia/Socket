package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
  int sid;
  
  int cid;
  
  int bid;
  
  String content;
  
  String writer;
  
  String writedate;
  
  int isupdate;
  
  @QueryProjection
  public AnswerDto(int sid, int cid, int bid, String content, String writer, String writedate) {
    this.sid = sid;
    this.cid = cid;
    this.bid = bid;
    this.content = content;
    this.writer = writer;
    this.writedate = writedate;
  }
}
