package ar.edu.itba.paw.models;

import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(
        name = "appointment",
        indexes = {
                @Index(columnList = "appointment_id", name = "appointment_appointment_id_uindex", unique = true),
                @Index(columnList = "status", name = "appointment_status_status_index"),
                @Index(columnList = "from_date", name = "appointment_from_date_to_date_index")
        }
)
public class Appointment extends GenericModel<Integer> {
    public static final int DURATION = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_pk")
    @SequenceGenerator(sequenceName = "appointment_appointment_id_seq", name = "appointment_pk", allocationSize = 1)
    @Column(name = "appointment_id")
    private Integer id;
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus appointmentStatus;
    @Column(name = "from_date", nullable = false)
    private DateTime fromDate;
    @Column(name = "message")
    private String message;
    @Column(name = "motive")
    private String motive;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

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

    public DateTime getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDate() {
        return this.fromDate.plusMinutes(Appointment.DURATION);
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
