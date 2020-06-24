package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_appointment_id_seq")
    @SequenceGenerator(sequenceName = "appointment_appointment_id_seq", name = "appointment_appointment_id_seq", allocationSize = 1)
    @Column(name = "appointment_id")
    private Integer id;
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus appointmentStatus;
    @Column(name = "from_date", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime fromDate;
    @Column(name = "message")
    private String message;
    @Column(name = "motive", nullable = false)
    private String motive;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
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

    public LocalDateTime getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
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
    protected boolean isSameType(Object o) {
        return o instanceof Appointment;
    }
}
