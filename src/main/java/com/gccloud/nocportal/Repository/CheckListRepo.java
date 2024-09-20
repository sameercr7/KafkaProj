package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CheckListRepo extends JpaRepository<CheckList,Long> {
    @Query(value = "SELECT * FROM check_list WHERE noc_list_id=?1",nativeQuery = true)
    List<CheckList> findAllCheckListById(Long index);

    @Query(value = "SELECT * FROM check_list WHERE noc_list_id=?1",nativeQuery = true)
    List<CheckList> findByNocName(Long id);
}
