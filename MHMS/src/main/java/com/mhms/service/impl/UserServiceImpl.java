package com.mhms.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mhms.dto.QUserListDto;
import com.mhms.dto.UserListDto;
import com.mhms.security.UserContext;
import com.mhms.service.UserService;
import com.mhms.sqlite.entities.QAccount;
import com.mhms.sqlite.entities.QBuilding;
import com.mhms.sqlite.entities.QUserRole;
import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class UserServiceImpl implements UserService {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<UserListDto> getUser(UserContext user){
		
		JPAQuery query = new JPAQuery(entityManager);

		QUserRole userRole = QUserRole.userRole;
		QAccount account = QAccount.account;
		QBuilding building = QBuilding.building;
		
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
		
		//사용자가 매니저의 경우 해당 건물의 사용자만 조회해야한다.
		if(UserRole.equals("ROLE_MANAGER")) {
			query.where(account.role.eq("ROLE_MANAGER").or(account.role.eq("ROLE_USER")));
			query.where(userRole.building.bid.in(user.getBid()));
		}
		
		dto = query.list(new QUserListDto(account.uid,account.usernm, account.useyn, account.role, userRole.building.bnm, userRole.building.rnm, userRole.building));
		
	    return dto;
	}
	
	@Override
	public List<UserListDto> getModifyUser(int uid) {
		
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
}
