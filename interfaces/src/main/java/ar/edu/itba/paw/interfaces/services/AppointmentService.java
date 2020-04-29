package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentAlreadyCancelledException;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentAlreadyCompletedException;
import ar.edu.itba.paw.interfaces.services.exceptions.InvalidAppointmentStatusChangeException;
import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService extends GenericService<Appointment, Integer> {
    List<Appointment> find(Staff staff);

    List<Appointment> find(Patient patient);

    List<Appointment> findTodayAppointments(Staff staff);

    List<Appointment> findTodayAppointments(Patient patient);

    List<Appointment> findByDay(Staff staff, LocalDate date);

    void setStatus(Appointment appointment, AppointmentStatus status) throws
            AppointmentAlreadyCancelledException,
            InvalidAppointmentStatusChangeException,
            AppointmentAlreadyCompletedException;

    List<AppointmentTimeSlot> findAvailableTimeslots(Patient patient, Staff staff, LocalDate date);

    List<AppointmentTimeSlot> findAvailableTimeslots(Staff staff, LocalDate date);
}
