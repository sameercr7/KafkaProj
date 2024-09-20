package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.DistrictList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictListRepo extends JpaRepository<DistrictList,Long> {
    @Query(value = "SELECT district FROM dst_list ORDER BY district",nativeQuery = true)
    List<String> findAllDistrict();
}
