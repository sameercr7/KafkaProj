package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.NocList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NocListRepo extends JpaRepository<NocList, Long> {

    @Query(value = "SELECT noc_name FROM noc_list", nativeQuery = true)
    List<String> findAllNocName();

    @Query(value = "SELECT * FROM noc_list WHERE noc_name=?1", nativeQuery = true)
    NocList findByNocName(String nocName);


}
