package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.models.AppointmentTimeSlot;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.models.WorkdayDay;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkdayServiceImpl extends GenericServiceImpl<WorkdayDao, Workday, Integer> implements WorkdayService {
    @Autowired
    private WorkdayDao repository;

    public Workday create(Workday model) {
        return this.repository.create(model);
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
