package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.AnswerDto;
import com.mhms.dto.NoticeDto;
import com.mhms.security.UserContext;
import com.mhms.sqlite.entities.Notice;


public interface NoticeService {

  Notice selectNotice(Map<String, String[]> map, UserContext user) throws SQLException;
	  
  List<NoticeDto> BBSList(UserContext user);
  
  List<NoticeDto> noticeList(UserContext user);
  
  List<AnswerDto> selectAnswer(Map<String, String[]> map, UserContext user);
  
  int insertNotice(Map<String, String[]> map, UserContext user) throws SQLException;
  
  int insertAnswer(Map<String, String[]> map, UserContext user) throws SQLException;
  
  void updateFile(Map<String, String[]> map, String paramString, int paramInt) throws SQLException;
  
  long updateNotice(Map<String, String[]> map) throws SQLException;
  
  long deleteNotice(Map<String, String[]> map) throws SQLException;
  
  long deleteAnswerBBS(Map<String, String[]> map) throws SQLException;
}
