package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkdayServiceImpl extends GenericServiceImpl<WorkdayDao, Workday, Integer> implements WorkdayService {
    @Autowired
    private WorkdayDao repository;

    @Override
    public List<Workday> findByUser(User user) {
        return this.repository.findByUser(user);
    }

    @Override
    public List<Workday> findByDoctor(Doctor doctor) {
        return this.repository.findByDoctor(doctor);
    }

    @Override
    public List<Workday> findByDoctor(Doctor doctor, WorkdayDay day) {
        return this.repository.findByDoctor(doctor, day);
    }

    @Override
    public boolean doctorWorks(Doctor doctor, AppointmentTimeSlot timeSlot) {
        return this.repository.doctorWorks(doctor, timeSlot);
    }

    @Override
    public List<Workday> create(List<Workday> workdays) {
        return this.repository.create(workdays);
    }

    @Override
    protected WorkdayDao getRepository() {
        return this.repository;
    }
}
