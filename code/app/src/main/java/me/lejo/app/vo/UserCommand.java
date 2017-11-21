package me.lejo.app.vo;

import me.lejo.core.domain.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserCommand {

    private long id;
    @NotEmpty
    @Email
    private String username;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String description;
    private String dateCreated;
    private String lastUpdated;

    private List<RoleCommand> roles = new ArrayList<>();

    public UserCommand(User user) {
        id = user.getId();
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        description = user.getDescription();
        Locale locale = LocaleContextHolder.getLocale();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        dateCreated = dateFormat.format(user.getDateCreated());
        if (user.getLastUpdated() != null) {
            lastUpdated = dateFormat.format(user.getLastUpdated());
        }

        user.getRoles().forEach(role -> {
            RoleCommand command = new RoleCommand(role);
            roles.add(command);
        });
    }

    public UserCommand() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<RoleCommand> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleCommand> roles) {
        this.roles = roles;
    }
}
