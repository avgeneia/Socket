package com.mhms.sqlite.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.sqlite.Repository.UserRepository;
import com.mhms.sqlite.entities.Account;
import com.mhms.sqlite.service.UserService;

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
		
		TypedQuery<Account> boardListQuery = entityManager.createQuery(criteriaQuery);
		
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

}
