package com.mhms.service;

import java.util.List;

import com.mhms.dto.CodeDto;

public interface CodeService {
	
	public List<CodeDto> getCode(String upcd);
}
