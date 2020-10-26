package com.mhms.sqlite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mhms.sqlite.entities.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

}
