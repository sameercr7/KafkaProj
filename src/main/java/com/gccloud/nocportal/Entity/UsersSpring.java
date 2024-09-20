package com.gccloud.nocportal.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usersspring")
public class UsersSpring extends AuditSuperClass{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String hinLoginId;
    private String password;

    @Column(nullable = false, unique = true) // Ensure the username is unique and not null
    private String username;
    private String emailId;
    private String mobile;
    private String role; // Against User Type
    private String ce1;
    private String hindCe1;
    private String ce2;
    private String hindCe2;
    private String hod;
    private String se;
    private String hinSe;
    private String ee;
    private String hinEe;
    private String emailVerification;
    private String mobileVerification;
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;
    private String extra5;
    private String extra6;

    @Transient
    private MultipartFile image;
    private String imagePath;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @ToString.Exclude // Exclude this field from toString method
    @EqualsAndHashCode.Exclude // Exclude this field from equals and hashCode methods
    private UserDetails userDetails;

}
