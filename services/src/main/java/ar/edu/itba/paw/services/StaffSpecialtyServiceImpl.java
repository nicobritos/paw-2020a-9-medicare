package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.StaffSpecialtyDao;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffSpecialtyServiceImpl extends GenericSearchableServiceImpl<StaffSpecialtyDao, StaffSpecialty, Integer> implements StaffSpecialtyService {
    @Autowired
    private StaffSpecialtyDao repository;

    @Autowired
    StaffService staffService;

    @Override
    public void addToUser(int staffSpecialtyId, User user) {
        List<Staff> staffs = this.staffService.findByUser(user);
        Optional<StaffSpecialty> staffSpecialty = findById(staffSpecialtyId);
        if(staffSpecialty.isPresent()) {
            for (Staff staff : staffs) {
                if(!staff.getStaffSpecialties().contains(staffSpecialty.get())) {
                    staff.getStaffSpecialties().add(staffSpecialty.get());
                    staffService.update(staff);
                }
            }
        }
    }

    @Override
    public void removeFromUser(int staffSpecialtyId, User user) {
        List<Staff> staffs = this.staffService.findByUser(user);
        Optional<StaffSpecialty> staffSpecialty = findById(staffSpecialtyId);
        if(staffSpecialty.isPresent()) {
            for (Staff staff : staffs) {
                staff.getStaffSpecialties().remove(staffSpecialty.get());
                staffService.update(staff);
            }
        }
    }

    @Override
    protected StaffSpecialtyDao getRepository() {
        return this.repository;
    }
}
