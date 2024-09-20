package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.UsersSpring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UsersSpring,Long> {


    @Query(value="SELECT distinct username AS ce2 FROM  usersspring WHERE ce1 = ? AND role = 'ce2' ",nativeQuery = true)
    List<String> fetchAllCe2(String ce1);
    UsersSpring findUserByUsername(String username);


    @Query(value = "SELECT ee \n" +
            "FROM usersspring \n" +
            "WHERE ee IS NOT NULL \n" +
            "AND ee <> ''",nativeQuery = true)
    List<String> findAllEe();

    @Query(value = "SELECT * FROM usersspring WHERE ee=?1",nativeQuery = true)
    List<UsersSpring> findRespectiveSeCeByEe(String ee);

    @Query(value="SELECT distinct username AS se FROM  usersspring WHERE ce2 = ? AND role = 'se' ",nativeQuery = true)
    List<String> fetchAllSeWhereCe2(String ce2);

    @Query(value="SELECT distinct username AS ee FROM  usersspring WHERE se = ? AND role = 'ee' ",nativeQuery = true)
    List<String> fetchAllEeWhereSe(String se);
    @Query(value="SELECT distinct username AS ce1 FROM  usersspring WHERE role = 'ce1' ",nativeQuery = true)
    List<String> fetchAllCe1();

    @Query(value = "SELECT distinct username AS ce2 FROM  usersspring WHERE se=?1  AND role = 'ee'",nativeQuery = true)
    List<String> fetchEEforSE(String se);

    @Query(value = "SELECT distinct username FROM usersspring WHERE role = 'subAdmin'",nativeQuery = true)
    List<String> finAllSubAdmin();

    @Query(value = "select distinct(se) from usersspring where ce1=?1 ",nativeQuery = true)
    List<String> fetchSEbyCE1Name(String username);

    @Query(value = "select distinct(ee) from usersspring where ce1=?1 ",nativeQuery = true)
    List<String> fetchEEbyCE1Name(String username);

    @Query(value = "select distinct(ee) from usersspring where ce2=?1 ",nativeQuery = true)
    List<String> fetchEEbyCE2Name(String username);

    @Query(value = " SELECT id FROM usersspring where username=?1 ",nativeQuery = true)
    String findByUserName(String username);

    boolean existsByUsername(String username);

}
