package com.gccloud.nocportal.Repository;

import com.gccloud.nocportal.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    @Query(value = "SELECT upload_image_path FROM feedback WHERE id =?1 ;",nativeQuery = true)
    String findPathByfeedbackId(Long id);
}
