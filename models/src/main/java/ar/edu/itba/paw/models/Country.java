package ar.edu.itba.paw.models;

public class Country extends GenericModel<String> {
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
