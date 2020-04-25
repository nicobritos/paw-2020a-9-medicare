package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;

import java.util.List;

public interface AppointmentDao extends GenericDao<Appointment, Integer> {
    List<Appointment> findPending(Patient patient);

    List<Appointment> findPending(Patient patient, Staff staff);
}
