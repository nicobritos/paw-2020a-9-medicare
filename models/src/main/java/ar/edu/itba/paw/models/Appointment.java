package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.time.Duration;
import java.util.Date;

@Table(name = "appointment", primaryKey = "appointment_id")
public class Appointment extends GenericModel<Appointment, Integer> {
    public static final int DURATION = 15;

    @Column(name = "status", required = true)
    private String appointmentStatus;
    @ManyToOne(name = "patient_id", required = true)
    private Patient patient;
    @ManyToOne(name = "staff_id", required = true)
    private Staff staff;
    @Column(name = "from_date", required = true)
    private Date fromDate;
    @Column(name = "to_date", required = true)
    private Date toDate;

    public String getAppointmentStatus() {
        return this.appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
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

    public Date getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return this.toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getDuration() {
        return (int) Duration.between(this.fromDate.toInstant(), this.toDate.toInstant()).toMinutes();
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Appointment;
    }
}
