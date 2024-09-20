package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.LogsOfMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LogsOfFileRepository extends JpaRepository<LogsOfMovement,Long> {

    @Query(value = "SELECT * FROM logs_of_movement WHERE movement_status_id = ?1 ORDER BY id DESC LIMIT 1;",nativeQuery = true)
    LogsOfMovement findLatestLogDetails(Long appCurrentStatusId);


    @Query(value = "SELECT l.*, a.status, a.remark FROM logs_of_movement l JOIN movement_status a ON l.movement_status_id = a.id WHERE l.app_id = ?1 ; ",nativeQuery = true)
    List<Map<String, Object>> findLogsDetailsByAppCurrentStatusId(Long id);


}
