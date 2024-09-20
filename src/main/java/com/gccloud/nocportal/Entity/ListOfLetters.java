package com.gccloud.nocportal.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "list_of_letters") //Letter Details
public class ListOfLetters extends AuditSuperClass{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String letterType; //Approval Letter, Rejection Letter
    private String content; //The actual content of the letter
    private String issuedBy; //The engineer or authority who issued the letter.



    private String description;
    private String docPath;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = true)
    @JsonBackReference
    private Application application;

}
