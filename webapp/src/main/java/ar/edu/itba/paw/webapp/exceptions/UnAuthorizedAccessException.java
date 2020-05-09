package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.interfaces.MediCareException;

public class UnAuthorizedAccessException extends MediCareException {
    public UnAuthorizedAccessException() {
        super("No se encuentra autenticado");
    }
}
