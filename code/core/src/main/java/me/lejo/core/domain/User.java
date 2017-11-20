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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 64, unique = true)
    @NonNull
    private String username;

    @Column(nullable = false, length = 128)
    @NonNull
    private String password;

    @Column(nullable = false, length = 32)
    @NonNull
    private String firstName;

    @Column(nullable = false, length = 32)
    @NonNull
    private String lastName;

    @Lob
    private byte[] profileImage;

    @Column
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Column(nullable = false)
    @NonNull
    private boolean active;

    @Version
    private long version;

    // JoinTable makes User is the owner of the relationship to Permission
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // JoinTable makes User is the owner of the relationship to Permission
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_permission_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
