package com.mhms.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mhms.dto.QUserListDto;
import com.mhms.dto.UserListDto;
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
	public List<UserListDto> getUser(){
		
		JPAQuery query = new JPAQuery(entityManager);

		QUserRole userRole = QUserRole.userRole;
		QAccount account = QAccount.account;
		QBuilding building = QBuilding.building;
		
		List<UserListDto> dto = query.from(account)
					                 .leftJoin(account.userRole, userRole)
					                 .leftJoin(userRole.building, building)
					                 .on(userRole.building.bid.eq(building.bid))
					                 .on(userRole.building.rid.eq(building.rid))
					                 .list(new QUserListDto(account.usernm, account.useyn, account.role, userRole.building.bnm, userRole.building.bnm, userRole.building));
		
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
					                 .list(new QUserListDto(account.usernm, account.useyn, account.role, userRole.building.bnm, userRole.building.bnm, userRole.building));
								
        return dto;
	}
}
