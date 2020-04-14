package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StaffServiceImpl extends GenericSearchableServiceImpl<StaffDao, Staff, Integer> implements StaffService {
    @Autowired
    private StaffDao repository;

    @Override
    public Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        return this.repository.findByStaffSpecialties(staffSpecialties);
    }

    @Override
    public Collection<Staff> findByNameAndStaffSpecialties(String name, Collection<StaffSpecialty> staffSpecialties) {
        return this.repository.findByNameAndStaffSpecialties(name, staffSpecialties);
    }

    @Override
    public Collection<Staff> findByNameOfficeAndStaffSpecialties(String name, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return this.repository.findByNameOfficeAndStaffSpecialties(name, offices, staffSpecialties);
    }

    @Override
    public Collection<Staff> findByOfficeAndStaffSpecialties(Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return this.repository.findByOfficeAndStaffSpecialties(offices, staffSpecialties);
    }

    @Override
    public Collection<Staff> findByNameAndOffice(String name, Collection<Office> offices) {
        return this.repository.findByNameAndOffice(name, offices);
    }

    @Override
    public Collection<Staff> findByOffice(Collection<Office> offices) {
        return this.repository.findByOffice(offices);
    }

    @Override
    public void addStaffSpecialty(Staff staff, StaffSpecialty staffSpecialty) {
        staff.getStaffSpecialties().add(staffSpecialty);
        this.repository.update(staff);
    }

    @Override
    public void addStaffSpecialties(Staff staff, Collection<StaffSpecialty> staffSpecialties) {
        staff.getStaffSpecialties().addAll(staffSpecialties);
        this.repository.update(staff);
    }

    @Override
    protected StaffDao getRepository() {
        return this.repository;
    }
}
