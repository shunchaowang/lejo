package me.lejo.core.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Set;

@Entity
@Data
public class MenuCategory {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 64, unique = true)
    @NonNull
    private String name;

    @Column(nullable = false)
    @NonNull
    private int index;

    @Version
    private long version;

    @OneToMany(mappedBy = "menuCategory")
    private Set<MenuItem> menuItems;
}
