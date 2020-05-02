package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;
import org.joda.time.DateTime;

import java.time.LocalDateTime;

@Table(name = "appointment", primaryKey = "appointment_id")
public class Appointment extends GenericModel<Integer> {
    public static final int DURATION = 15;

    @Column(name = "status", required = true)
    private String appointmentStatus;
    @Column(name = "from_date", required = true)
    private DateTime fromDate;
    private Patient patient;
    private Staff staff;

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

    public DateTime getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate(){
//        return LocalDateTime.ofEpochSecond(this.fromDate.toInstant().getEpochSecond(), 0, ZoneOffset.UTC);
        return null;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Appointment;
    }

    public LocalDateTime getFromLocalDate() {
//        return LocalDateTime.ofEpochSecond(this.fromDate.toInstant().getEpochSecond(), 0, ZoneOffset.UTC);
        return null;
    }
}
