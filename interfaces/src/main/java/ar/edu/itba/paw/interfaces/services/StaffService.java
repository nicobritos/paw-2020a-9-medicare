package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Collection;
import java.util.Set;

public interface StaffService extends GenericSearchableService<Staff, Integer> {
    Set<Staff> findBy(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities);

    Set<Staff> findBy(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities, int page);

    Set<Staff> findBy(Set<String> names, Set<String> surnames, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities, int page);

    void addStaffSpecialty(Staff staff, StaffSpecialty staffSpecialty);

    void addStaffSpecialties(Staff staff, Set<StaffSpecialty> staffSpecialties);
}
