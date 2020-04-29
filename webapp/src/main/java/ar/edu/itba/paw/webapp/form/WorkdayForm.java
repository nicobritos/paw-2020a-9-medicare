package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class WorkdayForm {
    @Min(0)
    @Max(7)
    private int dow;

    @Pattern(regexp="(2[0-3]|[01][0-9]):[0-5][0-9]")
    private String startHour;

    @Pattern(regexp="(2[0-3]|[01][0-9]):[0-5][0-9]")
    private String endHour;

    @Min(0)
    private int id;

    public int getDow() {
        return dow;
    }

    public void setDow(int dow) {
        this.dow = dow;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
