package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StaffServiceImpl extends GenericServiceImpl<Staff, Integer> implements StaffService {
    @Autowired
    protected StaffDao repository;

    @Override
    public Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        return null;
    }

    @Override
    public Collection<Staff> findByName(String name) {
        return null;
    }
}
