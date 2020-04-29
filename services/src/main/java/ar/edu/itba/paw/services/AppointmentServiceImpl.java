package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class AppointmentServiceImpl extends GenericServiceImpl<AppointmentDao, Appointment, Integer> implements AppointmentService {
    @Autowired
    private AppointmentDao repository;
    @Autowired
    private WorkdayService workdayService;

    @Override
    public List<Appointment> findPending(Patient patient) {
        return this.repository.findPending(patient);
    }

    @Override
    public List<Appointment> findPending(Staff staff) {
        return this.repository.findPending(staff);
    }

    @Override
    public List<Appointment> findPending(Patient patient, Staff staff) {
        return this.repository.findPending(patient, staff);
    }

    @Override
    public List<Appointment> findTodayAppointments(Staff staff){
        return this.repository.findByDate(staff, LocalDate.now());
    }

    @Override
    public List<Appointment> findAppointmentsByDate(Staff staff, LocalDate date){
        return this.repository.findByDate(staff, date);
    }

    @Override
    public void setStatus(Appointment appointment, AppointmentStatus status) {

    }

    public Appointment create(Appointment model) {
        if (!(model.getDuration() == Appointment.DURATION))
            return null; // TODO: Exception

        if (!this.isValidDate(model.getStaff(), model.getFromDate(), model.getToDate()))
            return null; // TODO: Exception

        return this.repository.create(model);
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Patient patient, Staff staff) {
        List<Workday> workdays = this.workdayService.findByStaff(staff);
        return new LinkedList<>(); // TODO
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff) {
        List<Workday> workdays = this.workdayService.findByStaff(staff);
        return new LinkedList<>(); // TODO
    }

    @Override
    protected AppointmentDao getRepository() {
        return this.repository;
    }

    private boolean isValidDate(Staff staff, Date fromDate, Date toDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(fromDate);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(toDate);

        AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
        appointmentTimeSlot.setDay(WorkdayDay.fromCalendar(fromCalendar).name());
        appointmentTimeSlot.setFromHour(fromCalendar.get(Calendar.HOUR));
        appointmentTimeSlot.setFromMinute(fromCalendar.get(Calendar.MINUTE));

        appointmentTimeSlot.setToHour(toCalendar.get(Calendar.HOUR));
        appointmentTimeSlot.setToMinute(toCalendar.get(Calendar.MINUTE));

        if (!this.workdayService.isStaffWorking(staff, appointmentTimeSlot))
            return false;

        if (!this.findAvailableTimeslots(staff).contains(appointmentTimeSlot))
            return false;

        return this.isValidTimeSlot(appointmentTimeSlot);
    }

    private boolean isValidTimeSlot(AppointmentTimeSlot appointmentTimeSlot) {
        if (appointmentTimeSlot.getFromHour() < appointmentTimeSlot.getToHour())
            return false;
        if (appointmentTimeSlot.getFromHour() == appointmentTimeSlot.getToHour()
                && appointmentTimeSlot.getFromMinute() <= appointmentTimeSlot.getToMinute())
            return false;
        return appointmentTimeSlot.getDuration() == Appointment.DURATION;
    }
}
