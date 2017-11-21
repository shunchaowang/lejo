package me.lejo.app.vo;

import me.lejo.core.domain.Role;
import org.hibernate.validator.constraints.NotEmpty;

public class RoleCommand {

    private long id;
    @NotEmpty
    private String name;

    public RoleCommand() {
    }

    public RoleCommand(Role role) {
        id = role.getId();
        name = role.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
