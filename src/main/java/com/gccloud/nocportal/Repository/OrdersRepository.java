package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrdersRepository extends JpaRepository<Orders,Long> {
    @Query(value = "SELECT upload_doc_path FROM orders WHERE id =?1 ;",nativeQuery = true)
    String findPathByOrderId(Long id);
}
