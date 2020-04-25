package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.HashSet;
import java.util.Set;

@Table(name = "users", primaryKey = "users_id")
public class User extends GenericModel<User, Integer> {
    @Column(name = "email", required = true)
    private String email;
    @Column(name = "password", required = true)
    private String password;
    @Column(name = "first_name", required = true)
    private String firstName;
    @Column(name = "surname", required = true)
    private String surname;
    @OneToMany(name = "staff_id", className = Staff.class)
    private Set<Staff> staffs = new HashSet<>();
    @OneToMany(name = "patient_id", className = Patient.class)
    private Set<Patient> patients = new HashSet<>();

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Staff> getStaffs() {
        return this.staffs;
    }

    public Set<Patient> getPatients() {
        return this.patients;
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

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof User;
    }
}
