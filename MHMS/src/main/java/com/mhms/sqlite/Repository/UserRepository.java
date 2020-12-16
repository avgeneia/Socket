package com.mhms.sqlite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mhms.sqlite.entities.Account;

@Repository
public interface UserRepository extends JpaRepository<Account, Long> {

	Account findByusernm(String name);
	
}
