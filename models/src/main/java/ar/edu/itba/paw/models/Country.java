package ar.edu.itba.paw.models;

import ar.edu.itba.paw.interfaces.daos.annotations.Column;
import ar.edu.itba.paw.interfaces.daos.annotations.Entity;
import ar.edu.itba.paw.interfaces.daos.annotations.Table;

@Entity
@Table(name = "system_country", primaryKey = "country_id")
public class Country extends GenericModel<String> {
    @Column(name = "name", required = true)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
