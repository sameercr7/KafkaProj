package com.gccloud.nocportal.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "noc_list")
public class NocList extends AuditSuperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nocName;
    private String description; //A detailed description of the NOC type.

    private String cardsIconEndPoint;
    @OneToMany(mappedBy = "nocList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CheckList> checkLists;


    @ManyToOne
    @JoinColumn(name = "application_id", nullable = true)
    @JsonBackReference
    private Application application;

}
