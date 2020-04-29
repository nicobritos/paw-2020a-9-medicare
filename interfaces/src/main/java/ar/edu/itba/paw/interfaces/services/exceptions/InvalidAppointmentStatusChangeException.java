package ar.edu.itba.paw.interfaces.services.exceptions;

import ar.edu.itba.paw.interfaces.MediCareException;

public class InvalidAppointmentStatusChangeException extends MediCareException {
    public InvalidAppointmentStatusChangeException() {
        super("Estado invalido");
    }
}
