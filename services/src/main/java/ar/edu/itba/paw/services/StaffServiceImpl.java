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
    public Set<Staff> findByStaffSpecialties(Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findByStaffSpecialties(staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameAndSurname(String name, String surname) {
        return this.repository.findByNameAndSurname(name, surname);
    }

    @Override
    public Set<Staff> findByNameAndStaffSpecialties(String name, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findByNameAndStaffSpecialties(name, staffSpecialties);
    }

    @Override
    public Set<Staff> findBySurnameAndOffice(String surname, Set<Office> offices) {
        return this.repository.findBySurnameAndOffice(surname, offices);
    }

    @Override
    public Set<Staff> findBySurnameAndStaffSpecialties(String surname, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findBySurnameAndStaffSpecialties(surname, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameOfficeAndStaffSpecialties(String name, Set<Office> offices, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findByNameOfficeAndStaffSpecialties(name, offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameSurnameAndStaffSpecialties(String name, String surname, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findByNameSurnameAndStaffSpecialties(name, surname, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameSurnameAndOffice(String name, String surname, Set<Office> offices) {
        return this.repository.findByNameSurnameAndOffice(name, surname, offices);
    }

    @Override
    public Set<Staff> findByNameSurnameOfficeAndStaffSpecialities(String name, String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findByNameSurnameOfficeAndStaffSpecialities(name, surname, offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findByOfficeAndStaffSpecialties(Set<Office> offices, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findByOfficeAndStaffSpecialties(offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findBySurnameOfficeAndStaffSpecialties(String surname, Set<Office> offices, Set<StaffSpecialty> staffSpecialties) {
        return this.repository.findBySurnameOfficeAndStaffSpecialties(surname, offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameAndOffice(String name, Set<Office> offices) {
        return this.repository.findByNameAndOffice(name, offices);
    }

    @Override
    public Set<Staff> findBySurname(String surname) {
        return this.repository.findBySurname(surname);
    }

    @Override
    public Set<Staff> findByOffice(Set<Office> offices) {
        return this.repository.findByOffice(offices);
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
