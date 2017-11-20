package me.lejo.core.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import java.util.Set;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 64, unique = true)
    @NonNull
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    @NonNull
    private boolean active;

    @Version
    private long version;

    // JoinTable makes Role is the owner of the relationship to Permission
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission_mapping",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
