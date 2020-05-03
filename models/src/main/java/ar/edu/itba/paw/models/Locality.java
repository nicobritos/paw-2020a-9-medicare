package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "system_locality", primaryKey = "locality_id")
public class Locality extends GenericModel<Integer> {
    @Column(name = "name", required = true)
    private String name;
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

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Locality;
    }
}
