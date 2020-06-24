package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RequestAppointmentForm {
    @Min(0)
    private int staffId;
    @Min(2000)
    private int year;
    @Min(1)
    @Max(12)
    private int month;
    @Min(1)
    @Max(31)
    private int day;
    @Min(0)
    @Max(23)
    private int hour;
    @Min(0)
    @Max(59)
    private int minute;
    @NotEmpty
    private String motive;
    private String comment;
    private String phone;

    public int getStaffId() {
        return this.staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return this.hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
