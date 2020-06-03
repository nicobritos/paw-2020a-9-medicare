package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentAlreadyCancelledException;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentAlreadyCompletedException;
import ar.edu.itba.paw.interfaces.services.exceptions.InvalidAppointmentStatusChangeException;
import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.*;
import org.joda.time.LocalDateTime;

import java.util.List;

public interface AppointmentService extends GenericService<Appointment, Integer> {
    List<Appointment> find(Staff staff);

    List<Appointment> findByStaffs(List<Staff> staffs);

    List<Appointment> find(Patient patient);

    List<Appointment> findByPatients(List<Patient> patients);

    List<Appointment> findByPatientsFromDate(List<Patient> patients, LocalDateTime from);

    List<Appointment> findToday(List<Staff> staff);

    List<Appointment> findToday(Patient patient);

    List<Appointment> findByDay(Staff staff, LocalDateTime date);

    List<Appointment> findByStaffsAndDay(List<Staff> staffs, LocalDateTime date);

    List<Appointment> findByStaffsAndDay(List<Staff> staffs, LocalDateTime from, LocalDateTime to);

    List<Appointment> findByPatientsAndDay(List<Patient> patients, LocalDateTime date);

    void setStatus(Appointment appointment, AppointmentStatus status) throws
            AppointmentAlreadyCancelledException,
            InvalidAppointmentStatusChangeException,
            AppointmentAlreadyCompletedException;

    List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDateTime fromDate, LocalDateTime toDate);

    List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDateTime date);

    void cancelAppointments(Workday workday);
}
