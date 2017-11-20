package me.lejo.core.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import java.util.Set;

@Entity
@Data
public class Permission {

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

    @OneToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    @ManyToMany(mappedBy = "permissions")
    private Set<User> users;
}
