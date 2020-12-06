package ar.edu.itba.paw.webapp.models;

import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.DoctorSpecialty;

import java.util.Collection;

public class DoctorSignUp extends UserSignUp {
    private Locality locality;
    private Office office;
    private Integer registrationNumber;
    private Collection<DoctorSpecialty> doctorSpecialties;

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

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Collection<DoctorSpecialty> getDoctorSpecialties() {
        return this.doctorSpecialties;
    }

    public void setDoctorSpecialties(Collection<DoctorSpecialty> doctorSpecialties) {
        this.doctorSpecialties = doctorSpecialties;
    }
}
