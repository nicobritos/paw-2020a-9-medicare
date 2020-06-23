package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;

public class SpecialtyForm {
    @Min(0)
    private int specialtyId;

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }
}
