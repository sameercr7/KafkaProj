package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    //    This Query For Fetch Stats Dashboard Data For All

    @Query(value = "SELECT COUNT(*) FROM application a JOIN movement_status acs ON a.id = acs.application_id WHERE acs.status = ?1 ;", nativeQuery = true)
    Long countByApplicationStatus(String status);

    @Query(value = "SELECT COUNT(*) FROM movement_status WHERE status = ? AND date >= CURDATE() - INTERVAL 7 DAY", nativeQuery = true)
    Long get7DaysPendingApplication(String pending);

    @Query(value = "SELECT COUNT(*) FROM movement_status WHERE status = ? AND date >= CURDATE() - INTERVAL 15 DAY", nativeQuery = true)
    Long get15DaysPendingApplication(String pending);

    //    This Query For Fetch Stats Dashboard Data For Current User

    @Query(value = "SELECT COUNT(*) FROM application " +
            "WHERE user_id = :userDetailsId " +
            "AND date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long getApplicationCountForUser(@Param("userDetailsId") Long userDetailsId,
                                    @Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate);

    @Query(value = "SELECT COUNT(*) FROM application a " +
            "JOIN movement_status acs ON a.id = acs.application_id " +
            "WHERE acs.status = :status " +
            "AND a.user_id = :userDetailsId " +
            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long countByApplicationStatusForUser(@Param("status") String status,
                                         @Param("userDetailsId") Long userDetailsId,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate);

    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
            "JOIN application a ON acs.application_id = a.id " +
            "WHERE acs.status = :status " +
            "AND a.user_id = :userDetailsId " +
            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long get15DaysPendingApplicationForUser(@Param("status") String status,
                                            @Param("userDetailsId") Long userDetailsId,
                                            @Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate);

    @Query(value = "SELECT COUNT(*) FROM application a " +
            "JOIN movement_status acs ON a.id = acs.application_id " +
            "WHERE acs.status = :status " +
            "AND acs.certificate_issued = 'true' " +
            "AND a.user_id = :userDetailsId " +
            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long getCertificateIssuedCountForUser(@Param("status") String status,
                                          @Param("userDetailsId") Long userDetailsId,
                                          @Param("startDate") Date startDate,
                                          @Param("endDate") Date endDate);

    @Query(value = "SELECT COUNT(*) FROM application a " +
            "JOIN movement_status acs ON a.id = acs.application_id " +
            "WHERE acs.re_submission = 'true' " +
            "AND a.user_id = :userDetailsId " +
            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long getReSubmittedApplicationForUser(@Param("userDetailsId") Long userDetailsId,
                                          @Param("startDate") Date startDate,
                                          @Param("endDate") Date endDate);

    @Query(value = "SELECT acs.id, a.applied_for, a.date, acs.application_id, acs.status, acs.from_user, acs.to_user " +
            "FROM application a INNER JOIN movement_status acs ON a.id = acs.application_id " +
            "WHERE a.user_id = ?1 AND a.date BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationsOfCurrentUser(Long userDetailsId, Date startDate, Date endDate);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.date BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT COUNT(*) FROM application a " +
            "JOIN movement_status acs ON a.id = acs.application_id " +
            "WHERE acs.status = :status " +
            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long countByApplicationStatusAndDateRange(@Param("status") String status,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);


    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
            "JOIN application a ON a.id = acs.application_id " +
            "WHERE acs.status = :status " +
            "AND acs.date BETWEEN :startDate AND :endDate " +
            "AND acs.date >= :sevenDaysAgo", nativeQuery = true)
    Long get7DaysPendingApplicationByFinYear(@Param("status") String status,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate,
                                             @Param("sevenDaysAgo") Date sevenDaysAgo);

    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
            "JOIN application a ON a.id = acs.application_id " +
            "WHERE acs.status = :status " +
            "AND acs.date BETWEEN :startDate AND :endDate " +
            "AND acs.date >= :fifteenDaysAgo", nativeQuery = true)
    Long get15DaysPendingApplicationByFinYear(@Param("status") String status,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("fifteenDaysAgo") Date fifteenDaysAgo);


    @Query(value = "SELECT COUNT(*) FROM application a JOIN movement_status acs ON a.id = acs.application_id \n" +
            "WHERE acs.to_user = ?1 AND a.date BETWEEN ?2 AND ?3 ", nativeQuery = true)
    Long countByDateRangeWhereToUser(String username, Date startDate, Date endDate);

//    @Query("SELECT COUNT(a) FROM Application a WHERE a.ce2 = :ce2 AND a.date BETWEEN :startDate AND :endDate")
//    Long countByDateRangeWhereCe2(@Param("ce2") String ce2, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
//
//
//    @Query("SELECT COUNT(a) FROM Application a WHERE a.se = :se AND a.date BETWEEN :startDate AND :endDate")
//    Long countByDateRangeWhereSe(@Param("se") String se, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
//
//    @Query("SELECT COUNT(a) FROM Application a WHERE a.ee = :ee AND a.date BETWEEN :startDate AND :endDate")
//    Long countByDateRangeWhereEe(@Param("ee") String ee, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(value = "SELECT COUNT(*) FROM application a " +
            "JOIN movement_status acs ON a.id = acs.application_id " +
            "WHERE acs.to_user = ?1 AND acs.status = ?2 " +
            "AND acs.date BETWEEN ?3 AND ?4 ", nativeQuery = true)
    Long countByApplicationStatusAndDateRangeWhereToUser(String username, String status, Date startDate, Date endDate);


//    @Query(value = "SELECT COUNT(*) FROM application a " +
//            "JOIN movement_status acs ON a.id = acs.application_id " +
//            "WHERE a.ce2 = :ce2 AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
//    Long countByApplicationStatusAndDateRangeWhereCe2(@Param("ce2") String ce2,@Param("status") String status,
//                                                      @Param("startDate") Date startDate,
//                                                      @Param("endDate") Date endDate);
//
//    @Query(value = "SELECT COUNT(*) FROM application a " +
//            "JOIN movement_status acs ON a.id = acs.application_id " +
//            "WHERE a.se = :se AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
//    Long countByApplicationStatusAndDateRangeWhereSe(@Param("se") String se,@Param("status") String status,
//                                                      @Param("startDate") Date startDate,
//                                                      @Param("endDate") Date endDate);
//
//    @Query(value = "SELECT COUNT(*) FROM application a " +
//            "JOIN movement_status acs ON a.id = acs.application_id " +
//            "WHERE a.ee = :ee AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate", nativeQuery = true)
//    Long countByApplicationStatusAndDateRangeWhereEe(@Param("ee") String se,@Param("status") String status,
//                                                     @Param("startDate") Date startDate,
//                                                     @Param("endDate") Date endDate);


    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
            "JOIN application a ON a.id = acs.application_id " +
            "WHERE acs.to_user = ?1 AND acs.status = ?2 " +
            "AND acs.date BETWEEN ?3 AND ?4 " +
            "AND acs.date >= ?5", nativeQuery = true)
    Long get7DaysPendingApplicationByFinYearWhereToUser(String username, String status, Date startDate, Date endDate, Date sevenDaysAgo);


