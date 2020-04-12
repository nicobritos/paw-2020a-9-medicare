package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

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

    @Override
    public String toString() {
        return "StaffSpecialty{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
