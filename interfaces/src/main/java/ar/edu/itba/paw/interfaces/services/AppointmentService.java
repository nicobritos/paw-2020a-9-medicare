package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentAlreadyCancelledException;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentAlreadyCompletedException;
import ar.edu.itba.paw.interfaces.services.exceptions.InvalidAppointmentStatusChangeException;
import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.*;
import org.joda.time.LocalDateTime;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AppointmentService extends GenericService<Appointment, Integer> {
    List<Appointment> find(Staff staff);

    List<Appointment> findByStaffs(Collection<Staff> staffs);

    List<Appointment> find(Patient patient);

    List<Appointment> findByPatients(Collection<Patient> patients);

    List<Appointment> findToday(Collection<Staff> staff);

    List<Appointment> findToday(Patient patient);

    List<Appointment> findByDay(Staff staff, LocalDateTime date);

    List<Appointment> findByStaffsAndDay(Collection<Staff> staffs, LocalDateTime date);

    List<Appointment> findByStaffsAndDay(Collection<Staff> staffs, LocalDateTime from, LocalDateTime to);

    List<Appointment> findByPatientsFromDay(Collection<Patient> patients, LocalDateTime from);

    List<Appointment> findByPatientsAndDay(Collection<Patient> patients, LocalDateTime from, LocalDateTime to);

    List<Appointment> findByWorkday(Workday workday);

    void setStatus(Appointment appointment, AppointmentStatus status) throws
            AppointmentAlreadyCancelledException,
            InvalidAppointmentStatusChangeException,
            AppointmentAlreadyCompletedException;

    List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDateTime fromDate, LocalDateTime toDate);

    List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDateTime date);

    List<Appointment> cancelAppointments(Workday workday);

    Map<Workday, Integer> appointmentQtyByWorkdayOfUser(User user);

    void remove(Integer id, User user);

    List<List<AppointmentTimeSlot>> findWeekTimeslots(Staff staff, LocalDateTime from, LocalDateTime to);
}
