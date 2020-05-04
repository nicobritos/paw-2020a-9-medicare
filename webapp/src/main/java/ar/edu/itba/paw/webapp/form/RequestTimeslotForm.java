package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RequestTimeslotForm {
    @Min(2000)
    private int fromYear;
    @Min(1)
    @Max(12)
    private int fromMonth;
    @Min(1)
    @Max(31)
    private int fromDay;
    @Min(2000)
    private int toYear;
    @Min(1)
    @Max(12)
    private int toMonth;
    @Min(1)
    @Max(31)
    private int toDay;

    public int getFromYear() {
        return this.fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getFromMonth() {
        return this.fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getFromDay() {
        return this.fromDay;
    }

    public void setFromDay(int fromDay) {
        this.fromDay = fromDay;
    }

    public int getToYear() {
        return this.toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public int getToMonth() {
        return this.toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public int getToDay() {
        return this.toDay;
    }

    public void setToDay(int toDay) {
        this.toDay = toDay;
    }
}
