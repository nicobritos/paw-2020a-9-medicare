package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

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
    @Column(name = "picture")
    private byte[] picture;

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

    public byte[] getPicture() {
        return this.picture;
    }

    public String getPictureAsHex() {
        return this.picture == null ? null : StringHelper.byteToHex(this.picture);
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof User;
    }
}
