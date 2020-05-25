package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(
        name = "patient",
        indexes = {
                @Index(columnList = "patient_id", name = "patient_patient_id_uindex", unique = true),
        }
)
public class Patient extends GenericModel<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_pk")
    @SequenceGenerator(sequenceName = "patient_pk", name = "patient_pk", allocationSize = 1)
    @Column(name = "patient_id")
    private Integer id;
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @JoinColumn(name = "office_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Office office;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Patient;
    }
}
