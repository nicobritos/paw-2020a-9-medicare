package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Collection;

public interface StaffDao extends GenericSearchableDao<Staff, Integer> {
    Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties);
}
