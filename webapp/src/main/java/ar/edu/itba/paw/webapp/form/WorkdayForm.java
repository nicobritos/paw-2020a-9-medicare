package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.*;
import java.util.List;

public class WorkdayForm {

    @NotNull
    @Size(min=7,max = 7)
    private boolean[] dow={false,false,false,false,false,false,false};

    @Pattern(regexp = "(2[0-3]|[01][0-9]):[0-5][0-9]")
    private String startHour;

    @Pattern(regexp = "(2[0-3]|[01][0-9]):[0-5][0-9]")
    private String endHour;

    @Min(0)
    private int officeId;

    public boolean[] getDow() {
        return dow;
    }

    public void setDow(boolean[] dow) {
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

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }
}
