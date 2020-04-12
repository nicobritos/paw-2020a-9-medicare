package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "system_province", primaryKey = "province_id")
public class Province extends GenericModel<Integer> {
    @Column(name = "name", required = true)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Province{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