//    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
//            "JOIN application a ON a.id = acs.application_id " +
//            "WHERE a.ce2 = :ce2 AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate " +
//            "AND acs.date >= :sevenDaysAgo", nativeQuery = true)
//    Long get7DaysPendingApplicationByFinYearWhereCe2(@Param("ce2") String ce2,@Param("status") String status,
//                                                      @Param("startDate") Date startDate,
//                                                      @Param("endDate") Date endDate,
//                                                      @Param("sevenDaysAgo") Date sevenDaysAgo);
//
//
//    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
//            "JOIN application a ON a.id = acs.application_id " +
//            "WHERE a.se = :se AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate " +
//            "AND acs.date >= :sevenDaysAgo", nativeQuery = true)
//    Long get7DaysPendingApplicationByFinYearWhereSe(@Param("se") String se,@Param("status") String status,
//                                                      @Param("startDate") Date startDate,
//                                                      @Param("endDate") Date endDate,
//                                                      @Param("sevenDaysAgo") Date sevenDaysAgo);
//
//
//    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
//            "JOIN application a ON a.id = acs.application_id " +
//            "WHERE a.ee = :ee AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate " +
//            "AND acs.date >= :sevenDaysAgo", nativeQuery = true)
//    Long get7DaysPendingApplicationByFinYearWhereEe(@Param("ee") String ee,@Param("status") String status,
//                                                     @Param("startDate") Date startDate,
//                                                     @Param("endDate") Date endDate,
//                                                     @Param("sevenDaysAgo") Date sevenDaysAgo);


    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
            "JOIN application a ON a.id = acs.application_id " +
            "WHERE acs.to_user = ?1 AND acs.status = ?2 " +
            "AND acs.date BETWEEN ?3 AND ?4 " +
            "AND acs.date >= ?5 ", nativeQuery = true)
    Long get15DaysPendingApplicationByFinYearWhereToUser(String username, String status, Date startDate, Date endDate, Date fifteenDaysAgo);


