package com.mhms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mhms.dto.BuildingDto;
import com.mhms.dto.RoomDto;
import com.mhms.security.UserContext;
import com.mhms.sqlite.entities.Building;


public interface BuildingService {

  Building selectBuild(Map<String, String[]> paramMap) throws SQLException;
  
  List<BuildingDto> buildingList(UserContext paramUserContext);
  
  List<BuildingDto> initBuild(UserContext paramUserContext);
  
  List<RoomDto> initRoom(UserContext paramUserContext);
  
  int insertBuild(Map<String, String[]> paramMap, boolean paramBoolean) throws SQLException;
  
  long updateBuild(Map<String, String[]> paramMap) throws SQLException;
  
  long deleteBuild(Map<String, String[]> paramMap) throws SQLException;
}
