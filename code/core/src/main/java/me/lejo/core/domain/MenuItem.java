package me.lejo.core.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
@Data
public class MenuItem {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 64, unique = true)
    @NonNull
    private String name;

    @Column(nullable = false, length = 128)
    @NonNull
    private String target;

    @Column(nullable = false)
    @NonNull
    private int index;

    @Version
    private long version;

    // unidirectional relation to MenuCategory
    @ManyToOne
    @Column(name = "menu_category_id")
    private MenuCategory menuCategory;
}
