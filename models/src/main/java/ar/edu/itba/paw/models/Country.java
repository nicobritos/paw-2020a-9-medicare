package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Collection;

@Table(name = "system_country", primaryKey = "country_id", customPrimaryKey = true)
public class Country extends GenericModel<Country, String> {
    @Column(name = "name", required = true)
    private String name;
    @OneToMany(name = "country_id", className = Province.class)
    private JoinedCollection<Province> provinces = new JoinedCollection<>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Province> getProvinces() {
        return this.provinces.getModels();
    }

    @Override
    public Country copy() {
        Country country = new Country();
        country.name = this.name;
        country.id = this.id;
        country.provinces = this.provinces.copy();
        return country;
    }
}
