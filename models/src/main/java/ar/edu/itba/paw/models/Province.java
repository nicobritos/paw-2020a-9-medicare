package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OrderBy;
import ar.edu.itba.paw.persistenceAnnotations.OrderCriteria;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "system_province", primaryKey = "province_id")
public class Province extends GenericModel<Integer> {
    @OrderBy(OrderCriteria.ASC)
    @Column(name = "name", required = true)
    private String name;
    private Country country;

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

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Province;
    }
}
