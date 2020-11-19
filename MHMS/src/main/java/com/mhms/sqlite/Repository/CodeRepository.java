package com.mhms.sqlite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mhms.sqlite.entities.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
	
}
