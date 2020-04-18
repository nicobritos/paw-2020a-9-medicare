package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.HashSet;
import java.util.Set;

@Table(name = "system_country", primaryKey = "country_id", customPrimaryKey = true)
public class Country extends GenericModel<Country, String> {
    @Column(name = "name", required = true)
    private String name;
    @OneToMany(name = "country_id", className = Province.class)
    private Set<Province> provinces = new HashSet<>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Province> getProvinces() {
        return this.provinces;
    }
}
