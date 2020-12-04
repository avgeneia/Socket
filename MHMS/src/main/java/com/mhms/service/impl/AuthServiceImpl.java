package com.mhms.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.dto.AuthDto;
import com.mhms.dto.QAuthDto;
import com.mhms.security.UserContext;
import com.mhms.service.AuthService;
import com.mhms.sqlite.entities.QAccount;
import com.mhms.sqlite.entities.QBuilding;
import com.mhms.sqlite.entities.QUserRole;
import com.mysema.query.jpa.impl.JPAQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLTemplates;

@Service
public class AuthServiceImpl implements AuthService {
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private DataSource dataSource;
	
	SQLTemplates templates = new H2Templates();
	Configuration configuration = new Configuration(templates);
	
	@Override
	public List<AuthDto> authList(UserContext user) {
		// TODO Auto-generated method stub
		
		JPAQuery query = new JPAQuery(entityManager);
		
		QUserRole userRole = QUserRole.userRole;
		QAccount account = QAccount.account;
		QBuilding building = QBuilding.building;
		
		query.from(userRole);
		query.leftJoin(userRole.account, account);
		query.leftJoin(userRole.building, building);
		List<AuthDto> dto = query.list(new QAuthDto(userRole.account.uid, userRole.account.usernm, userRole.building.bid, userRole.building.bnm, userRole.building.rid, userRole.building.rnm, userRole.role, userRole.comment));
		
		return dto;
	}

	@Override
	public int insertAuth(Map<String, String[]> map, int type) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn = dataSource.getConnection();
		String insertSQL = "";
		PreparedStatement pstmt = null;
		int result = 0; 
		
		/*
		 * type : 코드저장 구분
		 * 0 : 상위코드
		 * 1 : 하위코드
		 * */
		if(type == 0) {
			
			insertSQL = "INSERT INTO tb_code (cd, upr_cd, cd_nm, comment, isdel, sort) VALUES(?, '*', ?, ?, 0, ?)";
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, map.get("cd")[0]);
			pstmt.setString(2, map.get("cd_nm")[0]);
			pstmt.setString(2, map.get("comment")[0]);
			pstmt.setString(2, map.get("sort")[0]);
			result = pstmt.executeUpdate();
			
		} else {
			insertSQL = "INSERT INTO tb_code (cd, upr_cd, cd_nm, comment, isdel, sort) VALUES('', '', '', '', 0, 0)";
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setInt(1, Integer.parseInt(map.get("rid")[0]));
			pstmt.setString(2, map.get("rnm")[0]);
			pstmt.setInt(3, Integer.parseInt(map.get("bid")[0]));
			result = pstmt.executeUpdate();
		}
		
		return result;
	
	}
	
}
