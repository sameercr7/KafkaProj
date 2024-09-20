package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.Canal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CanalRepo extends JpaRepository<Canal,Long> {

    @Query(value = "SELECT canal_name FROM canal WHERE canal_name <> '' ORDER BY canal_name",nativeQuery = true)
    List<String> findAllCanal();
}
