package me.lejo.app.vo;

import me.lejo.core.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;

public class PasswordCommand {

    private long id;
    @NotEmpty
    private String currentPassword;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;

    public PasswordCommand(User user) {
        id = user.getId();
    }

    public PasswordCommand() {
    }

    @AssertTrue(message = "{PasswordCommand.isPasswordConfirmed.message}")
    public boolean isValid() {
        return StringUtils.equals(password, confirmPassword);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
