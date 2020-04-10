package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Collection;

@Table(name = "staff", primaryKey = "staff_id")
public class Staff extends GenericModel<Integer> {
    @Column(name = "office_id", required = true)
    private int officeId;
    private Office office;
    @Column(name = "first_name", required = true)
    private String firstName;
    @Column(name = "surname", required = true)
    private String surname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "registration_number", required = true)
    private int registrationNumber;
    private Collection<StaffSpecialty> staffSpecialties;

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(Office office) {
        this.office = office;
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

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRegistrationNumber() {
        return this.registrationNumber;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public Collection<StaffSpecialty> getStaffSpecialties() {
        return this.staffSpecialties;
    }

    public void setStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        this.staffSpecialties = staffSpecialties;
    }
}
