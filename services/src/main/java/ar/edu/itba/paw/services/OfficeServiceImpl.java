package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.interfaces.services.OfficeService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OfficeServiceImpl extends GenericSearchableServiceImpl<OfficeDao, Office, Integer> implements OfficeService {
    @Autowired
    private OfficeDao repository;

    @Override
    public Collection<Office> findByCountry(Country country) {
        return this.repository.findByCountry(country);
    }

    @Override
    public Collection<Office> findByProvince(Province province) {
        return this.repository.findByProvince(province);
    }

    @Override
    public Collection<Office> findByLocality(Locality locality) {
        return this.findByLocality(locality);
    }

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
    protected OfficeDao getRepository() {
        return this.repository;
    }
}
