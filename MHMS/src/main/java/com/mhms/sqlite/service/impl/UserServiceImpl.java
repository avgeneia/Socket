package com.mhms.sqlite.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.sqlite.Repository.UserRepository;
import com.mhms.sqlite.entities.Account;
import com.mhms.sqlite.entities.QAccount;
import com.mhms.sqlite.entities.QBuilding;
import com.mhms.sqlite.entities.QUserRole;
import com.mhms.sqlite.entities.UserRole;
import com.mhms.sqlite.service.UserService;
import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userDao;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<Account> getUser(){

		// CriteriaBuilder 인스턴스를 작성한다.
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		javax.persistence.criteria.CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
		
		// Root는 영속적 엔티티를 표시하는 쿼리 표현식이다. SQL의 FROM 과 유사함
		Root<Account> root = criteriaQuery.from(Account.class);
		
		// SQL의 WHERE절이다. 조건부는 CriteriaBuilder에 의해 생성
		Predicate restrictions = criteriaBuilder.equal(root.get("USERNM"), "admin");
		criteriaQuery.where(restrictions);
		
		// ORDER BY절. CriteriaQuery로 생성
		//criteriaQuery.orderBy(criteriaBuilder.desc(root.get("boardIdx")));
		
		//TypedQuery<Account> boardListQuery = entityManager.createQuery(criteriaQuery);
		
		List<Account> resultList = (List<Account>) userDao.findAll();
	    
	    return resultList;
	}
	
	@Override
	public Account getUser(String username, String password) {
		// TODO Auto-generated method stub
		
		Account resultMap = userDao.findByUSERNMAndUSERPW(username, password);
		
		return resultMap;
	}
	
	/*public List<UserLogin> getAllUsers() {
		List<UserLogin> persons = (List<UserLogin>) userDao.findAll();
		return persons;
	}

	public void save(UserLogin person) {
		userDao.save(person);
	}*/

	@Override
	public List<UserRole> getModifyUser(int uid) {
		
		JPAQuery query = new JPAQuery(entityManager);
		
		QUserRole userRole = QUserRole.userRole;
		QAccount account = QAccount.account;
		QBuilding building = QBuilding.building;
		List<UserRole> userRoleList = query.from(userRole)
				                     .join(userRole.user, account)
				                     .join(userRole.building, building)
				                     .on(userRole.building.BID.eq(building.BID))
				                     .where(userRole.user.UID.eq(uid))
				                     .list(userRole);
								
        return userRoleList;
	}
}
