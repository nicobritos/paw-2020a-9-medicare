package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "patient", primaryKey = "patient_id")
public class Patient extends GenericModel<Integer> {
    private User user;
    private Office office;
    @Column(name = "user_id", required = true)
    private int userId;
    @Column(name = "office_id", required = true)
    private int officeId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        if(this.user != null && !this.user.getId().equals(userId)){
            this.user = null;
        }
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
        if(this.office != null && !this.office.getId().equals(officeId)){
            this.office = null;
        }
    }

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
