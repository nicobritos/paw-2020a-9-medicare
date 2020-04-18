package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Set;

public interface StaffService extends GenericSearchableService<Staff, Integer> {
    Set<Staff> findBySurname(String surname);

    Set<Staff> findByOffice(Set<Office> offices);

    Set<Staff> findByStaffSpecialties(Set<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameAndSurname(String name, String surname);

    Set<Staff> findByNameAndOffice(String name, Set<Office> offices);

    Set<Staff> findByNameAndStaffSpecialties(String name, Set<StaffSpecialty> staffSpecialties);

    Set<Staff> findBySurnameAndOffice(String surname, Set<Office> offices);

    Set<Staff> findBySurnameAndStaffSpecialties(String surname, Set<StaffSpecialty> staffSpecialties);

    Set<Staff> findByOfficeAndStaffSpecialties(Set<Office> offices, Set<StaffSpecialty> staffSpecialties);

    Set<Staff> findBySurnameOfficeAndStaffSpecialties(String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameOfficeAndStaffSpecialties(String name, Set<Office> offices, Set<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameSurnameAndStaffSpecialties(String name, String surname, Set<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameSurnameAndOffice(String name, String surname, Set<Office> offices);

    Set<Staff> findByNameSurnameOfficeAndStaffSpecialities(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties);

    void addStaffSpecialty(Staff staff, StaffSpecialty staffSpecialty);

    void addStaffSpecialties(Staff staff, Set<StaffSpecialty> staffSpecialties);
}
