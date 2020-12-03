package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.BuildingDto;
import com.mhms.security.UserContext;
import com.mhms.sqlite.entities.Building;


public interface BuildingService {

	public Building selectBuild(Map<String, String[]> map) throws SQLException;
	
    public List<BuildingDto> buildingList(UserContext user);
    
    public List<BuildingDto> initBuild(UserContext user);
    
    public int insertBuild(Map<String, String[]> map) throws SQLException;
    
    public long updateBuild(Map<String, String[]> map) throws SQLException;
    
    public long deleteBuild(Map<String, String[]> map) throws SQLException;
}
