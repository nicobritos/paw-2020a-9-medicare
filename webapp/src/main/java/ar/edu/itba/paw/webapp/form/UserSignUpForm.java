package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class UserSignUpForm {
    @Email(message = "Debe escribir un mail válido")
    @NotEmpty(message = "Debe escribir un mail válido")
    private String email;
    @NotEmpty(message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;
    @NotEmpty(message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String repeatPassword;
    @NotEmpty(message = "El nombre debe tener entre 8 y 100 caracteres")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    private String firstName;
    @NotEmpty(message = "El nombre debe tener entre 8 y 100 caracteres")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    private String surname;

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

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User getAsUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setFirstName(this.firstName);
        user.setSurname(this.surname);
        return user;
    }

    public String getRepeatPassword() {
        return this.repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
