package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class StaffServiceImpl extends GenericServiceImpl<StaffDao, Staff, Integer> implements StaffService {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private StaffDao repository;

    @Override
    public List<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities) {
        return this.repository.findBy(name, surname, offices, staffSpecialties, localities);
    }

    @Override
    public List<Staff> findBy(Collection<String> name, Collection<String> surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities) {
        return this.repository.findBy(name, surname, offices, staffSpecialties, localities);
    }

    @Override
    @Transactional
    public Paginator<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page) {
        return this.repository.findBy(name, surname, offices, staffSpecialties, localities, page, PAGE_SIZE);
    }

    @Override
    @Transactional
    public Paginator<Staff> findBy(Collection<String> name, Collection<String> surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page) {
        return this.repository.findBy(name, surname, offices, staffSpecialties, localities, page, PAGE_SIZE);
    }

    @Override
    public List<Staff> findByUser(User user) {
        return this.repository.findByUser(user);
    }

    @Override
    protected StaffDao getRepository() {
        return this.repository;
    }
}
