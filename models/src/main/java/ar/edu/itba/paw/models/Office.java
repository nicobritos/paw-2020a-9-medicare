package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OrderBy;
import ar.edu.itba.paw.persistenceAnnotations.OrderCriteria;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "office", primaryKey = "office_id")
public class Office extends GenericModel<Integer> {
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @OrderBy(OrderCriteria.ASC)
    @Column(name = "name", required = true)
    private String name;
    @Column(name = "street", required = true)
    private String street;
    @Column(name = "url")
    private String url;

    private Locality locality;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Locality getLocality() {
        return this.locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Office;
    }
}
