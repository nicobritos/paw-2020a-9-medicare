package ar.edu.itba.paw.webapp.models;

import java.util.Collection;

public class DoctorPagination {
    private int page;
    private int perPage;
    private Collection<Integer> localityIds;
    private Collection<Integer> specialtyIds;
    private String name;

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return this.perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public Collection<Integer> getLocalityIds() {
        return this.localityIds;
    }

    public void setLocalityIds(Collection<Integer> localityIds) {
        this.localityIds = localityIds;
    }

    public Collection<Integer> getSpecialtyIds() {
        return this.specialtyIds;
    }

    public void setSpecialtyIds(Collection<Integer> specialtyIds) {
        this.specialtyIds = specialtyIds;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
