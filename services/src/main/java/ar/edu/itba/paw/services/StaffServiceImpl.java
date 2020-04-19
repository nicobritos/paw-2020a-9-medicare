package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StaffServiceImpl extends GenericSearchableServiceImpl<StaffDao, Staff, Integer> implements StaffService {
    @Autowired
    private StaffDao repository;

    @Override
    public Set<Staff> findBy(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findBy(name, surname, offices, staffSpecialties);
    }

    @Override
    public void addStaffSpecialty(Staff staff, StaffSpecialty staffSpecialty) {
        staff.getStaffSpecialties().add(staffSpecialty);
        this.repository.update(staff);
    }

    @Override
    public void addStaffSpecialties(Staff staff, Set<StaffSpecialty> staffSpecialties) {
        staff.getStaffSpecialties().addAll(staffSpecialties);
        this.repository.update(staff);
    }

    @Override
    protected StaffDao getRepository() {
        return this.repository;
    }
}
