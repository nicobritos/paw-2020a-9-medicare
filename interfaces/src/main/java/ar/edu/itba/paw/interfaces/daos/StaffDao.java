package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Collection;
import java.util.List;

public interface StaffDao extends GenericSearchableDao<Staff, Integer> {
    /**
     *
     * @param name if not null nor empty String, the search will filter the list by staff's name that
     *             contains the string (ignoring case)
     * @param surname if not null nor empty String, the search will filter the list by staff's surname that
     *                contains the string (ignoring case)
     * @param offices if not null nor empty collection, the search will include only those staffs
     *                that work for any of the offices in the collection
     * @param staffSpecialties if not null nor empty collection, the search will include only those staffs
     *                         that has any of the specialties in the collection
     * @return the filtered set
     */
    List<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities);

    List<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int pageSize);

    List<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int pageSize);

    List<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities);

    void setOffice(Staff staff, Office office);

    void addStaffSpecialties(Staff staff, Collection<StaffSpecialty> staffSpecialties);

    void addStaffSpecialty(Staff staff, StaffSpecialty staffSpecialty);
}
