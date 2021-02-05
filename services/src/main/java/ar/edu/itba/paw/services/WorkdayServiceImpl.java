package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class WorkdayServiceImpl extends GenericServiceImpl<WorkdayDao, Workday, Integer> implements WorkdayService {
    @Autowired
    private WorkdayDao repository;
    @Autowired
    private DoctorService doctorService;

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
    public Collection<Workday> create(Collection<Workday> workdays) {
        return this.repository.create(workdays);
    }

    @Override
    public void remove(Integer id, User user) {
        List<Doctor> doctors = this.doctorService.findByUser(user);
        Optional<Workday> workdayOptional = findById(id);
        if (!workdayOptional.isPresent())
            throw new IllegalArgumentException();
        if(!doctors.contains(workdayOptional.get().getDoctor()))
            throw new MediCareException("The user is not allowed to delete this workday");
        this.repository.remove(workdayOptional.get());
    }

    @Override
    protected WorkdayDao getRepository() {
        return this.repository;
    }
}
