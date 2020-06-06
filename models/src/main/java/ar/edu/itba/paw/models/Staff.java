package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedList;

@Entity
@Table(
        name = "staff",
        indexes = {
                @Index(columnList = "staff_id", name = "staff_staff_id_uindex", unique = true),
                @Index(columnList = "user_id", name = "staff_user_id_index")
        }
)
public class Staff extends GenericModel<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_staff_id_seq")
    @SequenceGenerator(sequenceName = "staff_staff_id_seq", name = "staff_staff_id_seq", allocationSize = 1)
    @Column(name = "staff_id")
    private Integer id;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "registration_number")
    private Integer registrationNumber;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @JoinColumn(name = "office_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Office office;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "system_staff_specialty_staff",
            joinColumns = @JoinColumn(name = "specialty_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    @OrderBy("name ASC")
    private Collection<StaffSpecialty> staffSpecialties = new LinkedList<>();

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRegistrationNumber() {
        return this.registrationNumber == null ? 0 : this.registrationNumber;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Office getOffice() {
        return this.office;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Collection<StaffSpecialty> getStaffSpecialties() {
        return this.staffSpecialties;
    }

    public void setStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        this.staffSpecialties = staffSpecialties;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Staff;
    }
}