//    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
//            "JOIN application a ON a.id = acs.application_id " +
//            "WHERE a.ce2 = :ce2 AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate " +
//            "AND acs.date >= :fifteenDaysAgo", nativeQuery = true)
//    Long get15DaysPendingApplicationByFinYearWhereCe2(@Param("ce2") String ce2,
//                                                      @Param("status") String status,
//                                                      @Param("startDate") Date startDate,
//                                                      @Param("endDate") Date endDate,
//                                                      @Param("fifteenDaysAgo") Date fifteenDaysAgo);
//
//    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
//            "JOIN application a ON a.id = acs.application_id " +
//            "WHERE a.se = :se AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate " +
//            "AND acs.date >= :fifteenDaysAgo", nativeQuery = true)
//    Long get15DaysPendingApplicationByFinYearWhereSe(@Param("se") String se,
//                                                      @Param("status") String status,
//                                                      @Param("startDate") Date startDate,
//                                                      @Param("endDate") Date endDate,
//                                                      @Param("fifteenDaysAgo") Date fifteenDaysAgo);
//
//
//    @Query(value = "SELECT COUNT(*) FROM movement_status acs " +
//            "JOIN application a ON a.id = acs.application_id " +
//            "WHERE a.ee = :ee AND acs.status = :status " +
//            "AND acs.date BETWEEN :startDate AND :endDate " +
//            "AND acs.date >= :fifteenDaysAgo", nativeQuery = true)
//    Long get15DaysPendingApplicationByFinYearWhereEe(@Param("ee") String ee,
//                                                     @Param("status") String status,
//                                                     @Param("startDate") Date startDate,
//                                                     @Param("endDate") Date endDate,
//                                                     @Param("fifteenDaysAgo") Date fifteenDaysAgo);


    @Query(value = "SELECT a.*, ms.status,ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id " +
            " JOIN movement_status ms ON a.id = ms.application_id" +
            " WHERE a.admin = ?1 AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataByUser(String username, Date startDate, Date endDate);

//    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_details ud ON a.user_id = ud.id \n" +
//            "JOIN movement_status acs ON a.id = acs.application_id \n" +
//            "WHERE acs.to_user = ?1 AND a.date BETWEEN ?2 AND ?3 ;",nativeQuery = true)
//    List<Map<String, Object>> findAllApplicationDataByToUser(String username,Date startDate,Date endDate);


    @Query(value = "SELECT a.*, ud.agency_name,acs.status FROM application a JOIN user_details ud ON a.user_id = ud.id \n" +
            "JOIN movement_status acs ON a.id = acs.application_id \n" +
            "WHERE a.created_by = ?1 AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataByToUser(String username, Date startDate, Date endDate);


//    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id WHERE a.ce2 = ?1 AND a.date BETWEEN ?2 AND ?3;",nativeQuery = true)
//    List<Map<String, Object>> findAllApplicationDataByUserCe2(String username,Date startDate,Date endDate);
//
//    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id WHERE a.se = ?1 AND a.date BETWEEN ?2 AND ?3;",nativeQuery = true)
//    List<Map<String, Object>> findAllApplicationDataByUserSe(String username,Date startDate,Date endDate);
//
//    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id WHERE a.ee = ?1 AND a.date BETWEEN ?2 AND ?3;",nativeQuery = true)
//    List<Map<String, Object>> findAllApplicationDataByUserEe(String username,Date startDate,Date endDate);

    @Query(value = "SELECT a.*, \n" +
            "       cd.id AS cd_id, \n" +
            "       cd.remarks, \n" +
            "       cd.curr_status,\n" +
            "       cd.association_id, \n" +
            "       cd.data_field, \n" +
            "       cd.check_list_id, \n" +
            "       cd.description \n" +
            "FROM application a\n" +
            "JOIN document_status cd ON a.id = cd.application_id \n" +
            "WHERE a.id = ?1;", nativeQuery = true)
    List<Map<String, Object>> findApplicationDataById(Long id);

    @Query(value = "SELECT a.*,ud.agency_name, acs.to_user FROM application a JOIN user_Details ud ON a.user_id = ud.id  " +
            "JOIN movement_status acs ON a.id = acs.application_id WHERE acs.to_user = ?1 " +
            "AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataByUserAndByOwnership(String username, Date startDate, Date endDate);


    @Query(value = "SELECT a.*,ud.agency_name, acs.to_user,acs.status FROM application a JOIN user_Details ud ON a.user_id = ud.id  " +
            "JOIN movement_status acs ON a.id = acs.application_id WHERE acs.to_user = ?1 " +
            "AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataByToUserAndByOwnership(String username, Date startDate, Date endDate);


//    @Query(value = "SELECT a.*,ud.agency_name, acs.to_user FROM application a JOIN user_Details ud ON a.user_id = ud.id  " +
//            "JOIN movement_status acs ON a.id = acs.application_id WHERE a.ce2 = ?1 AND acs.to_user = ?1 " +
//            "AND a.date BETWEEN ?2 AND ?3 ;",nativeQuery = true)
//    List<Map<String, Object>> findAllApplicationDataByUserCe2AndByOwnership(String username,Date startDate,Date endDate);
//
//
//    @Query(value = "SELECT a.*,ud.agency_name, acs.to_user FROM application a JOIN user_Details ud ON a.user_id = ud.id  " +
//            "JOIN movement_status acs ON a.id = acs.application_id WHERE a.se = ?1 AND acs.to_user = ?1 " +
//            "AND a.date BETWEEN ?2 AND ?3 ;",nativeQuery = true)
//    List<Map<String, Object>> findAllApplicationDataByUserSeAndByOwnership(String username,Date startDate,Date endDate);
//
//
//    @Query(value = "SELECT a.*,ud.agency_name, acs.to_user FROM application a JOIN user_Details ud ON a.user_id = ud.id  " +
//            "JOIN movement_status acs ON a.id = acs.application_id WHERE a.ee = ?1 AND acs.to_user = ?1 " +
//            "AND a.date BETWEEN ?2 AND ?3 ;",nativeQuery = true)
//    List<Map<String, Object>> findAllApplicationDataByUserEeAndByOwnership(String username,Date startDate,Date endDate);

    @Query(value = "SELECT * FROM Application where spc_id=?1", nativeQuery = true)
    List<Application> findByUniqueId(String id);


    @Query(value = "SELECT COALESCE(MAX(a.id), 0) FROM Application a", nativeQuery = true)
    long getLastApplicationId();

    @Query(value = " SELECT \n" +
            "    a.ce1 AS toUser,\n" +
            "    COUNT(a.id) AS totalApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' AND ms.to_user = a.ce1 THEN 1 ELSE 0  END) AS pendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' AND ms.to_user = a.ce1 THEN 1  ELSE 0  END) AS inProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' AND ms.to_user = a.ce1 THEN 1 ELSE 0  END) AS approvedApplications,\n" +
            "    SUM(CASE   WHEN ms.status = 'REJECTED' AND ms.to_user = a.ce1 THEN 1  ELSE 0  END) AS rejectedApplications\n" +
            "FROM \n" +
            "    application a\n" +
            "LEFT JOIN \n" +
            "    movement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "    a.ce1 IN (:ce1Users)\n" +
            "    AND a.date between  :startDate AND :endDate  \n" +
            "GROUP BY \n" +
            "    a.ce1;",
            nativeQuery = true)
    List<Map<String, Object>> getApplicationCountsByCe1Users(@Param("ce1Users") List<String> ce1Users,
                                                             @Param("startDate") Date startDate,
                                                             @Param("endDate") Date endDate);


    @Query(value = "SELECT\n" +
            " n.noc_name,\n" +
            " COALESCE(s.approved_count, 0) AS approved_count,\n" +
            "COALESCE(s.pending_count, 0) AS pending_count,\n" +
            " COALESCE(s.rejected_count, 0) AS rejected_count,\n" +
            " COALESCE(s.process_count, 0) AS process_count\n" +
            "        FROM\n" +
            "        noc_list n\n" +
            "    LEFT JOIN \n" +
            "     (SELECT \n" +
            "    a.applied_for, \n" +
            "     SUM(CASE WHEN s.status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\n" +
            "  SUM(CASE WHEN s.status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count,\n" +
            "      SUM(CASE WHEN s.status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count, \n" +
            "SUM(CASE WHEN s.status = 'IN_PROCESS' THEN 1 ELSE 0 END) AS process_count\n" +
            "  FROM \n" +
            "      application a\n" +
            "       JOIN \n" +
            "      movement_status s ON a.id = s.application_id\n" +
            "     WHERE \n" +
            "         s.to_user = ?1  and\n" +
            "          s.date BETWEEN  ?2 AND ?3 \n" +
            "   GROUP BY \n" +
            "   a.applied_for\n" +
            "   ) s\n" +
            "    ON \n" +
            "      n.noc_name = s.applied_for ;", nativeQuery = true)
    List<Map<String, Object>> fetchTheTotalCountOfGraph(String username, Date startDate, Date endDate);

    @Query(value = "SELECT created_by FROM Application where id=?1", nativeQuery = true)
    String findByAppId(Long applicationId);

    @Query(value = "SELECT acs.id, a.applied_for,a.spc_id, a.date, acs.application_id, acs.status, acs.from_user, acs.to_user " +
            "FROM application a INNER JOIN movement_status acs ON a.id = acs.application_id " +
            "WHERE a.user_id = ?1 AND acs.status =?2 AND a.date BETWEEN ?3 AND ?4 ", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationWhereStatus(Long userDetailsId, String status, Date startDate, Date endDate);

    @Query(value = "SELECT * FROM application where id=?1", nativeQuery = true)
    Application findApplicationDataByAppId(Long id);

    @Query(value = "SELECT * FROM application where id=?1", nativeQuery = true)
    List<Application> findStatusById(Long applicationId);


    @Query(value = "SELECT a.*, ud.agency_name, ms.status\n" +
            "FROM application a\n" +
            "JOIN user_details ud ON a.user_id = ud.id\n" +
            "JOIN movement_status ms ON ms.application_id = a.id\n" +
            "WHERE ms.status = ?1 \n" +
            "  AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataWhereStatus(String status, Date startDate,Date endDate);


    @Query(value = "SELECT pdf_path FROM application WHERE id = ?1 ",nativeQuery = true)
    String findCombinePathByCurrentDocumentId(Long id);

    @Query(value = " SELECT \n" +
            "    a.ce1 AS toUser,\n" +
            "    COUNT(a.id) AS totalApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' AND ms.to_user = a.ce1 THEN 1 ELSE 0  END) AS pendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' AND ms.to_user = a.ce1 THEN 1  ELSE 0  END) AS inProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' AND ms.to_user = a.ce1 THEN 1 ELSE 0  END) AS approvedApplications,\n" +
            "    SUM(CASE   WHEN ms.status = 'REJECTED' AND ms.to_user = a.ce1 THEN 1  ELSE 0  END) AS rejectedApplications\n" +
            "FROM \n" +
            "    application a\n" +
            "LEFT JOIN \n" +
            "    movement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "    a.ce1 IN (:listOfCe2)\n" +
            "    AND a.date between  :startDate AND :endDate  \n" +
            "GROUP BY \n" +
            "    a.ce1; ",nativeQuery = true)
    List<Map<String, Object>> getApplicationCountsByCe2Users(@Param("listOfCe2") List<String> listOfCe2,
                                                             @Param("startDate") Date startDate,
                                                             @Param("endDate") Date endDate);

    @Query(value = "SELECT \n" +
            "    a.se AS toUser,\n" +
            "    COALESCE(COUNT(a.id), 0) AS totalApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'PENDING' AND ms.to_user = a.se THEN 1 ELSE 0  END), 0) AS pendingApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'IN_PROGRESS' AND ms.to_user = a.se THEN 1  ELSE 0  END), 0) AS inProgressApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'APPROVED' AND ms.to_user = a.se THEN 1 ELSE 0  END), 0) AS approvedApplications,\n" +
            "    COALESCE(SUM(CASE   WHEN ms.status = 'REJECTED' AND ms.to_user = a.se THEN 1  ELSE 0  END), 0) AS rejectedApplications\n" +
            "FROM \n" +
            "    application a\n" +
            "LEFT JOIN \n" +
            "    movement_status ms ON ms.application_id = a.id \n" +
            "WHERE \n" +
            "    a.se IN (:listOfSe)  \n" +
            "    AND a.date  between :startDate AND :endDate \n" +
            "GROUP BY \n" +
            "    a.se \n" +
            "HAVING \n" +
            "    a.se IS NOT NULL;",nativeQuery = true)
    List<Map<String, Object>> getApplicationCountsBySEUsers(@Param("listOfSe") List<String> listOfSe,
                                                            @Param("startDate") Date startDate,
                                                            @Param("endDate") Date endDate);


    @Query(value = "SELECT \n" +
            "    a.ce2 AS toUser,\n" +
            "    COUNT(a.id) AS totalApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' AND ms.to_user = a.ce2 THEN 1 ELSE 0  END) AS pendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' AND ms.to_user = a.ce2 THEN 1  ELSE 0  END) AS inProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' AND ms.to_user = a.ce2 THEN 1 ELSE 0  END) AS approvedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' AND ms.to_user = a.ce2 THEN 1  ELSE 0  END) AS rejectedApplications\n" +
            "FROM \n" +
            "    application a\n" +
            "LEFT JOIN \n" +
            "    movement_status ms ON ms.application_id = a.id \n" +
            "WHERE \n" +
            "    a.ce2 IN (:listOfCe1) \n" +
            "    AND a.date between :startDate AND :endDate \n" +
            "GROUP BY \n" +
            "    a.ce2;",nativeQuery = true)
    List<Map<String, Object>> getApplicationCountsByCe2Dashboard(@Param("listOfCe1")  List<String> listOfCe1,
                                                                 @Param("startDate") Date startDate,
                                                                 @Param("endDate") Date endDate);


    @Query(value = "SELECT \n" +
            "    a.se AS toUser,\n" +
            "    COALESCE(COUNT(a.id), 0) AS totalApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'PENDING' AND ms.to_user = a.se THEN 1 ELSE 0  END), 0) AS pendingApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'IN_PROGRESS' AND ms.to_user = a.se THEN 1  ELSE 0  END), 0) AS inProgressApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'APPROVED' AND ms.to_user = a.se THEN 1 ELSE 0  END), 0) AS approvedApplications,\n" +
            "    COALESCE(SUM(CASE   WHEN ms.status = 'REJECTED' AND ms.to_user = a.se THEN 1  ELSE 0  END), 0) AS rejectedApplications\n" +
            "FROM \n" +
            "    application a\n" +
            "LEFT JOIN \n" +
            "    movement_status ms ON ms.application_id = a.id \n" +
            "WHERE \n" +
            "    a.se IN (:listOfCe1)  \n" +
            "    AND a.date  between :startDate AND :endDate \n" +
            "GROUP BY \n" +
            "    a.se \n" +
            "HAVING \n" +
            "    a.se IS NOT NULL;",nativeQuery = true)
    List<Map<String, Object>> getApplicationCountsBySeDashboard( @Param("listOfCe1") List<String> listOfCe1,
                                                                @Param("startDate") Date startDate,
                                                                @Param("endDate") Date endDate);


    @Query(value = "SELECT \n" +
            "    a.ee AS toUser,\n" +
            "    COALESCE(COUNT(a.id), 0) AS totalApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'PENDING' AND ms.to_user = a.ee THEN 1 ELSE 0  END), 0) AS pendingApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'IN_PROGRESS' AND ms.to_user = a.ee THEN 1  ELSE 0  END), 0) AS inProgressApplications,\n" +
            "    COALESCE(SUM(CASE WHEN ms.status = 'APPROVED' AND ms.to_user = a.ee THEN 1 ELSE 0  END), 0) AS approvedApplications,\n" +
            "    COALESCE(SUM(CASE   WHEN ms.status = 'REJECTED' AND ms.to_user = a.ee THEN 1  ELSE 0  END), 0) AS rejectedApplications\n" +
            "FROM \n" +
            "    application a\n" +
            "LEFT JOIN \n" +
            "    movement_status ms ON ms.application_id = a.id \n" +
            "WHERE \n" +
            "    a.ee IN (:listOfCe1)  \n" +
            "    AND a.date  between :startDate AND :endDate\n" +
            "GROUP BY \n" +
            "    a.ee\n" +
            "HAVING \n" +
            "    a.ee IS NOT NULL;",nativeQuery = true)
    List<Map<String, Object>> getApplicationCountsByEEDashboard(@Param("listOfCe1")  List<String> listOfCe1,
                                                                @Param("startDate") Date startDate,
                                                                @Param("endDate") Date endDate);


    @Query(value = "            SELECT\n" +
            "                            n.noc_name,\n" +
            "                            COALESCE(s.approved_count, 0) AS approved_count,\n" +
            "                            COALESCE(s.pending_count, 0) AS pending_count,\n" +
            "                            COALESCE(s.rejected_count, 0) AS rejected_count,\n" +
            "                             \n" +
            "                            COALESCE(s.process_count, 0) AS process_count\n" +
            "                        FROM\n" +
            "                            noc_list n\n" +
            "                        LEFT JOIN \n" +
            "                            (SELECT \n" +
            "                                 a.applied_for, \n" +
            "                                 SUM(CASE WHEN s.status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\n" +
            "                                 SUM(CASE WHEN s.status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count,\n" +
            "                                 SUM(CASE WHEN s.status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count, \n" +
            "                                 SUM(CASE WHEN s.status = 'IN_PROCESS' THEN 1 ELSE 0 END) AS process_count\n" +
            "                             FROM \n" +
            "                                 application a\n" +
            "                             JOIN \n" +
            "                                 movement_status s ON a.id = s.application_id\n" +
            "                             WHERE \n" +
            "                                  s.date BETWEEN  :startDate AND :endDate \n" +
            "                             GROUP BY \n" +
            "                                 a.applied_for\n" +
            "                            ) s\n" +
            "                        ON \n" +
            "                            n.noc_name = s.applied_for;",nativeQuery = true)
    List<Map<String, Object>> fetchTheTotalCountOfGraphAdmin(  @Param("startDate") Date startDate,
                                                               @Param("endDate") Date endDate);


    @Query(value="SELECT \n" +
            "    u.login_id AS ce1,\n" +
            "     COUNT(a.id) AS totalApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS totalInProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS totalIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' THEN 1 ELSE 0 END) AS gasPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS gasPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS gasPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS gasPipeApprovedApplications,   \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS gasPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS gasPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' THEN 1 ELSE 0 END) AS pulPuliyaTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS pulPuliyaPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS pulPuliyaInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS pulPuliyaApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS pulPuliyaRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS pulPuliyaIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' THEN 1 ELSE 0 END) AS waterPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS waterPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS waterPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS waterPipeApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS waterPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS waterPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' THEN 1 ELSE 0 END) AS roadConstructionTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS roadConstructionPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS roadConstructionInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS roadConstructionApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS roadConstructionRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS roadConstructionIssuedApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' THEN 1 ELSE 0 END) AS jalawantanTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS jalawantanPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS jalawantanInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS jalawantanApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS jalawantanRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS jalawantanIssuedApplications\n" +
            "\n" +
            "FROM \n" +
            "    usersspring u \n" +
            "LEFT JOIN \n" +
            "    application a  ON u.login_id = a.ce1 \n" +
            "LEFT JOIN\n" +
            "\tmovement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "u.role='ce1'\n" +
            "GROUP BY \n" +
            "    u.login_id\n" +
            "ORDER BY \n" +
            "    u.login_id;",nativeQuery = true)
    List<Map<String, Object>> fetchAllOfficerWiseReportForCE1();

    @Query(value="SELECT \n" +
            "    u.login_id as ce2,\n" +
            "     COUNT(a.id) AS totalApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS totalInProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS totalIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' THEN 1 ELSE 0 END) AS gasPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS gasPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS gasPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS gasPipeApprovedApplications,   \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS gasPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS gasPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' THEN 1 ELSE 0 END) AS pulPuliyaTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS pulPuliyaPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS pulPuliyaInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS pulPuliyaApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS pulPuliyaRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS pulPuliyaIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' THEN 1 ELSE 0 END) AS waterPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS waterPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS waterPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS waterPipeApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS waterPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS waterPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' THEN 1 ELSE 0 END) AS roadConstructionTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS roadConstructionPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS roadConstructionInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS roadConstructionApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS roadConstructionRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS roadConstructionIssuedApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' THEN 1 ELSE 0 END) AS jalawantanTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS jalawantanPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS jalawantanInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS jalawantanApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS jalawantanRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS jalawantanIssuedApplications\n" +
            "\n" +
            "FROM \n" +
            "    usersspring u \n" +
            "LEFT JOIN \n" +
            "    application a  ON u.login_id = a.ce2 \n" +
            "LEFT JOIN\n" +
            "\tmovement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "u.role='ce2'\n" +
            "GROUP BY \n" +
            "    u.login_id\n" +
            "ORDER BY \n" +
            "    u.login_id;",nativeQuery = true)
    List<Map<String, Object>> fetchAllOfficerReportForCeII();


    @Query(value = "SELECT \n" +
            "    u.login_id as se,\n" +
            "     COUNT(a.id) AS totalApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS totalInProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS totalIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' THEN 1 ELSE 0 END) AS gasPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS gasPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS gasPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS gasPipeApprovedApplications,   \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS gasPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS gasPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' THEN 1 ELSE 0 END) AS pulPuliyaTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS pulPuliyaPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS pulPuliyaInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS pulPuliyaApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS pulPuliyaRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS pulPuliyaIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' THEN 1 ELSE 0 END) AS waterPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS waterPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS waterPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS waterPipeApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS waterPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS waterPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' THEN 1 ELSE 0 END) AS roadConstructionTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS roadConstructionPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS roadConstructionInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS roadConstructionApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS roadConstructionRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS roadConstructionIssuedApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' THEN 1 ELSE 0 END) AS jalawantanTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS jalawantanPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS jalawantanInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS jalawantanApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS jalawantanRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS jalawantanIssuedApplications\n" +
            "\n" +
            "FROM \n" +
            "    usersspring u \n" +
            "LEFT JOIN \n" +
            "    application a  ON u.login_id = a.se \n" +
            "LEFT JOIN\n" +
            "\tmovement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "u.role='se'\n" +
            "GROUP BY \n" +
            "    u.login_id\n" +
            "ORDER BY \n" +
            "    u.login_id;",nativeQuery = true)
    List<Map<String, Object>> fetchOfficerReportForSE();


    @Query(value="SELECT \n" +
            "    u.login_id as ee,\n" +
            "     COUNT(a.id) AS totalApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS totalInProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS totalIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' THEN 1 ELSE 0 END) AS gasPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS gasPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS gasPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS gasPipeApprovedApplications,   \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS gasPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS gasPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' THEN 1 ELSE 0 END) AS pulPuliyaTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS pulPuliyaPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS pulPuliyaInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS pulPuliyaApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS pulPuliyaRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS pulPuliyaIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' THEN 1 ELSE 0 END) AS waterPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS waterPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS waterPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS waterPipeApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS waterPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS waterPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' THEN 1 ELSE 0 END) AS roadConstructionTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS roadConstructionPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS roadConstructionInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS roadConstructionApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS roadConstructionRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS roadConstructionIssuedApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' THEN 1 ELSE 0 END) AS jalawantanTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS jalawantanPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS jalawantanInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS jalawantanApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS jalawantanRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS jalawantanIssuedApplications\n" +
            "\n" +
            "FROM \n" +
            "    usersspring u \n" +
            "LEFT JOIN \n" +
            "    application a  ON u.login_id = a.ee  \n" +
            "LEFT JOIN\n" +
            "\tmovement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "u.role='ee'\n" +
            "GROUP BY \n" +
            "    u.login_id\n" +
            "ORDER BY \n" +
            "    u.login_id;",nativeQuery = true)
    List<Map<String, Object>> fetchOfficerReportForExecutiveEngineer();

    @Query(value = "SELECT ce1 FROM application WHERE id= ?1 ;",nativeQuery = true)
    String fetchCe1NameWhereAppId(String appId);

    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_details ud ON a.user_id = ud.id " +
            "WHERE a.spc_id = ?1 AND a.transfer_status = 'FOR_ISSUANCE'; ",nativeQuery = true)
    List<Map<String, Object>> fetchApplicationDetailsBySpecialId(String specialId);

    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_details ud ON a.user_id = ud.id " +
            "WHERE a.spc_id = ?1 AND a.transfer_status = 'FOR_ISSUANCE'; ",nativeQuery = true)
    Application fetchApplicationBySpecialId(String specialId);

    @Query(value = "SELECT pdf_path FROM certificate WHERE special_id = ?1;",nativeQuery = true)
    String findCertificatePathBySpecialId(String id);

    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id WHERE a.ce1 = ?1 AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataByCe1(String username,Date startDate,Date endDate);

    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id WHERE a.ce2 = ?1 AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataByCe2(String username,Date startDate,Date endDate);

    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id WHERE a.se = ?1 AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataBySe(String username,Date startDate,Date endDate);

    @Query(value = "SELECT a.*, ud.agency_name FROM application a JOIN user_Details ud ON a.user_id = ud.id WHERE a.ee = ?1 AND a.date BETWEEN ?2 AND ?3 ;", nativeQuery = true)
    List<Map<String, Object>> findAllApplicationDataByEe(String username,Date startDate,Date endDate);

    @Query(value = "SELECT \n" +
            "    u.login_id AS ce2,\n" +
            "    COUNT(a.id) AS totalApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS totalInProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS totalIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' THEN 1 ELSE 0 END) AS gasPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS gasPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS gasPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS gasPipeApprovedApplications,   \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS gasPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS gasPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' THEN 1 ELSE 0 END) AS pulPuliyaTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS pulPuliyaPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS pulPuliyaInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS pulPuliyaApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS pulPuliyaRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS pulPuliyaIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' THEN 1 ELSE 0 END) AS waterPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS waterPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS waterPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS waterPipeApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS waterPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS waterPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' THEN 1 ELSE 0 END) AS roadConstructionTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS roadConstructionPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS roadConstructionInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS roadConstructionApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS roadConstructionRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS roadConstructionIssuedApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' THEN 1 ELSE 0 END) AS jalawantanTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS jalawantanPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS jalawantanInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS jalawantanApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS jalawantanRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS jalawantanIssuedApplications\n" +
            "\n" +
            "FROM \n" +
            "    usersspring u \n" +
            "LEFT JOIN \n" +
            "    application a ON u.login_id = a.ce2  \n" +
            "LEFT JOIN\n" +
            "    movement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "    u.role = 'ce2'\n" +
            "    AND u.login_id IN (:listOfCe2) -- Replace this list with your specific users\n" +
            "GROUP BY \n" +
            "    u.login_id\n" +
            "ORDER BY \n" +
            "    u.login_id;\n",nativeQuery = true)
    List<Map<String, Object>> fetchCE2forParticularUsername(@Param("listOfCe2")  List<String> listOfCe2);


    @Query(value = "SELECT \n" +
            "    u.login_id AS se,\n" +
            "    COUNT(a.id) AS totalApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS totalInProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS totalIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' THEN 1 ELSE 0 END) AS gasPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS gasPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS gasPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS gasPipeApprovedApplications,   \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS gasPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS gasPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' THEN 1 ELSE 0 END) AS pulPuliyaTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS pulPuliyaPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS pulPuliyaInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS pulPuliyaApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS pulPuliyaRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS pulPuliyaIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' THEN 1 ELSE 0 END) AS waterPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS waterPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS waterPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS waterPipeApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS waterPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS waterPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' THEN 1 ELSE 0 END) AS roadConstructionTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS roadConstructionPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS roadConstructionInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS roadConstructionApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS roadConstructionRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS roadConstructionIssuedApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' THEN 1 ELSE 0 END) AS jalawantanTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS jalawantanPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS jalawantanInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS jalawantanApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS jalawantanRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS jalawantanIssuedApplications\n" +
            "\n" +
            "FROM \n" +
            "    usersspring u \n" +
            "LEFT JOIN \n" +
            "    application a ON u.login_id = a.se  \n" +
            "LEFT JOIN\n" +
            "    movement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "    u.role = 'se'\n" +
            "    AND u.login_id IN (:listOfSeByCE1) \n" +
            "GROUP BY \n" +
            "    u.login_id\n" +
            "ORDER BY \n" +
            "    u.login_id;",nativeQuery = true)
    List<Map<String, Object>> fetchListOfSeByCe1Name(@Param("listOfSeByCE1") List<String> listOfSeByCE1);


    @Query(value = "SELECT \n" +
            "    u.login_id AS ee,\n" +
            "    COUNT(a.id) AS totalApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN ms.status = 'PENDING' THEN 1 ELSE 0 END) AS totalPendingApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS totalInProgressApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS totalApprovedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS totalRejectedApplications,\n" +
            "    SUM(CASE WHEN ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS totalIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' THEN 1 ELSE 0 END) AS gasPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS gasPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS gasPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS gasPipeApprovedApplications,   \n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS gasPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'गैस पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS gasPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' THEN 1 ELSE 0 END) AS pulPuliyaTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS pulPuliyaPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS pulPuliyaInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS pulPuliyaApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS pulPuliyaRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पुल - पुलिया' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS pulPuliyaIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' THEN 1 ELSE 0 END) AS waterPipeTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS waterPipePendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS waterPipeInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS waterPipeApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS waterPipeRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'पेयजल पाइप लाइन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS waterPipeIssuedApplications,\n" +
            " \n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' THEN 1 ELSE 0 END) AS roadConstructionTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS roadConstructionPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS roadConstructionInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS roadConstructionApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS roadConstructionRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = 'मार्ग निर्माण' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS roadConstructionIssuedApplications,\n" +
            "    \n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' THEN 1 ELSE 0 END) AS jalawantanTotalApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'PENDING' THEN 1 ELSE 0 END) AS jalawantanPendingApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS jalawantanInProgressApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'APPROVED' THEN 1 ELSE 0 END) AS jalawantanApprovedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'REJECTED' THEN 1 ELSE 0 END) AS jalawantanRejectedApplications,\n" +
            "    SUM(CASE WHEN a.applied_for = ' जल आवंटन' AND ms.status = 'ISSUED' THEN 1 ELSE 0 END) AS jalawantanIssuedApplications\n" +
            "\n" +
            "FROM \n" +
            "    usersspring u \n" +
            "LEFT JOIN \n" +
            "    application a ON u.login_id = a.ee  \n" +
            "LEFT JOIN\n" +
            "    movement_status ms ON ms.application_id = a.id\n" +
            "WHERE \n" +
            "    u.role = 'ee'\n" +
            "    AND u.login_id IN (:listOfEEByCE1) -- Replace this list with your specific users\n" +
            "GROUP BY \n" +
            "    u.login_id",nativeQuery = true)
    List<Map<String, Object>> fetchListOfEEByCe1Name(@Param("listOfEEByCE1") List<String> listOfEEByCE1);


    @Query(value = "SELECT ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce1= ?2 and ms.status = ?1 and a.applied_for=?3 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForCe1StatusNocName(String status, String ce1, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce2= ?2 and ms.status = ?1 and a.applied_for= ?3  ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForCe2StatusNocName(String status, String ce2, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.se= ?1 and ms.status = ?2 and a.applied_for= ?3 ;" , nativeQuery = true)
    List<Map<String, Object>> fetchDataForSEStatusNOCName(String se, String status, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ee= ?1 and ms.status = ?2  and a.applied_for= ?3", nativeQuery = true)
    List<Map<String, Object>> fetchDataForEEStatusNOCName(String ee, String status, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce1 = ?1 and a.applied_for = ?2 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForCE1AndNocName(String ce1, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce2= ?1 and a.applied_for= ?2 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForCE2AndNocName(String ce2, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.se= ?1 and a.applied_for= ?2;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForSEAndNocName(String se, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ee= ?1 and a.applied_for= ?2 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForEEAndNocName(String ee, String nocName);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce1= ?1 and ms.status = ?2 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForCe1andStatus(String ce1, String status);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce2= ?1 and ms.status = ?2 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForCe2andStatus(String ce2, String status);


    @Query(value= "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.se= ?1 and ms.status = ?2 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForSEandStatus(String se, String status);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ee= ?1 and ms.status = ?2 ;",nativeQuery = true)
    List<Map<String, Object>> fetchDataForEEandStatus(String ee, String status);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce1= ?1 ",nativeQuery = true)
    List<Map<String, Object>> fetchDataForTotalOFCe1(String ce1);



    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ce2= ?1 ",nativeQuery = true)
    List<Map<String, Object>> fetchDataForTotalOFCe2(String ce2);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.se= ?1 ",nativeQuery = true)
    List<Map<String, Object>> fetchDataForTotalOFSE(String se);


    @Query(value = "SELECT ms.status,ud.agency_name,a.id,a.* FROM application a LEFT JOIN movement_status ms on a.id = ms.application_id\n" +
            "LEFT JOIN user_details ud on a.user_id = ud.id\n" +
            "where a.ee= ?1 ",nativeQuery = true)
    List<Map<String, Object>> fetchDataForTotalOFEE(String ee);


    @Query(value = "SELECT COUNT(*) FROM movement_status acs JOIN application a ON a.id = acs.application_id " +
            "WHERE acs.status = ?1 AND acs.date BETWEEN ?2 AND ?3 AND acs.date >= ?4 ", nativeQuery = true)
    Long getNoAction7DaysApplicationByFinYear(String status, Date startDate, Date endDate,Date sevenDaysAgo);


}
