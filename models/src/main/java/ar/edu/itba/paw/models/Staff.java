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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_pk")
    @SequenceGenerator(sequenceName = "staff_pk", name = "staff_pk", allocationSize = 1)
    @Column(name = "staff_id")
    private Integer id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "registration_number")
    private Integer registrationNumber;
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @JoinColumn(name = "office_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Office office;
    @JoinColumn(name = "user_id")
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

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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
