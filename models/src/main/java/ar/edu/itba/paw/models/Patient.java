package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "patient", primaryKey = "patient_id")
public class Patient extends GenericModel<Patient, Integer> {
    @ManyToOne(name = "user_id", inverse = true)
    private User user;
    @ManyToOne(name = "office_id", inverse = true)
    private Office office;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Patient;
    }
}
