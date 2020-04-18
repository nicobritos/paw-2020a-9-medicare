package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.*;

import java.util.Collection;
import java.util.Set;

public interface OfficeService extends GenericSearchableService<Office, Integer> {
    Set<Office> findByCountry(Country country);

    Set<Office> findByProvince(Province province);

    Set<Office> findByLocality(Locality locality);

    void addStaff(Office office, Staff staff);

    void addStaffs(Office office, Collection<Staff> staffs);
}
