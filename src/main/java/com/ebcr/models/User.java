package com.ebcr.models;

import com.ebcr.audits.InitiatorAudit;
import com.ebcr.enums.Gender;
import com.ebcr.enums.Role;
import com.ebcr.filehandling.File;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}), @UniqueConstraint(columnNames = {"phone_number"}), @UniqueConstraint(columnNames = "national_id")})
@OnDelete(action = OnDeleteAction.CASCADE)
public class User extends InitiatorAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String userName;
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "national_id")
    private String nationalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "rejection_description")
    private String rejectionDescription;


    @JoinColumn(name = "profile_image_id")
    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private File profileImage;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<com.ebcr.models.Role> roles = new HashSet<>();

    @JsonIgnore
    @NotBlank
    @Column(name = "password")
    private String password;

    public User(String firstName, String lastName, String phoneNumber, String email, String nationalId, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationalId = nationalId;
        this.gender = gender;
    }

    public User(String firstName, String lastName, String phoneNumber, String email, String nationalId, Gender gender, Set<com.ebcr.models.Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationalId = nationalId;
        this.gender = gender;
        this.roles = roles;
    }

    public User(String firstName, String lastName, String phoneNumber, String email, String nationalId, Gender gender, Set<com.ebcr.models.Role> roles, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationalId = nationalId;
        this.gender = gender;
        this.roles = roles;
        this.password = password;
    }
    public Role getRole() {
        Optional<com.ebcr.models.Role> role = this.getRoles().stream().findFirst();
        Role theRole = null;

        if (role.isPresent())
            theRole = role.get().getName();
        return theRole;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
