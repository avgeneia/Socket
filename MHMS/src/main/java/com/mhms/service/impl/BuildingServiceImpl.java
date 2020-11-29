package com.mhms.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mhms.dto.BuildingDto;
import com.mhms.dto.QBuildingDto;
import com.mhms.service.BuildingService;
import com.mhms.sqlite.entities.QBuilding;
import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class BuildingServiceImpl implements BuildingService {
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<BuildingDto> getBuildingList(ArrayList<Integer> bid) {
		// TODO Auto-generated method stub
		
		JPAQuery query = new JPAQuery(entityManager);
		
		QBuilding building = QBuilding.building;
		
		List<BuildingDto> dto = null;
		
		query.from(building);
		query.where(building.bid.in(bid));
		dto = query.list(new QBuildingDto(building.bid, building.rid, building.bnm, building.rnm));
		
		return dto;
	}

	@Override
	public List<BuildingDto> getModifyUser(int uid) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
