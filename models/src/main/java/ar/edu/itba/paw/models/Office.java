package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.OneToOne;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Collection;

@Table(name = "office", primaryKey = "office_id")
public class Office extends GenericModel<Integer> {
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "name", required = true)
    private String name;
    private int provinceId;
    @OneToOne(name = "province_id", required = true)
    private Province province;
    @Column(name = "street_number", required = true)
    private int streetNumber;
    @Column(name = "street", required = true)
    private String street;
    @OneToMany(joinedName = "office_id", required = true, className = Staff.class)
    private Collection<Staff> staffs;

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

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public int getStreetNumber() {
        return this.streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Collection<Staff> getStaffs() {
        return this.staffs;
    }

    public void setStaffs(Collection<Staff> staffs) {
        this.staffs = staffs;
    }

    public int getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "Office{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", provinceId=" + provinceId +
                ", province=" + province +
                ", streetNumber=" + streetNumber +
                ", street='" + street + '\'' +
                ", staffs=" + staffs +
                ", id=" + id +
                '}';
    }
}
