package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OrderBy;
import ar.edu.itba.paw.persistenceAnnotations.OrderCriteria;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "system_country", primaryKey = "country_id", manualPrimaryKey = true)
public class Country extends GenericModel<String> {
    @OrderBy(OrderCriteria.ASC)
    @Column(name = "name", required = true)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Country;
    }
}
