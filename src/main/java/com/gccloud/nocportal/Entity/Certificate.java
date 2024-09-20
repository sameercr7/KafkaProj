package com.gccloud.nocportal.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "certificate")
public class Certificate extends AuditSuperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String specialId;

    @Transient
    private MultipartFile pdf;

    private String pdfPath;
    @Transient
    private String nocName;
    @Transient
    private String agencyName;


}
