package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrentDocumentRepo extends JpaRepository<DocumentStatus,Long> {


    @Query(value = "SELECT data_field FROM document_status WHERE id =?1 ;",nativeQuery = true)
    String findPathByCurrentDocumentId(Long id);

    @Query(value = "SELECT * FROM document_status WHERE application_id =?1 ;",nativeQuery = true)
    List<DocumentStatus> findByApplicationId(Long applicationId);

    @Query(value = "SELECT * FROM document_status WHERE application_id =?1 ;",nativeQuery = true)
    List<DocumentStatus> findAllDataByApplicatioId(Long id);
}
