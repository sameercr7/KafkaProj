package com.gccloud.nocportal.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "application") //Pdf Details
public class Application extends AuditSuperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appliedFor; //Applied For Which Type Of NOC From NOC List
    private String district;
    private String canal;
    private String admin;
    private String ce1;
    private String ce2;
    private String se;
    private String ee;
    private String timestamp;
    private Date date;

    private String noOfAttempts;
    private String agencyStatus;
    private String administrationStatus;
    private String hodStatus;
    private String subAdminStatus;
    private String deputyAdminStatus;
    private String adminStatus;
    private String ce1Status;
    private String ce2Status;
    private String seStatus;
    private String eeStatus;
    private Date completionDate;

//    private String description;
//    private String type;
//    private String  dataField;

    @Transient
    private List<MultipartFile> files; // Updated to handle multiple files


    @Column(columnDefinition = "LONGTEXT")
    private String pdfPath;

    @Column(columnDefinition = "LONGTEXT")
    private String document; // Store JSON data here


    @Column(nullable = false)
    private String transferStatus = "FORWARDING"; // Set This Initially Forwarding


    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<NocList> nocLists;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude // Exclude this field from toString method
    @EqualsAndHashCode.Exclude // Exclude this field from equals and hashCode methods
    private MovementStatus movementStatus;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DocumentStatus> documentStatus = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LogsOfDocument> logsOfDocuments = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude // Exclude this field from equals and hashCode methods
    private List<ListOfLetters> listOfLetters;


    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LogsOfMovement> logsOfMovements = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference
    private UserDetails userDetails;

    @Transient
    private String attachedDocumentType;

    private String spc_Id;

}
