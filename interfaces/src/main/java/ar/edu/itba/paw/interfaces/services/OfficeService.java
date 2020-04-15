package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.*;

import java.util.Collection;

public interface OfficeService extends GenericSearchableService<Office, Integer> {
    Collection<Office> findByCountry(Country country);

    Collection<Office> findByProvince(Province province);

    Collection<Office> findByLocality(Locality locality);

    void addStaff(Office office, Staff staff);

    void addStaffs(Office office, Collection<Staff> staffs);
}
