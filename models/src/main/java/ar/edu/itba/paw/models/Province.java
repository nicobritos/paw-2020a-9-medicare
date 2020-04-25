package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Collection;
import java.util.LinkedList;

@Table(name = "system_province", primaryKey = "province_id")
public class Province extends GenericModel<Province, Integer> {
    @Column(name = "name", required = true)
    private String name;
    @OneToMany(name = "locality_id", className = Locality.class)
    private Collection<Locality> localities = new LinkedList<>();
    @ManyToOne(name = "country_id", inverse = true)
    private Country country;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Locality> getLocalities() {
        return this.localities;
    }

    public Country getCountry() {
        return this.country;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Province;
    }
}
