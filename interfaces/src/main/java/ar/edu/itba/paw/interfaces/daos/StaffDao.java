package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Collection;
import java.util.Set;

public interface StaffDao extends GenericSearchableDao<Staff, Integer> {
    Set<Staff> findBySurname(String surname);

    Set<Staff> findByOffice(Collection<Office> offices);

    Set<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameAndSurname(String name, String surname);

    Set<Staff> findByNameAndOffice(String name, Collection<Office> offices);

    Set<Staff> findByNameAndStaffSpecialties(String name, Collection<StaffSpecialty> staffSpecialties);

    Set<Staff> findBySurnameAndOffice(String surname, Collection<Office> offices);

    Set<Staff> findBySurnameAndStaffSpecialties(String surname, Collection<StaffSpecialty> staffSpecialties);

    Set<Staff> findByOfficeAndStaffSpecialties(Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);

    Set<Staff> findBySurnameOfficeAndStaffSpecialties(String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameOfficeAndStaffSpecialties(String name, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameSurnameAndStaffSpecialties(String name, String surname, Collection<StaffSpecialty> staffSpecialties);

    Set<Staff> findByNameSurnameAndOffice(String name, String surname, Collection<Office> offices);

    Set<Staff> findByNameSurnameOfficeAndStaffSpecialities(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);
}
