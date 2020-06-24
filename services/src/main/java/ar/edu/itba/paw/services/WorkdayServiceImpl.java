package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WorkdayServiceImpl extends GenericServiceImpl<WorkdayDao, Workday, Integer> implements WorkdayService {
    @Autowired
    private WorkdayDao repository;

    @Override
    public List<Workday> findByUser(User user) {
        return this.repository.findByUser(user);
    }

    @Override
    public List<Workday> findByStaff(Staff staff) {
        return this.repository.findByStaff(staff);
    }

    @Override
    public List<Workday> findByStaff(Staff staff, WorkdayDay day) {
        return this.repository.findByStaff(staff, day);
    }

    @Override
    public boolean isStaffWorking(Staff staff, AppointmentTimeSlot timeSlot) {
        return this.repository.isStaffWorking(staff, timeSlot);
    }

    @Override
    protected WorkdayDao getRepository() {
        return this.repository;
    }
}
