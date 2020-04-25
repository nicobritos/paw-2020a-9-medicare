package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "workday", primaryKey = "workday_id")
public class Workday extends GenericModel<Workday, Integer> {
    @ManyToOne(name = "staff_id", required = true, inverse = true)
    private Staff staff;
    @Column(name = "start_hour", required = true)
    private Integer startHour;
    @Column(name = "end_hour", required = true)
    private Integer endHour;
    @Column(name = "day", required = true)
    private String day;

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Integer getStartHour() {
        return this.startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return this.endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Workday;
    }
}
