package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OrderBy;
import ar.edu.itba.paw.persistenceAnnotations.OrderCriteria;
import ar.edu.itba.paw.persistenceAnnotations.Table;
import org.joda.time.DateTime;

@Table(name = "appointment", primaryKey = "appointment_id")
public class Appointment extends GenericModel<Integer> {
    public static final int DURATION = 15;

    @Column(name = "status", required = true)
    private String appointmentStatus;
    @OrderBy(OrderCriteria.ASC)
    @Column(name = "from_date", required = true)
    private DateTime fromDate;
    @Column(name = "patient_id", required = true)
    private int patientId;
    private Patient patient;
    @Column(name = "staff_id", required = true)
    private int staffId;
    private Staff staff;
    @Column(name = "message")
    private String message;
    @Column(name = "motive")
    private String motive;

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
        this.patientId = patient.getId();
    }

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
        this.staffId = staff.getId();
    }

    public DateTime getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDate(){
        return this.fromDate.plusMinutes(Appointment.DURATION);
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
        if(this.patient != null && !this.patient.getId().equals(patientId)){
            patient = null;
        }

    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
        if(this.staff != null && !this.staff.getId().equals(staffId)){
            staff = null;
        }
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMotive() {
        return this.motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Appointment;
    }
}
