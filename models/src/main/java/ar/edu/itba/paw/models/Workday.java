package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "workday", primaryKey = "workday_id")
public class Workday extends GenericModel<Integer> {
    private Staff staff;
    @Column(name = "start_hour", required = true)
    private Integer startHour;
    @Column(name = "end_hour", required = true)
    private Integer endHour;
    @Column(name = "start_minute", required = true)
    private Integer startMinute;
    @Column(name = "end_minute", required = true)
    private Integer endMinute;
    @Column(name = "day", required = true)
    private String day;
    @Column(name = "staff_id", required = true)
    private int staffId;

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
        this.staffId = staff.getId();
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

    public Integer getStartMinute() {
        return this.startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndMinute() {
        return this.endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;

        if(this.staff != null && !this.staff.getId().equals(staffId)){
            this.staff = null;
        }
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Workday;
    }
}
