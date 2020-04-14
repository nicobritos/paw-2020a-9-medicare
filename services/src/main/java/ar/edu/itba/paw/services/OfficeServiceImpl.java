package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.interfaces.services.OfficeService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OfficeServiceImpl extends GenericSearchableServiceImpl<OfficeDao, Office, Integer> implements OfficeService {
    @Autowired
    private OfficeDao repository;

    @Override
    public void addStaff(Office office, Staff staff) {
        office.getStaffs().add(staff);
        this.repository.update(office);
    }

    @Override
    public void addStaffs(Office office, Collection<Staff> staffs) {
        office.getStaffs().addAll(staffs);
        this.repository.update(office);
    }

    @Override
    public Collection<Office> findByCountry(Country country) {
        return this.getRepository().findByCountry(country);
    }

    @Override
    protected OfficeDao getRepository() {
        return this.repository;
    }
}
