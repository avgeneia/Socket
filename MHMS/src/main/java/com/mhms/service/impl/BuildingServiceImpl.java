package com.mhms.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.dto.BuildingDto;
import com.mhms.dto.QBuildingDto;
import com.mhms.service.BuildingService;
import com.mhms.sqlite.entities.QBuilding;
import com.mysema.query.jpa.impl.JPAQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLTemplates;

@Service
public class BuildingServiceImpl implements BuildingService {
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private DataSource dataSource;
	
	SQLTemplates templates = new H2Templates();
	Configuration configuration = new Configuration(templates);
	
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
	
	//화면에 필요한 정보들을 넘겨주기위한 메소드
	@Override
	public List<BuildingDto> initBuild(ArrayList<Integer> bid) {
		// TODO Auto-generated method stub

		JPAQuery query = new JPAQuery(entityManager);
		QBuilding building = QBuilding.building;
		
		query.from(building);
		query.where(building.bid.in(bid));
		query.groupBy(building.bid);
		List<BuildingDto> result = query.list(new QBuildingDto(building.bid, building.bnm));
		     
		return result;
	}

	@Override
	public int insertBuild(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		String insertSQL = "insert into tb_building (bid, rid, bnm, rnm) select bid, ?, bnm, ? from tb_building where bid = ? group by bid";
		
		Connection conn = dataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(insertSQL);
		pstmt.setInt(1, Integer.parseInt(map.get("rid")[0]));
		pstmt.setString(2, map.get("rnm")[0]);
		pstmt.setInt(3, Integer.parseInt(map.get("bid")[0]));
		int result = pstmt.executeUpdate();
		
		return result;
	}
	
}
