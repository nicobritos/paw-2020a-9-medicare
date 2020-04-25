package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.Date;

@Table(name = "workday", primaryKey = "workday_id")
public class Workday extends GenericModel<Workday, Integer> {
    @ManyToOne(name = "staff_id", required = true)
    private Staff staff;
    @Column(name = "start_date", required = true)
    private Date startDate;
    @Column(name = "end_date", required = true)
    private Date endDate;

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Workday;
    }
}
