package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.ListOfLetters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListOfLettersRepository extends JpaRepository<ListOfLetters,Long> {

    @Query(value = "SELECT * FROM list_of_letters WHERE application_id = ?1 ;",nativeQuery = true)
    List<ListOfLetters> findByApplicationId(String id);

    @Query(value = "SELECT doc_path FROM list_of_letters WHERE id =?1 ;",nativeQuery = true)
    String findPathByAppId(Long id);
}
