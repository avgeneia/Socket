package com.mhms.sqlite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mhms.sqlite.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUSERNMAndUSERPW(String name, String pw);
}
