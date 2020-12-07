package ar.edu.itba.paw.webapp.models;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;

import java.util.Collection;

public class UserMe {
    private User user;
    private Collection<Patient> patients;
    private Collection<Doctor> doctors;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Collection<Patient> patients) {
        this.patients = patients;
    }

    public Collection<Doctor> getDoctors() {
        return this.doctors;
    }

    public void setDoctors(Collection<Doctor> doctors) {
        this.doctors = doctors;
    }
}
