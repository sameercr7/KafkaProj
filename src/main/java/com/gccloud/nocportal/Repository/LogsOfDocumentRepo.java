package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.LogsOfDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface LogsOfDocumentRepo extends JpaRepository<LogsOfDocument,Long> {

    @Query(value = "SELECT * FROM logs_of_document WHERE application_id =?1 ;",nativeQuery = true)
    List<LogsOfDocument> findByApplicationId(Long applicationId);

    @Query(value = "SELECT * FROM logs_of_document WHERE application_id =?1 ;",nativeQuery = true)
    List<LogsOfDocument>  findAllDataByApplicatioId(Long id);

    @Query(value = "  SELECT login_id AS to_user,  \n" +
            "           ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) AS avg_days_to_close,\n" +
            "           CASE\n" +
            "               WHEN COALESCE(AVG(DATEDIFF(completion_date, date)), 0) > 0 THEN \n" +
            "                   DENSE_RANK() OVER (ORDER BY ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) DESC)\n" +
            "               ELSE NULL\n" +
            "           END AS ranked\n" +
            "    FROM usersspring u \n" +
            "    LEFT JOIN application a ON u.login_id = a.ce2\n" +
            "    AND a.completion_date IS NOT NULL \n" +
            "    AND TRIM(a.completion_date) != ''\n" +
            "    WHERE u.role = 'ce2'\n" +
            "    GROUP BY login_id",nativeQuery = true)
    List<Map<String, Object>> fetchInteralDepartmentStatsForCe2();



    @Query(value = "  SELECT login_id AS to_user,  \n" +
            "           ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) AS avg_days_to_close,\n" +
            "           CASE\n" +
            "               WHEN COALESCE(AVG(DATEDIFF(completion_date, date)), 0) > 0 THEN \n" +
            "                   DENSE_RANK() OVER (ORDER BY ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) DESC)\n" +
            "               ELSE NULL\n" +
            "           END AS ranked\n" +
            "    FROM usersspring u \n" +
            "    LEFT JOIN application a ON u.login_id = a.se\n" +
            "    AND a.completion_date IS NOT NULL \n" +
            "    AND TRIM(a.completion_date) != ''\n" +
            "    WHERE u.role = 'se'\n" +
            "    GROUP BY login_id",nativeQuery = true)
    List<Map<String, Object>> fetchInternalDepartmentStatsForSE();


    @Query(value = "  SELECT login_id AS to_user,  \n" +
            "           ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) AS avg_days_to_close,\n" +
            "           CASE\n" +
            "               WHEN COALESCE(AVG(DATEDIFF(completion_date, date)), 0) > 0 THEN \n" +
            "                   DENSE_RANK() OVER (ORDER BY ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) DESC)\n" +
            "               ELSE NULL\n" +
            "           END AS ranked\n" +
            "    FROM usersspring u \n" +
            "    LEFT JOIN application a ON u.login_id = a.ee\n" +
            "    AND a.completion_date IS NOT NULL \n" +
            "    AND TRIM(a.completion_date) != ''\n" +
            "    WHERE u.role = 'ee'\n" +
            "    GROUP BY login_id",nativeQuery = true)
    List<Map<String, Object>> fetchInternalDepartmentStatsForEE();


    @Query(value = "  SELECT login_id AS to_user,  \n" +
            "           ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) AS avg_days_to_close,\n" +
            "           CASE\n" +
            "               WHEN COALESCE(AVG(DATEDIFF(completion_date, date)), 0) > 0 THEN \n" +
            "                   DENSE_RANK() OVER (ORDER BY ROUND(COALESCE(AVG(DATEDIFF(completion_date, date)), 0), 2) DESC)\n" +
            "               ELSE NULL\n" +
            "           END AS ranked\n" +
            "    FROM usersspring u \n" +
            "    LEFT JOIN application a ON u.login_id = a.ce1\n" +
            "    AND a.completion_date IS NOT NULL \n" +
            "    AND TRIM(a.completion_date) != ''\n" +
            "    WHERE u.role = 'ce1'\n" +
            "    GROUP BY login_id",nativeQuery = true)
    List<Map<String, Object>> fetchInteralDepartmentStatsForCe1();
}
