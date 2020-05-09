package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.interfaces.MediCareException;

public class UserNotVerifiedException extends MediCareException {
    public UserNotVerifiedException() {
        super("La cuenta no se encuentra confirmada");
    }
}
