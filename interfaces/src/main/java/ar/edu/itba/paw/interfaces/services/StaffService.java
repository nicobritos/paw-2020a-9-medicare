package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;

import java.util.Collection;

public interface StaffService extends GenericSearchableService<Staff, Integer> {
    Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties);
}
