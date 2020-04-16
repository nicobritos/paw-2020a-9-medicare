package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Collection;

public interface StaffDao extends GenericSearchableDao<Staff, Integer> {
    Collection<Staff> findBySurname(String surname);

    Collection<Staff> findByOffice(Collection<Office> offices);

    Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties);

    Collection<Staff> findByNameAndSurname(String name, String surname);

    Collection<Staff> findByNameAndOffice(String name, Collection<Office> offices);

    Collection<Staff> findByNameAndStaffSpecialties(String name, Collection<StaffSpecialty> staffSpecialties);

    Collection<Staff> findBySurnameAndOffice(String surname, Collection<Office> offices);

    Collection<Staff> findBySurnameAndStaffSpecialties(String surname, Collection<StaffSpecialty> staffSpecialties);

    Collection<Staff> findByOfficeAndStaffSpecialties(Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);

    Collection<Staff> findBySurnameOfficeAndStaffSpecialties(String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);

    Collection<Staff> findByNameOfficeAndStaffSpecialties(String name, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);

    Collection<Staff> findByNameSurnameAndStaffSpecialties(String name, String surname, Collection<StaffSpecialty> staffSpecialties);

    Collection<Staff> findByNameSurnameAndOffice(String name, String surname, Collection<Office> offices);

    Collection<Staff> findByNameSurnameOfficeAndStaffSpecialities(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties);
}
