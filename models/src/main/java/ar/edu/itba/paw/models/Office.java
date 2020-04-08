package ar.edu.itba.paw.models;

public class Office extends GenericModel<Integer> {
    private String name;
    private Address address;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
