package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;
import ar.edu.itba.paw.persistenceAnnotations.TableRelation;

@Table(name = "system_province", primaryKey = "province_id")
public class Province extends GenericModel<Integer> {
    @Column(name = "country_id", required = true, relation = TableRelation.MANY_TO_ONE)
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
