package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.Workday;
import org.joda.time.LocalDateTime;

import java.util.Collection;
import java.util.List;

public interface AppointmentDao extends GenericDao<Appointment, Integer> {
    List<Appointment> find(Patient patient);

    List<Appointment> findByPatients(Collection<Patient> patient);

    List<Appointment> find(Staff staff);

    List<Appointment> findByStaffs(Collection<Staff> staffs);

    List<Appointment> findPending(Patient patient);

    List<Appointment> findPending(Staff staff);

    List<Appointment> findPending(Patient patient, Staff staff);

    List<Appointment> findByStaffsAndDate(Collection<Staff> staffs, LocalDateTime date);

    List<Appointment> findByStaffsAndDate(Collection<Staff> staffs, LocalDateTime fromDate, LocalDateTime toDate);

    List<Appointment> findByPatientsAndDate(Collection<Patient> patients, LocalDateTime fromDate, LocalDateTime toDate);

    List<Appointment> findByDate(Patient patient, LocalDateTime date);

    List<Appointment> findByPatientsFromDate(Collection<Patient> patients, LocalDateTime from);

    List<Appointment> findByWorkday(Workday workday);

    void cancelAppointments(Collection<Appointment> appointments);
}
