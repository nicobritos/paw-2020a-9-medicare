package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class StaffDaoImpl implements StaffDao {
    @Override
    public Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        return null;
    }

    @Override
    public Collection<Staff> getByName(String name) {
        return null;
    }

    @Override
    public Staff getById(Integer id) {
        return null;
    }

    @Override
    public void save(Staff model) {

    }

    @Override
    public void remove(Staff model) {

    }
}
