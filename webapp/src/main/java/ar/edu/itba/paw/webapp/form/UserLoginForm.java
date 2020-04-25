package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserLoginForm {
    @Email(message = "Debe escribir un mail válido")
    @NotEmpty(message = "Debe escribir un mail válido")
    private String email;
    @NotEmpty(message = "Debe escribir una contraseña")
    private String password;

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
}
