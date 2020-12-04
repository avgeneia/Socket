package com.mhms.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.mhms.dto.BuildingDto;
import com.mhms.dto.QBuildingDto;
import com.mhms.security.UserContext;
import com.mhms.service.BuildingService;
import com.mhms.sqlite.entities.Building;
import com.mhms.sqlite.entities.QBuilding;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
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
	public Building selectBuild(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		JPAQuery query = new JPAQuery(entityManager);
		QBuilding building = QBuilding.building;
		 
		query.from(building);
		query.where(building.bid.eq(Integer.parseInt(map.get("bid")[0])).and(building.rid.eq(Integer.parseInt(map.get("rid")[0]))));
		
		Building result = query.singleResult(building);
		
		return result;
	}
	
	@Override
	public List<BuildingDto> buildingList(UserContext user) {
		// TODO Auto-generated method stub
		
		JPAQuery query = new JPAQuery(entityManager);
		
		QBuilding building = QBuilding.building;
		
		List<BuildingDto> dto = null;
		
		//관리자 권한을 확인해서 전체를 조회하게
		boolean auth = false;
		Iterator<GrantedAuthority> itertor = user.getAuthorities().iterator();
		while(itertor.hasNext()) {
			GrantedAuthority arg = itertor.next();
			if(arg.getAuthority().equals("ROLE_ADMIN")) {
				auth = true;
				break;
			}
		}
		
		query.from(building);
		if(auth) {
			query.where(building.bid.ne(0).and(building.rid.eq(0)));
		} else {
			query.where(building.bid.in(user.getBid()));
		}
		
		dto = query.list(new QBuildingDto(building.bid, building.rid, building.bnm, building.rnm));
		
		return dto;
	}
	
	//화면에 필요한 정보들을 넘겨주기위한 메소드
	@Override
	public List<BuildingDto> initBuild(UserContext user) {
		// TODO Auto-generated method stub

		JPAQuery query = new JPAQuery(entityManager);
		QBuilding building = QBuilding.building;
		
		//관리자 권한을 확인해서 전체를 조회하게
		boolean auth = false;
		Iterator<GrantedAuthority> itertor = user.getAuthorities().iterator();
		while(itertor.hasNext()) {
			GrantedAuthority arg = itertor.next();
			if(arg.getAuthority().equals("ROLE_ADMIN")) {
				auth = true;
				break;
			}
		}
		
		query.from(building);
		if(auth) {
			query.where(building.bid.ne(0));
		} else {
			query.where(building.bid.in(user.getBid()));
		}
		query.groupBy(building.bid);
		List<BuildingDto> result = query.list(new QBuildingDto(building.bid, building.bnm));
		     
		return result;
	}

	@Override
	public int insertBuild(Map<String, String[]> map, boolean authType) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn = dataSource.getConnection();
		String insertSQL = "";
		PreparedStatement pstmt = null;
		int result = 0; 
		
		//관리자 권한의 경우 건축물만 추가한다 매니저로.
		if(authType) {
			insertSQL = "INSERT INTO tb_building (bid, rid, bnm, rnm) VALUES(?, 0, ?, '매니저')";
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setInt(1, Integer.parseInt(map.get("rid")[0]));
			pstmt.setString(2, map.get("rnm")[0]);
			result = pstmt.executeUpdate();
		} else {
			insertSQL = "insert into tb_building (bid, rid, bnm, rnm) select bid, ?, bnm, ? from tb_building where bid = ? group by bid";
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setInt(1, Integer.parseInt(map.get("rid")[0]));
			pstmt.setString(2, map.get("rnm")[0]);
			pstmt.setInt(3, Integer.parseInt(map.get("bid")[0]));
			result = pstmt.executeUpdate();
		}
		
		return result;
	}
	
	@Transactional
	@Override
	public long updateBuild(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QBuilding building = QBuilding.building;
		
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, building);
		long reuslt = updateClause.set(building.rnm, String.valueOf(map.get("rnm")[0])).where(building.bid.eq(Integer.parseInt(map.get("bid")[0])).and(building.rid.eq(Integer.parseInt(map.get("rid")[0])))).execute();
		
		return reuslt;
	}
	
	@Transactional
	@Override
	public long deleteBuild(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub

		QBuilding building = QBuilding.building;
		
		JPADeleteClause deleteClause = new JPADeleteClause(entityManager, building);
		long result = deleteClause.where(building.bid.eq(Integer.parseInt(map.get("bid")[0])).and(building.rid.eq(Integer.parseInt(map.get("rid")[0])))).execute();
		
		return result;
	}

}
