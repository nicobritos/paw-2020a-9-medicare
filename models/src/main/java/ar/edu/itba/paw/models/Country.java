package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Collection;

@Table(name = "system_country", primaryKey = "country_id")
public class Country extends GenericModel<String> {
    @Column(name = "name", required = true)
    private String name;
    @OneToMany(joinedName = "country_id", required = true, className = Province.class)
    private Collection<Province> provinces;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Province> getProvinces() {
        return this.provinces;
    }

    public void setProvinces(Collection<Province> provinces) {
        this.provinces = provinces;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", provinces=" + provinces +
                ", id=" + id +
                '}';
    }
}
