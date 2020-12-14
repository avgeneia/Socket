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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mhms.dto.QUserDto;
import com.mhms.dto.QUserListDto;
import com.mhms.dto.UserDto;
import com.mhms.dto.UserListDto;
import com.mhms.security.UserContext;
import com.mhms.service.UserService;
import com.mhms.sqlite.entities.Account;
import com.mhms.sqlite.entities.QAccount;
import com.mhms.sqlite.entities.QBuilding;
import com.mhms.sqlite.entities.QCode;
import com.mhms.sqlite.entities.QUserRole;
import com.mhms.util.CommUtil;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Service
public class UserServiceImpl implements UserService {
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private DataSource dataSource;
	
	@Override
	public List<UserListDto> userList(UserContext user){
		
		JPAQuery query = new JPAQuery(entityManager);

		QUserRole userRole = QUserRole.userRole;
		QAccount account = QAccount.account;
		QBuilding building = QBuilding.building;
		QCode code = QCode.code;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		UserDetails userDetails = (UserDetails) principal;
		
		//현재 사용자의 정보를 가져옴
		String UserRole = userDetails.getAuthorities().toArray()[0].toString();
		
		List<UserListDto> dto = null; 
		
		query.from(account);
		query.leftJoin(account.userRole, userRole);
		query.leftJoin(userRole.building, building);
		query.on(userRole.building.bid.eq(building.bid));
		query.on(userRole.building.rid.eq(building.rid));
		query.join(code).on(account.role.eq(code.cd).and(code.upr_cd.eq("ROLE")));
		
		//사용자가 매니저의 경우 해당 건물의 사용자만 조회해야한다.
		if(UserRole.equals("ROLE_MANAGER")) {
			query.where(account.role.eq("ROLE_MANAGER").or(account.role.eq("ROLE_USER")));
			query.where(userRole.building.bid.in(user.getBid()));
		}
		
		dto = query.list(new QUserListDto(account.uid,account.usernm, account.useyn, code.cd_nm, userRole.building.bnm, userRole.building.rnm, userRole.building));
		
	    return dto;
	}
	
	@Override
	public List<UserListDto> selectUser(int uid) {
		
		JPAQuery query = new JPAQuery(entityManager);
		
		QUserRole userRole = QUserRole.userRole;
		QAccount account = QAccount.account;
		QBuilding building = QBuilding.building;
		
		List<UserListDto> dto = query.from(account)
									 .leftJoin(account.userRole, userRole)
					                 .leftJoin(userRole.building, building)
					                 .on(userRole.building.bid.eq(building.bid))
					                 .on(userRole.building.rid.eq(building.rid))
					                 .where(account.uid.eq(uid))
					                 .list(new QUserListDto(account.uid, account.usernm, account.useyn, account.role, userRole.building.bnm, userRole.building.rnm, userRole.building));
								
        return dto;
	}

	@Override
	public int insertUser(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = dataSource.getConnection();
		String insertSQL = "";
		PreparedStatement pstmt = null;
		int result = 0; 

		JPAQuery query = new JPAQuery(entityManager);
		
		//추가하기전에 'usernm'으로 중복검사를 실시한다.
		QAccount account = QAccount.account;
		Account user = null;
		user = query.from(account).where(account.usernm.eq(map.get("usernm")[0])).singleResult(account);
		
		if(user != null) {
			return 0;
		}
		
		//사용자의 비밀번호는 초기 비밀번호로 입력한다 id = pw
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		//관리자 권한의 경우 건축물만 추가한다 매니저로.
		insertSQL = "INSERT INTO tb_user (\"role\", usernm, userpw, useyn) VALUES(?, ?, ?, 1)";
		pstmt = conn.prepareStatement(insertSQL);
		pstmt.setString(1, map.get("role")[0]);
		pstmt.setString(2, map.get("usernm")[0]);
		pstmt.setString(3, encoder.encode(map.get("usernm")[0]));
		result = pstmt.executeUpdate();
		
		return result;
	}

	@Transactional
	@Override
	public long updateUser(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QAccount account = QAccount.account;

		JPAQuery query = new JPAQuery(entityManager);
		
		//추가하기전에 'usernm'으로 중복검사를 실시한다.
		Account user = null;
		user = query.from(account).where(account.usernm.eq(map.get("usernm")[0])).singleResult(account);
		
		if(user != null) {
			return 0;
		}
		
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, account);
		long result = updateClause.set(account.usernm, map.get("usernm")[0])
				                  .set(account.role, map.get("role")[0])
				                  .where(account.uid.eq(Integer.parseInt(map.get("uid")[0])))
				                  .execute();
		
		return result;
	}

	@Transactional
	@Override
	public long resetUser(Map<String, String[]> map, UserContext user) throws SQLException {
		// TODO Auto-generated method stub
		QAccount account = QAccount.account;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, account);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		long result = updateClause.set(account.userpw, encoder.encode(user.getUsername()))
				                  .where(account.uid.eq(Integer.parseInt(map.get("uid")[0])))
				                  .execute();
		
		return result;
	}
	
	@Transactional
	@Override
	public long changePassword(Map<String, String[]> map, UserContext user) throws SQLException {
		// TODO Auto-generated method stub
		QAccount account = QAccount.account;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, account);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		long result = updateClause.set(account.userpw, encoder.encode(map.get("userpw")[0]))
				                  .where(account.uid.eq(user.getUid()))
				                  .execute();
		
		return result;
	}
	
	@Transactional
	@Override
	public long updateUserUseyn(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		QAccount account = QAccount.account;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, account);
		
		long result = updateClause.set(account.useyn, Integer.parseInt(map.get("useyn")[0]))
				                  .where(account.uid.eq(Integer.parseInt(map.get("uid")[0])))
				                  .execute();
		
		return result;
	}
	
	@Transactional
	@Override
	public long deleteUser(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QAccount account = QAccount.account;
		JPADeleteClause deleteClause = new JPADeleteClause(entityManager, account);
		long result = deleteClause.where(account.uid.eq(Integer.parseInt(map.get("uid")[0]))).execute();
		
		return result;
	}

	@Override
	public List<UserDto> getUserList(UserContext user) {
		// TODO Auto-generated method stub
		QAccount account = QAccount.account;
		JPAQuery query = new JPAQuery(entityManager);
		
		boolean auth = CommUtil.isRole(user, "ROLE_ADMIN");
		
		List<UserDto> dto = null;
		
		query.from(account);
		if(!auth) {
			query.where(account.role.ne("ROLE_ADMIN").and(account.useyn.eq(1)));
		} else {
			query.where(account.useyn.eq(1));
		}
		dto = query.list(new QUserDto(account.uid, account.usernm));
		
		return dto;
	}

}
