package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Entity;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Entity
@Table(name = "system_staff_specialty", primaryKey = "specialty_id")
public class StaffSpecialty extends GenericModel<Integer> {
    @Column(name = "name", required = true)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
