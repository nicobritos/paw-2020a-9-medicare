package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
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
    public List<Appointment> find(Staff staff) {
        return this.repository.find(staff);
    }

    @Override
    public List<Appointment> find(Patient patient) {
        return this.repository.find(patient);
    }

    @Override
    public List<Appointment> findTodayAppointments(Staff staff){
        return this.repository.findByDate(staff, LocalDate.now());
    }

    @Override
    public List<Appointment> findByDay(Staff staff, LocalDate date){
        return this.repository.findByDate(staff, date);
    }

    @Override
    public void setStatus(Appointment appointment, AppointmentStatus status) throws
            AppointmentAlreadyCancelledException,
            InvalidAppointmentStatusChangeException,
            AppointmentAlreadyCompletedException {
        if (appointment.getAppointmentStatus().equals(status.name()))
            return;

        if (appointment.getAppointmentStatus().equals(AppointmentStatus.CANCELLED.name())) {
            throw new AppointmentAlreadyCancelledException();
        } else if (appointment.getAppointmentStatus().equals(AppointmentStatus.COMPLETE.name())) {
            throw new AppointmentAlreadyCompletedException();
        } else if (!this.isValidStatusChange(appointment.getAppointmentStatus(), status.name())) {
            throw new InvalidAppointmentStatusChangeException();
        }

        appointment.setAppointmentStatus(status.name());
        this.repository.update(appointment);
    }

    public Appointment create(Appointment model) throws InvalidAppointmentDateException, InvalidAppointmentDurationException {
        if (!(model.getDuration() == Appointment.DURATION))
            throw new InvalidAppointmentDurationException();
        if (!this.isValidDate(model.getStaff(), model.getFromDate(), model.getToDate()))
            throw new InvalidAppointmentDateException();

        model.setAppointmentStatus(AppointmentStatus.PENDING.name());
        return this.repository.create(model);
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Patient patient, Staff staff, LocalDate localDate) {
        List<Workday> workdays = this.workdayService.findByStaff(staff);
        return new LinkedList<>(); // TODO
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDate localDate) {
        List<Workday> workdays = this.workdayService.findByStaff(staff, WorkdayDay.from(localDate));
        if (workdays.isEmpty())
            return new LinkedList<>();

        List<Appointment> appointments = this.findByDay(staff, localDate);
        List<AppointmentTimeSlot> availableTimeslots = new LinkedList<>();
        for (Workday workday : workdays) {
            // todo
        }


        return new LinkedList<>(); // TODO
    }

    @Override
    protected AppointmentDao getRepository() {
        return this.repository;
    }

    private boolean isValidStatusChange(String appointmentStatus, String newStatus) {
        if (appointmentStatus.equals(AppointmentStatus.PENDING.name())) {
            return newStatus.equals(AppointmentStatus.WAITING.name()) || newStatus.equals(AppointmentStatus.CANCELLED.name());
        } else if (appointmentStatus.equals(AppointmentStatus.WAITING.name())) {
            return newStatus.equals(AppointmentStatus.SEEN.name());
        } else if (appointmentStatus.equals(AppointmentStatus.SEEN.name())) {
            return newStatus.equals(AppointmentStatus.COMPLETE.name());
        }
        return false;
    }

    private boolean isValidDate(Staff staff, Date fromDate, Date toDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(fromDate);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(toDate);

        AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
        appointmentTimeSlot.setDay(WorkdayDay.from(fromCalendar).name());
        appointmentTimeSlot.setFromHour(fromCalendar.get(Calendar.HOUR));
        appointmentTimeSlot.setFromMinute(fromCalendar.get(Calendar.MINUTE));

        appointmentTimeSlot.setToHour(toCalendar.get(Calendar.HOUR));
        appointmentTimeSlot.setToMinute(toCalendar.get(Calendar.MINUTE));

        if (!this.workdayService.isStaffWorking(staff, appointmentTimeSlot))
            return false;

        if (!this.findAvailableTimeslots(staff, LocalDate.ofEpochDay(fromCalendar.toInstant().getEpochSecond())).contains(appointmentTimeSlot))
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
