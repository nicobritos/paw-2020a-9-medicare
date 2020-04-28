package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.List;
import java.util.Set;

public interface StaffService extends GenericSearchableService<Staff, Integer> {
    List<Staff> findBy(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities);

    List<Staff> findBy(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities, int page);

    List<Staff> findBy(Set<String> names, Set<String> surnames, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities, int page);

    void addStaffSpecialty(Staff staff, StaffSpecialty staffSpecialty);

    void addStaffSpecialties(Staff staff, Set<StaffSpecialty> staffSpecialties);
}
