package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<Appointment> findToday(Staff staff){
        return this.repository.findByDate(staff, LocalDate.now());
    }

    @Override
    public List<Appointment> findToday(Patient patient){
        return this.repository.findByDate(patient, LocalDate.now());
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

    public Appointment create(Appointment model) throws InvalidAppointmentDateException, InvalidAppointmentDurationException, MediCareException {
        if (!this.isValidDate(model.getStaff(), model.getFromDate(), model.getFromDate())) // todo
            throw new InvalidAppointmentDateException();

        model.setAppointmentStatus(AppointmentStatus.PENDING.name());
        // TODO CHANGE DATE CLASS
//        LocalDate fromDateToCreate = LocalDate.from(model.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//        LocalDate toDateToCreate = LocalDate.from(model.getToDate().toInstant(ZoneOffset.UTC).atZone(ZoneId.systemDefault()).toLocalDate());


//        List<Appointment> appointments = findByDay(model.getStaff(), fromDateToCreate);
//        for (Appointment appointment : appointments){
//            LocalDate appointmentFromDate = LocalDate.from(appointment.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            LocalDate appointmentToDate = LocalDate.from(appointment.getToDate().atZone(ZoneId.systemDefault()).toLocalDate());
//
//            if(fromDateToCreate.isAfter(appointmentFromDate) && fromDateToCreate.isBefore(appointmentToDate)
//            || (toDateToCreate.isAfter(appointmentFromDate) && toDateToCreate.isBefore(appointmentToDate))){
//                throw new MediCareException("Workday date overlaps with an existing one");
//            }
//        }
        return this.repository.create(model);
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDate fromDate, LocalDate toDate) {
        LocalDate now = LocalDate.now();
        List<AppointmentTimeSlot> appointmentTimeSlots = new LinkedList<>();
        LocalDate localDate;
        if (now.isBefore(fromDate)) {
            localDate = LocalDate.ofEpochDay(now.toEpochDay());
        } else {
            localDate = LocalDate.ofEpochDay(fromDate.toEpochDay());
        }
        while (toDate.isAfter(localDate)) {
            WorkdayDay workdayDay = WorkdayDay.from(localDate);
            List<Workday> workdays = this.workdayService.findByStaff(staff, workdayDay);
            LocalDate localDateCopy = LocalDate.ofEpochDay(localDate.toEpochDay());
            for (Workday workday : workdays) {
                for (int j = workday.getStartMinute(); j < 60; j += Appointment.DURATION) {
                    AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                    appointmentTimeSlot.setDate(localDateCopy);
                    appointmentTimeSlot.setFromHour(workday.getStartHour());
                    appointmentTimeSlot.setFromMinute(j);
                    appointmentTimeSlot.setDuration(Appointment.DURATION);
                    appointmentTimeSlots.add(appointmentTimeSlot);
                }
                for (int i = workday.getStartHour() + 1; i < workday.getEndHour(); i++) {
                    for (int j = 0; j < 60; j += Appointment.DURATION) {
                        AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                        appointmentTimeSlot.setDate(localDateCopy);
                        appointmentTimeSlot.setFromHour(i);
                        appointmentTimeSlot.setFromMinute(j);
                        appointmentTimeSlot.setDuration(Appointment.DURATION);
                        appointmentTimeSlots.add(appointmentTimeSlot);
                    }
                }
                for (int j = 0; j < workday.getEndMinute(); j += Appointment.DURATION) {
                    AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                    appointmentTimeSlot.setDate(localDateCopy);
                    appointmentTimeSlot.setFromHour(workday.getEndHour());
                    appointmentTimeSlot.setFromMinute(j);
                    appointmentTimeSlot.setDuration(Appointment.DURATION);
                    appointmentTimeSlots.add(appointmentTimeSlot);
                }

                List<Appointment> takenAppointments = this.findByDay(staff, localDate);
                for (Appointment appointment : takenAppointments) {
                    AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
                    appointmentTimeSlot.setDate(localDateCopy);
                    // todo date to anything useful
//                    appointmentTimeSlot.setFromHour(appointment.getFromLocalDate().getHour());
//                    appointmentTimeSlot.setFromMinute(appointment.getFromLocalDate().getMinute());
                    appointmentTimeSlot.setDuration(Appointment.DURATION);

                    appointmentTimeSlots.remove(appointmentTimeSlot);
                }
            }

            localDate = localDate.plusDays(1);
        }

        return appointmentTimeSlots;
    }

    @Override
    public List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDate date) {
        return this.findAvailableTimeslots(staff, date, date);
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
        return true;
//        Calendar fromCalendar = Calendar.getInstance();
//        fromCalendar.setTime(fromDate);
//        Calendar toCalendar = Calendar.getInstance();
//        toCalendar.setTime(toDate);
//
//        AppointmentTimeSlot appointmentTimeSlot = new AppointmentTimeSlot();
//
//        appointmentTimeSlot.setDate(LocalDate.from(fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
//        appointmentTimeSlot.setFromHour(fromCalendar.get(Calendar.HOUR));
//        appointmentTimeSlot.setFromMinute(fromCalendar.get(Calendar.MINUTE));
//        appointmentTimeSlot.setDuration(toCalendar.get(Calendar.HOUR));
//
//        if (!this.workdayService.isStaffWorking(staff, appointmentTimeSlot))
//            return false;
//
//        if (!this.findAvailableTimeslots(staff, LocalDate.ofEpochDay(fromCalendar.toInstant().getEpochSecond())).contains(appointmentTimeSlot))
//            return false;
//
//        return this.isValidTimeSlot(appointmentTimeSlot);
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
