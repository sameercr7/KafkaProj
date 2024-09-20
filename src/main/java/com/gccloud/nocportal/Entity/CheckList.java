package com.gccloud.nocportal.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "check_list")
public class CheckList extends AuditSuperClass{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String checkListTask;

    private String taskType;

    private String parentTaskId;

    private String parentId;

    private String submitType;

    private  String docName;

    @ManyToOne
    @JoinColumn(name = "noc_list_id")
    @JsonBackReference
    private NocList nocList;

    @OneToMany(mappedBy = "checkList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DocumentStatus> documentStatuses = new ArrayList<>();

}
