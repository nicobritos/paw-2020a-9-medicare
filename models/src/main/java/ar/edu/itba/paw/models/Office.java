package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.HashSet;
import java.util.Set;

@Table(name = "office", primaryKey = "office_id")
public class Office extends GenericModel<Office, Integer> {
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "name", required = true)
    private String name;
    @ManyToOne(name = "locality_id", required = true)
    private Locality locality;
    @Column(name = "street", required = true)
    private String street;
    @OneToMany(name = "office_id", className = Staff.class)
    private Set<Staff> staffs = new HashSet<>();
    @OneToMany(name = "patient_id", className = Patient.class)
    private Set<Patient> patients = new HashSet<>();
    @Column(name = "url")
    private String url;

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

    public Set<Staff> getStaffs() {
        return this.staffs;
    }

    public Set<Patient> getPatients() {
        return patients;
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
