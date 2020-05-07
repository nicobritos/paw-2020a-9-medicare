package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;
import org.joda.time.DateTime;

@Table(name = "users", primaryKey = "users_id")
public class User extends GenericModel<Integer> {
    @Column(name = "email", required = true)
    private String email;
    @Column(name = "password", required = true)
    private String password;
    @Column(name = "first_name", required = true)
    private String firstName;
    @Column(name = "surname", required = true)
    private String surname;
    @Column(name = "verified")
    private Boolean verified = false;
    @Column(name = "token")
    private String token;
    @Column(name = "token_created_date")
    private DateTime tokenCreatedDate;

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

    public Boolean getVerified() {
        return this.verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDisplayName() {
        return this.firstName + " " + this.surname;
    }

    public DateTime getTokenCreatedDate() {
        return this.tokenCreatedDate;
    }

    public void setTokenCreatedDate(DateTime tokenCreatedDate) {
        this.tokenCreatedDate = tokenCreatedDate;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof User;
    }
}
