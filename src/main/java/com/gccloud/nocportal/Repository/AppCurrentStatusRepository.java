package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.MovementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppCurrentStatusRepository extends JpaRepository<MovementStatus,Long> {

    @Query(value = "SELECT * FROM movement_status WHERE application_id = ?1 ;",nativeQuery = true)
    MovementStatus findByApplicationId(Long applicationId);

    @Query(value = "SELECT * FROM movement_status WHERE application_id = ?1 ;",nativeQuery = true)
    List<MovementStatus> findAppCurrIdByAppplicationId(String id);
}
