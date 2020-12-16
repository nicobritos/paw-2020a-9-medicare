package ar.edu.itba.paw.webapp.models;

import ar.edu.itba.paw.models.Locality;

public class DoctorSignUp extends UserSignUp {
    private Locality locality;
    private Integer registrationNumber;

    public Integer getRegistrationNumber() {
        return this.registrationNumber;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Locality getLocality() {
        return this.locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }
}
