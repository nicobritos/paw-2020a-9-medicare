package ar.edu.itba.paw.webapp.form.authentication;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserLoginForm {
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    private Boolean rememberMe;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getRememberMe() {
        return this.rememberMe == null ? false : this.rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
