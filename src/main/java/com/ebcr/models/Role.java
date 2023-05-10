package com.ebcr.models;
import com.ebcr.audits.InitiatorAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role  extends InitiatorAudit {
    @Id
    @GeneratedValue(generator = "userUUID")
    @GenericGenerator(name = "userUUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private com.ebcr.enums.Role name;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public Role(com.ebcr.enums.Role name, String description) {
        this.name = name;
        this.description = description;
    }
}
