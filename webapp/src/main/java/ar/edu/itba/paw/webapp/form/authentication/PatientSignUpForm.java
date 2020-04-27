package ar.edu.itba.paw.webapp.form.authentication;

public class PatientSignUpForm extends UserSignUpForm {
    @Override
    public boolean isStaff() {
        return false;
    }
}
