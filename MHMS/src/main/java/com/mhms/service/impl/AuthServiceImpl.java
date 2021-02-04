package com.mhms.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.dto.AuthDto;
import com.mhms.dto.QAuthDto;
import com.mhms.security.UserContext;
import com.mhms.service.AuthService;
import com.mhms.sqlite.entities.QAccount;
import com.mhms.sqlite.entities.QBuilding;
import com.mhms.sqlite.entities.QUserRole;
import com.mhms.util.CommUtil;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
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
	public AuthDto selectAuth(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		JPAQuery query = new JPAQuery(entityManager);
		
		QUserRole userRole = QUserRole.userRole;
		
		AuthDto userDto = null;
		
		query.from(userRole);
		query.where(userRole.account.uid.eq(Integer.parseInt(map.get("uid")[0]))
				.and(userRole.building.bid.eq(Integer.parseInt(map.get("bid")[0]))
						.and(userRole.building.rid.eq(Integer.parseInt(map.get("rid")[0])))));
		userDto = query.singleResult(new QAuthDto(userRole.account.uid, userRole.account.usernm, userRole.building.bid, userRole.building.bnm, userRole.building.rid, userRole.building.rnm, userRole.role, userRole.comment));
		
		return userDto;
	}
	
	@Override
	public List<AuthDto> authList(UserContext user) {
		// TODO Auto-generated method stub
		
		JPAQuery query = new JPAQuery(entityManager);
		
		QUserRole userRole = QUserRole.userRole;
		QAccount account = QAccount.account;
		QBuilding building = QBuilding.building;
		
		boolean auth = CommUtil.isRole(user, "ROLE_ADMIN");
		
		query.from(userRole);
		query.leftJoin(userRole.account, account);
		query.leftJoin(userRole.building, building);
		if(!auth) {
			query.where(userRole.role.ne("ROLE_ADMIN").and(userRole.account.useyn.eq(1)));
		} else {
			query.where(userRole.account.useyn.eq(1));
		}
		
		List<AuthDto> dto = query.list(new QAuthDto(userRole.account.uid, userRole.account.usernm, userRole.building.bid, userRole.building.bnm, userRole.building.rid, userRole.building.rnm, userRole.role, userRole.comment));
		
		return dto;
	}

	@Override
	public int insertAuth(Map<String, String[]> map, UserContext user) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn = dataSource.getConnection();
		String insertSQL = "";
		PreparedStatement pstmt = null;
		int result = 0; 
		
		JPAQuery query = new JPAQuery(entityManager);
		QUserRole userRole = QUserRole.userRole;
		query.from(userRole);
		query.orderBy(userRole.sid.desc());
		int maxSid = query.singleResult(userRole).getSid();
		
		insertSQL = "INSERT INTO tb_userrole (comment, \"role\", writer, writerdate, bid, rid, uid, sid) VALUES(?, ?, ?, strftime(\"%Y%m%d\",'now','localtime'), ?, ?, ?, ?)";
		pstmt = conn.prepareStatement(insertSQL);
		pstmt.setString(1, map.get("comment")[0]);
		pstmt.setString(2, map.get("role")[0]);
		pstmt.setString(3, user.getUsername());
		pstmt.setInt(4, Integer.parseInt(map.get("bid")[0]));
		pstmt.setInt(5, Integer.parseInt(map.get("rid")[0]));
		pstmt.setInt(6, Integer.parseInt(map.get("uid")[0]));
		pstmt.setInt(7, maxSid > 0 ? maxSid + 1 : 1);
		result = pstmt.executeUpdate();

		pstmt.close();
		conn.close();
		
		return result;
	
	}
	
	@Transactional
	@Override
	public long updateAuth(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QUserRole userRole = QUserRole.userRole;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, userRole);
		
		long result = updateClause.set(userRole.role, map.get("role")[0])
				                  .set(userRole.comment, map.get("comment")[0])
				                  .where(userRole.building.bid.eq(Integer.parseInt(map.get("bid")[0]))
				                		  .and(userRole.building.rid.eq(Integer.parseInt(map.get("rid")[0]))
				                				  .and(userRole.account.uid.eq(Integer.parseInt(map.get("uid")[0])))))
				                  .execute();
		
		return result;
	}
	
	@Transactional
	@Override
	public long deleteAuth(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QUserRole userRole = QUserRole.userRole;
		JPADeleteClause deleteClause = new JPADeleteClause(entityManager, userRole);
		long result = deleteClause.where(userRole.account.uid.eq(Integer.parseInt(map.get("uid")[0]))
										.and(userRole.building.bid.eq(Integer.parseInt(map.get("bid")[0])))
											.and(userRole.building.rid.eq(Integer.parseInt(map.get("rid")[0]))))
								  .execute();
		return result;
	}
	
}
