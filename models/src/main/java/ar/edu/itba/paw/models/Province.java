package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Collection;

@Table(name = "system_province", primaryKey = "province_id")
public class Province extends GenericModel<Province, Integer> {
    @Column(name = "name", required = true)
    private String name;
    @OneToMany(name = "locality_id", className = Locality.class)
    private JoinedCollection<Locality> localities = new JoinedCollection<>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Locality> getLocalities() {
        return this.localities.getModels();
    }

    @Override
    public Province copy() {
        Province province = new Province();
        province.name = this.name;
        province.id = this.id;
        province.localities = this.localities.copy();
        return province;
    }
}
