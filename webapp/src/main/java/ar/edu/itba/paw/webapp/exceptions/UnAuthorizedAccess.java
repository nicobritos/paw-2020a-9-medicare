package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.interfaces.MediCareException;

public class UnAuthorizedAccess extends MediCareException {
    public UnAuthorizedAccess() {
        super("No se encuentra autenticado");
    }
}
