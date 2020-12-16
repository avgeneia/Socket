package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.CodeDto;
import com.mhms.security.UserContext;
import com.mhms.sqlite.entities.Code;

public interface CodeService {
	
  Code selectCode(Map<String, String[]> paramMap) throws SQLException;
  
  List<CodeDto> getCode(String paramString, boolean paramBoolean, int paramInt);
  
  List<CodeDto> uprCodeList(UserContext paramUserContext);
  
  List<CodeDto> codeList(String paramString);
  
  int insertCode(Map<String, String[]> paramMap) throws SQLException;
  
  long updateCode(Map<String, String[]> paramMap) throws SQLException;
  
  long updateCodeUseyn(Map<String, String[]> paramMap) throws SQLException;
  
  long deleteCode(Map<String, String[]> paramMap) throws SQLException;
}
