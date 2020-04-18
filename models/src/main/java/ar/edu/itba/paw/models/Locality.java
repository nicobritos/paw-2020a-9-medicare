package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "system_locality", primaryKey = "locality_id")
public class Locality extends GenericModel<Locality, Integer> {
    @Column(name = "name", required = true)
    private String name;
    @ManyToOne(name = "province_id", inverse = true)
    private Province province;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province getProvince() {
        return this.province;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Locality;
    }
}
