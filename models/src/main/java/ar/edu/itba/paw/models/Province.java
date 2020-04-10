package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Entity;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Entity
@Table(name = "system_province", primaryKey = "province_id")
public class Province extends GenericModel<Integer> {
    @Column(name = "country", required = true)
    private Country country;

    @Column(name = "name", required = true)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
