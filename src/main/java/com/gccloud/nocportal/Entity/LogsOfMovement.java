package com.gccloud.nocportal.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "logs_of_movement")
public class LogsOfMovement extends AuditSuperClass{
//logs_of_files
//    LogsOfFile
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromDepartmentUser;
    private String toDepartmentUser;

    private String timestamp;
    private Date Date;
    private String transferRemark;
    private String transferStatus;

    @ManyToOne
    @JoinColumn(name = "movement_status_id", nullable = true)
    @ToString.Exclude
    @JsonBackReference
    private MovementStatus movementStatus;

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = true)
    @ToString.Exclude
    @JsonBackReference
    private Application application;

}
