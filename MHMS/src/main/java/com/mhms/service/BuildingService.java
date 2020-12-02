package com.mhms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mhms.dto.BuildingDto;


public interface BuildingService {

    public List<BuildingDto> getBuildingList(ArrayList<Integer> bid);
    
    public List<BuildingDto> getModifyUser(int uid);
    
    public List<BuildingDto> initBuild(ArrayList<Integer> bid);
    
    public int insertBuild(Map<String, String[]> map) throws SQLException;
}
