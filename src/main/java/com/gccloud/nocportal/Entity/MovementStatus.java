package com.gccloud.nocportal.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movement_status")
public class MovementStatus extends AuditSuperClass{

//    movement_status
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromUser; //The current approver/authority who forwarded or processed the application.
    private String toUser; // The next approver/authority the application is sent to.
    private String lastAction; //Details about the last action performed on the application (e.g., Approved, Rejected).
    private String status; //current status of the application in the workflow
    private String remark; //Comments or notes from the approver regarding the application.

    @Column(nullable = false)
    private String certificateIssued = "false"; // This column for checking if the certificate is issued or not for the current application

    @Column(nullable = false)
    private String reSubmission = "false"; // This column for checking if the agency has resubmitted the application or not

    private String timestamp;
    private Date date;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @ToString.Exclude // Exclude this field from toString method
    @EqualsAndHashCode.Exclude // Exclude this field from equals and hashCode methods
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;


    @OneToMany(mappedBy = "movementStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<LogsOfMovement> logsOfMovements;



}
