package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService extends GenericService<Appointment, Integer> {
    List<Appointment> findPending(Patient patient);

    List<Appointment> findPending(Staff staff);

    List<Appointment> findTodayAppointments(Staff staff);

    List<Appointment> findAppointmentsByDate(Staff staff, LocalDate date);

    List<Appointment> findPending(Patient patient, Staff staff);

    void setStatus(Appointment appointment, AppointmentStatus status);

    List<AppointmentTimeSlot> findAvailableTimeslots(Patient patient, Staff staff);

    List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff);
}
