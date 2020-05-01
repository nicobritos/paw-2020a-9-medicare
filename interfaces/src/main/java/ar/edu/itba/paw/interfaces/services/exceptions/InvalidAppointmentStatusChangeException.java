package ar.edu.itba.paw.interfaces.services.exceptions;

import ar.edu.itba.paw.interfaces.MediCareException;

public class InvalidAppointmentStatusChangeException extends MediCareException {
    public InvalidAppointmentStatusChangeException(String formStatus, String toStatus) {
        super("No se puede cambiar este turno del estado " + formStatus + " al estado " + toStatus);
    }
}
