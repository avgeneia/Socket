package com.mhms.service;

import java.util.ArrayList;
import java.util.List;

import com.mhms.dto.BuildingDto;


public interface BuildingService {

    public List<BuildingDto> getBuildingList(ArrayList<Integer> bid);
    
    public List<BuildingDto> getModifyUser(int uid);
}
