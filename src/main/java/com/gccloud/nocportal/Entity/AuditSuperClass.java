package com.gccloud.nocportal.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Data
@EnableJpaAuditing
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditSuperClass {

    @Column(name = "creation_date", updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private String creationDate;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "last_modified_date", updatable = false)
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private String lastModificationDate;

    @Column(name = "last_modified_by", updatable = false)
    @LastModifiedBy
    private String lastModifiedBy;

}
