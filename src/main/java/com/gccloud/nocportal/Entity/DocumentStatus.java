package com.gccloud.nocportal.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "document_status") //Data As Per Checklist
public class DocumentStatus extends AuditSuperClass{
//document_status
//    CurrentDocument
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String associationId;

    @Column(columnDefinition = "LONGTEXT")
    private String  description;

    private String dataField;


    @Transient
    private String inputValue;

    @Transient
    private MultipartFile file;

    @Transient
    private String document;

    private  String remarks;


    private  String currStatus;



    @Transient
    private MultipartFile files;

    @ManyToOne(cascade = CascadeType.ALL,  optional = false)
    @JoinColumn(name = "application_id", nullable = true)
    @JsonBackReference
    private Application application;

    @OneToMany(mappedBy = "documentStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LogsOfDocument> logsOfDocuments;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "check_list_id")
    @JsonBackReference
    private CheckList checkList;




}
