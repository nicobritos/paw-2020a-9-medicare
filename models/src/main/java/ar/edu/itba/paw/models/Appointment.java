package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Date;

@Table(name = "appointment", primaryKey = "appointment_id")
public class Appointment extends GenericModel<Appointment, Integer> {
    @ManyToOne(name = "status_id", required = true)
    private AppointmentStatus appointmentStatus;
    @ManyToOne(name = "patient_id", required = true)
    private Patient patient;
    @ManyToOne(name = "staff_id", required = true)
    private Staff staff;
    @Column(name = "date", required = true)
    private Date date;

    public AppointmentStatus getAppointmentStatus() {
        return this.appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Appointment;
    }
}
