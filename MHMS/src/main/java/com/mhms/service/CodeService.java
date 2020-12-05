package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.CodeDto;
import com.mhms.security.UserContext;

public interface CodeService {
	
	public List<CodeDto> getCode(String upcd);
	
	public List<CodeDto> uprCodeList(UserContext user);
	
	public List<CodeDto> codeList(String uprCd);
	
	public int insertCode(Map<String, String[]> map) throws SQLException;
	
	public long updateCode(Map<String, String[]> map) throws SQLException;
	
	public long deleteCode(Map<String, String[]> map) throws SQLException;
}
