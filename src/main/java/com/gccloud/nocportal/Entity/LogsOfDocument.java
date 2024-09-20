package com.gccloud.nocportal.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "logs_of_document")
public class LogsOfDocument extends AuditSuperClass{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String changeOn;
    private String dataChangeFrom; // Old Data
    private String dataChangeTo; // Updated Data (In Place Of Old Data)

    @ManyToOne(cascade = CascadeType.ALL,  optional = false)
    @JoinColumn(name = "document_status_id", nullable = true)
    @JsonBackReference
    private DocumentStatus documentStatus;


    @ManyToOne(cascade = CascadeType.ALL,  optional = false)
    @JoinColumn(name = "application_id", nullable = true)
    @JsonBackReference
    private Application application;

    @Column(columnDefinition = "LONGTEXT")
    private String  description;

    private String associationId;

    private  String remarks;

    private  String currStatus;

    @Transient
    private String document;

    private String dataField;

    @Transient
    private MultipartFile file;

    @Transient
    private String inputValue;

    @Transient
    private MultipartFile files;
}
