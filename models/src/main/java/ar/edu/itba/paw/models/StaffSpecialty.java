package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(
        name = "system_staff_specialty",
        indexes = {
                @Index(columnList = "specialty_id", name = "specialty_specialty_id_uindex", unique = true)
        }
)
public class StaffSpecialty extends GenericModel<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_staff_specialty_specialty_id_seq")
    @SequenceGenerator(sequenceName = "system_staff_specialty_specialty_id_seq", name = "system_staff_specialty_specialty_id_seq", allocationSize = 1)
    @Column(name = "specialty_id")
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof StaffSpecialty;
    }
}
