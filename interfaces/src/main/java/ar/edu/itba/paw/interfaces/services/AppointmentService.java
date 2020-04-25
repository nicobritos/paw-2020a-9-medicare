package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;

import java.util.List;

public interface AppointmentService extends GenericService<Appointment, Integer> {
    List<Appointment> findPending(Patient patient);

    List<Appointment> findPending(Patient patient, Staff staff);
}
