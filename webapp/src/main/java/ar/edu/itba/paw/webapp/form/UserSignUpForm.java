package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class UserSignUpForm {
    @Email
    @NotEmpty
    private String email;
    @Size(min = 8, max = 100)
    private String password;
    @Size(min = 8, max = 100)
    private String repeatPassword;
    @Size(min = 2, max = 20)
    private String firstName;
    @Size(min = 2, max = 20)
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

    public String getRepeatPassword() {
        return this.repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
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
}
