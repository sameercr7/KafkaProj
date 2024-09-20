package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails,Long> {


    @Query(value = "SELECT * FROM user_details WHERE user_id = ?1 ",nativeQuery = true)
    UserDetails findByUserId(Long id);

    @Query(value = "SELECT agency_name FROM user_details WHERE user_id = ?1 ",nativeQuery = true)
    String findByUserSpringId(String username);
}
