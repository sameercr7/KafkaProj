package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.Grievances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GrievancesRepository extends JpaRepository<Grievances,Long> {
    @Query(value = "SELECT * FROM grievances WHERE created_by = ?1 ;",nativeQuery = true)
    List<Grievances> findByusername(String username);
}
