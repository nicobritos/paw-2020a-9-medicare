package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StaffServiceImpl extends GenericSearchableServiceImpl<StaffDao, Staff, Integer> implements StaffService {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private StaffDao repository;

    @Override
    public Set<Staff> findBy(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities) {
        return this.repository.findBy(name, surname, offices, staffSpecialties, localities);
    }

    @Override
    public Set<Staff> findBy(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities, int page) {
        return this.repository.findBy(name, surname, offices, staffSpecialties, localities, page, PAGE_SIZE);
    }

    @Override
    public Set<Staff> findBy(Set<String> name, Set<String> surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties, Set<Locality> localities, int page) {
        return this.repository.findBy(name, surname, offices, staffSpecialties, localities, page, PAGE_SIZE);
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
