package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "system_locality", primaryKey = "locality_id")
public class Locality extends GenericModel<Locality, Integer> {
    @Column(name = "name", required = true)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Locality copy() {
        Locality copy = new Locality();
        copy.name = this.name;
        copy.id = this.id;
        return copy;
    }
}
