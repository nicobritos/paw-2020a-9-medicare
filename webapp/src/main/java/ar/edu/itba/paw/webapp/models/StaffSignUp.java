package ar.edu.itba.paw.webapp.models;

import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Collection;

public class StaffSignUp extends UserSignUp {
    private Locality locality;
    private Office office;
    private Integer registrationNumber;
    private Collection<StaffSpecialty> staffSpecialties;

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

    public Collection<StaffSpecialty> getStaffSpecialties() {
        return this.staffSpecialties;
    }

    public void setStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        this.staffSpecialties = staffSpecialties;
    }
}
